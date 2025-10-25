import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import BranchManagement from './pages/BranchManagement';
import StaffManagement from './pages/StaffManagement';
import ServiceManagement from './pages/ServiceManagement';
import CustomerManagement from './pages/CustomerManagement';
import AppointmentManagement from './pages/AppointmentManagement';
import CommissionManagement from './pages/CommissionManagement';
import InventoryManagement from './pages/InventoryManagement';
import BillingManagement from './pages/BillingManagement';
import PayrollManagement from './pages/PayrollManagement';
import Reports from './pages/Reports';
import Settings from './pages/Settings';
import Layout from './components/Layout';
import './index.css';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
          <div className="App">
            <Toaster position="top-right" />
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/" element={
                <ProtectedRoute>
                  <Layout />
                </ProtectedRoute>
              }>
                <Route index element={<Navigate to="/dashboard" replace />} />
                <Route path="dashboard" element={<Dashboard />} />
                <Route path="branches" element={<BranchManagement />} />
                <Route path="staff" element={<StaffManagement />} />
                <Route path="services" element={<ServiceManagement />} />
                <Route path="customers" element={<CustomerManagement />} />
                <Route path="appointments" element={<AppointmentManagement />} />
                <Route path="commissions" element={<CommissionManagement />} />
                <Route path="inventory" element={<InventoryManagement />} />
                <Route path="billing" element={<BillingManagement />} />
                <Route path="payroll" element={<PayrollManagement />} />
                <Route path="reports" element={<Reports />} />
                <Route path="settings" element={<Settings />} />
              </Route>
            </Routes>
          </div>
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;


