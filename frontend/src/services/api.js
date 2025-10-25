import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  logout: () => api.post('/auth/logout'),
  verifyToken: () => api.get('/auth/me'),
};

// Branch API
export const branchAPI = {
  getAll: () => api.get('/branches'),
  getById: (id) => api.get(`/branches/${id}`),
  create: (data) => api.post('/branches', data),
  update: (id, data) => api.put(`/branches/${id}moi`, data),
  delete: (id) => api.delete(`/branches/${id}`),
};

// Staff API
export const staffAPI = {
  getAll: () => api.get('/staff'),
  getById: (id) => api.get(`/staff/${id}`),
  create: (data) => api.post('/staff', data),
  update: (id, data) => api.put(`/staff/${id}`, data),
  delete: (id) => api.delete(`/staff/${id}`),
  getByBranch: (branchId) => api.get(`/staff/branch/${branchId}`),
};

// Service API
export const serviceAPI = {
  getAll: () => api.get('/services'),
  getById: (id) => api.get(`/services/${id}`),
  create: (data) => api.post('/services', data),
  update: (id, data) => api.put(`/services/${id}`, data),
  delete: (id) => api.delete(`/services/${id}`),
};

// Customer API
export const customerAPI = {
  getAll: () => api.get('/customers'),
  getById: (id) => api.get(`/customers/${id}`),
  create: (data) => api.post('/customers', data),
  update: (id, data) => api.put(`/customers/${id}`, data),
  delete: (id) => api.delete(`/customers/${id}`),
  search: (query) => api.get(`/customers/search?q=${query}`),
};

// Appointment API
export const appointmentAPI = {
  getAll: () => api.get('/appointments'),
  getById: (id) => api.get(`/appointments/${id}`),
  create: (data) => api.post('/appointments', data),
  update: (id, data) => api.put(`/appointments/${id}`, data),
  delete: (id) => api.delete(`/appointments/${id}`),
  getByBranch: (branchId) => api.get(`/appointments/branch/${branchId}`),
  getByStaff: (staffId) => api.get(`/appointments/staff/${staffId}`),
  getByCustomer: (customerId) => api.get(`/appointments/customer/${customerId}`),
  getByDate: (date) => api.get(`/appointments/date/${date}`),
  getTodays: () => api.get('/appointments/today'),
  getTodaysByBranch: (branchId) => api.get(`/appointments/today/branch/${branchId}`),
  getUpcoming: (days = 7) => api.get(`/appointments/upcoming?days=${days}`),
  updateStatus: (id, status) => api.patch(`/appointments/${id}/status?status=${status}`),
  cancel: (id, reason) => api.patch(`/appointments/${id}/cancel?reason=${reason}`),
  reschedule: (id, newDate, newStartTime, newEndTime) => 
    api.patch(`/appointments/${id}/reschedule?newDate=${newDate}&newStartTime=${newStartTime}&newEndTime=${newEndTime}`),
  getStaffAvailability: (staffId, date) => api.get(`/appointments/availability/staff/${staffId}?date=${date}`),
  getBranchAvailability: (branchId, date) => api.get(`/appointments/availability/branch/${branchId}?date=${date}`),
};

// Commission API
export const commissionAPI = {
  getAll: () => api.get('/commissions'),
  getByStaff: (staffId) => api.get(`/commissions/staff/${staffId}`),
  getByBranch: (branchId) => api.get(`/commissions/branch/${branchId}`),
  getByDateRange: (startDate, endDate) => api.get(`/commissions/range?start=${startDate}&end=${endDate}`),
};

// Inventory API
export const inventoryAPI = {
  getAll: () => api.get('/inventory'),
  getByBranch: (branchId) => api.get(`/inventory/branch/${branchId}`),
  updateStock: (id, data) => api.put(`/inventory/${id}/stock`, data),
  addProduct: (data) => api.post('/inventory', data),
};

// Billing API
export const billingAPI = {
  getAll: () => api.get('/billing'),
  getById: (id) => api.get(`/billing/${id}`),
  create: (data) => api.post('/billing', data),
  update: (id, data) => api.put(`/billing/${id}`, data),
  getByBranch: (branchId) => api.get(`/billing/branch/${branchId}`),
};

// Reports API
export const reportsAPI = {
  getBranchSummary: (branchId, startDate, endDate) => 
    api.get(`/reports/branch-summary?branchId=${branchId}&start=${startDate}&end=${endDate}`),
  getStaffCommission: (staffId, startDate, endDate) => 
    api.get(`/reports/staff-commission?staffId=${staffId}&start=${startDate}&end=${endDate}`),
  getRevenueTrends: (branchId, startDate, endDate) => 
    api.get(`/reports/revenue-trends?branchId=${branchId}&start=${startDate}&end=${endDate}`),
};

export default api;
