import React, { useState, useEffect } from 'react';
import api from '../services/api';

const CustomerManagement = () => {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showHistoryModal, setShowHistoryModal] = useState(false);
  const [showPreferencesModal, setShowPreferencesModal] = useState(false);
  const [showLoyaltyModal, setShowLoyaltyModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterLevel, setFilterLevel] = useState('ALL');
  const [loading, setLoading] = useState(false);
  const [customerHistory, setCustomerHistory] = useState([]);
  const [loyaltyBenefits, setLoyaltyBenefits] = useState([]);
  const [staff, setStaff] = useState([]);
  const [branches, setBranches] = useState([]);

  // Form states
  const [customerForm, setCustomerForm] = useState({
    firstName: '',
    lastName: '',
    phone: '',
    email: '',
    address: '',
    dateOfBirth: '',
    loyaltyPoints: 0,
    membershipLevel: 'BRONZE'
  });

  const [preferencesForm, setPreferencesForm] = useState({
    preferredStaffId: '',
    preferredBranchId: '',
    preferredTimeSlot: '',
    communicationPreference: 'SMS',
    receivePromotions: true,
    receiveReminders: true,
    notes: ''
  });

  const [loyaltyForm, setLoyaltyForm] = useState({
    membershipLevel: 'BRONZE',
    benefitType: 'DISCOUNT',
    description: '',
    discountPercentage: '',
    discountAmount: '',
    freeServiceId: '',
    bonusPointsMultiplier: '',
    isActive: true
  });

  useEffect(() => {
    fetchCustomers();
    fetchStaff();
    fetchBranches();
    fetchLoyaltyBenefits();
  }, []);

  const fetchCustomers = async () => {
    try {
      setLoading(true);
      const response = await api.get('/customers');
      setCustomers(response.data);
    } catch (error) {
      console.error('Error fetching customers:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchStaff = async () => {
    try {
      const response = await api.get('/staff');
      setStaff(response.data);
    } catch (error) {
      console.error('Error fetching staff:', error);
    }
  };

  const fetchBranches = async () => {
    try {
      const response = await api.get('/branches');
      setBranches(response.data);
    } catch (error) {
      console.error('Error fetching branches:', error);
    }
  };

  const fetchLoyaltyBenefits = async () => {
    try {
      const response = await api.get('/customers/loyalty-benefits');
      setLoyaltyBenefits(response.data);
    } catch (error) {
      console.error('Error fetching loyalty benefits:', error);
    }
  };

  const fetchCustomerHistory = async (customerId) => {
    try {
      const response = await api.get(`/customers/${customerId}/history`);
      setCustomerHistory(response.data);
    } catch (error) {
      console.error('Error fetching customer history:', error);
    }
  };

  const fetchCustomerPreferences = async (customerId) => {
    try {
      const response = await api.get(`/customers/${customerId}/preferences`);
      setPreferencesForm(response.data);
    } catch (error) {
      console.error('Error fetching customer preferences:', error);
    }
  };

  const handleAddCustomer = async (e) => {
    e.preventDefault();
    try {
      await api.post('/customers', customerForm);
      setShowAddModal(false);
      resetForm();
      fetchCustomers();
    } catch (error) {
      console.error('Error adding customer:', error);
    }
  };

  const handleEditCustomer = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/customers/${selectedCustomer.id}`, customerForm);
      setShowEditModal(false);
      resetForm();
      fetchCustomers();
    } catch (error) {
      console.error('Error updating customer:', error);
    }
  };

  const handleDeleteCustomer = async (id) => {
    if (window.confirm('Are you sure you want to delete this customer?')) {
      try {
        await api.delete(`/customers/${id}`);
        fetchCustomers();
      } catch (error) {
        console.error('Error deleting customer:', error);
      }
    }
  };

  const handleUpdatePreferences = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/customers/${selectedCustomer.id}/preferences`, preferencesForm);
      setShowPreferencesModal(false);
      fetchCustomers();
    } catch (error) {
      console.error('Error updating preferences:', error);
    }
  };

  const handleCreateLoyaltyBenefit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/customers/loyalty-benefits', loyaltyForm);
      setShowLoyaltyModal(false);
      resetLoyaltyForm();
      fetchLoyaltyBenefits();
    } catch (error) {
      console.error('Error creating loyalty benefit:', error);
    }
  };

  const resetForm = () => {
    setCustomerForm({
      firstName: '',
      lastName: '',
      phone: '',
      email: '',
      address: '',
      dateOfBirth: '',
      loyaltyPoints: 0,
      membershipLevel: 'BRONZE'
    });
  };

  const resetLoyaltyForm = () => {
    setLoyaltyForm({
      membershipLevel: 'BRONZE',
      benefitType: 'DISCOUNT',
      description: '',
      discountPercentage: '',
      discountAmount: '',
      freeServiceId: '',
      bonusPointsMultiplier: '',
      isActive: true
    });
  };

  const openEditModal = (customer) => {
    setSelectedCustomer(customer);
    setCustomerForm({
      firstName: customer.firstName,
      lastName: customer.lastName,
      phone: customer.phone,
      email: customer.email || '',
      address: customer.address || '',
      dateOfBirth: customer.dateOfBirth || '',
      loyaltyPoints: customer.loyaltyPoints,
      membershipLevel: customer.membershipLevel
    });
    setShowEditModal(true);
  };

  const openHistoryModal = async (customer) => {
    setSelectedCustomer(customer);
    await fetchCustomerHistory(customer.id);
    setShowHistoryModal(true);
  };

  const openPreferencesModal = async (customer) => {
    setSelectedCustomer(customer);
    await fetchCustomerPreferences(customer.id);
    setShowPreferencesModal(true);
  };

  const filteredCustomers = customers.filter(customer => {
    const matchesSearch = customer.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         customer.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         customer.phone.includes(searchTerm);
    const matchesLevel = filterLevel === 'ALL' || customer.membershipLevel === filterLevel;
    return matchesSearch && matchesLevel;
  });

  const getMembershipBadgeColor = (level) => {
    switch (level) {
      case 'BRONZE': return 'bg-amber-100 text-amber-800';
      case 'SILVER': return 'bg-gray-100 text-gray-800';
      case 'GOLD': return 'bg-yellow-100 text-yellow-800';
      case 'PLATINUM': return 'bg-purple-100 text-purple-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
    <div>
        <h1 className="text-2xl font-bold text-gray-900">Customer Management</h1>
        <p className="mt-1 text-sm text-gray-500">
            Manage customer profiles, preferences, and loyalty benefits
          </p>
        </div>
        <div className="flex space-x-3">
          <button
            onClick={() => setShowLoyaltyModal(true)}
            className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700"
          >
            Manage Loyalty Benefits
          </button>
          <button
            onClick={() => setShowAddModal(true)}
            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
          >
            Add Customer
          </button>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex space-x-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Search customers..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>
          <select
            value={filterLevel}
            onChange={(e) => setFilterLevel(e.target.value)}
            className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="ALL">All Levels</option>
            <option value="BRONZE">Bronze</option>
            <option value="SILVER">Silver</option>
            <option value="GOLD">Gold</option>
            <option value="PLATINUM">Platinum</option>
          </select>
        </div>
      </div>

      {/* Customers Table */}
      <div className="bg-white shadow rounded-lg overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Customer
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Contact
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Membership
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Stats
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="5" className="px-6 py-4 text-center text-gray-500">
                    Loading...
                  </td>
                </tr>
              ) : filteredCustomers.length === 0 ? (
                <tr>
                  <td colSpan="5" className="px-6 py-4 text-center text-gray-500">
                    No customers found
                  </td>
                </tr>
              ) : (
                filteredCustomers.map((customer) => (
                  <tr key={customer.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">
                          {customer.firstName} {customer.lastName}
                        </div>
                        <div className="text-sm text-gray-500">
                          ID: {customer.id}
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-gray-900">{customer.phone}</div>
                      <div className="text-sm text-gray-500">{customer.email}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getMembershipBadgeColor(customer.membershipLevel)}`}>
                        {customer.membershipLevel}
                      </span>
                      <div className="text-sm text-gray-500">
                        {customer.loyaltyPoints} points
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      <div>Visits: {customer.totalVisits}</div>
                      <div>Spent: Rs. {customer.totalSpent}</div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                      <button
                        onClick={() => openHistoryModal(customer)}
                        className="text-blue-600 hover:text-blue-900"
                      >
                        History
                      </button>
                      <button
                        onClick={() => openPreferencesModal(customer)}
                        className="text-green-600 hover:text-green-900"
                      >
                        Preferences
                      </button>
                      <button
                        onClick={() => openEditModal(customer)}
                        className="text-indigo-600 hover:text-indigo-900"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDeleteCustomer(customer.id)}
                        className="text-red-600 hover:text-red-900"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Customer Modal */}
      {showAddModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Add New Customer</h3>
              <form onSubmit={handleAddCustomer} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="text"
                    placeholder="First Name"
                    value={customerForm.firstName}
                    onChange={(e) => setCustomerForm({...customerForm, firstName: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    required
                  />
                  <input
                    type="text"
                    placeholder="Last Name"
                    value={customerForm.lastName}
                    onChange={(e) => setCustomerForm({...customerForm, lastName: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <input
                  type="tel"
                  placeholder="Phone Number"
                  value={customerForm.phone}
                  onChange={(e) => setCustomerForm({...customerForm, phone: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  required
                />
                <input
                  type="email"
                  placeholder="Email"
                  value={customerForm.email}
                  onChange={(e) => setCustomerForm({...customerForm, email: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                />
                <textarea
                  placeholder="Address"
                  value={customerForm.address}
                  onChange={(e) => setCustomerForm({...customerForm, address: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  rows="3"
                />
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="date"
                    value={customerForm.dateOfBirth}
                    onChange={(e) => setCustomerForm({...customerForm, dateOfBirth: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  />
                  <select
                    value={customerForm.membershipLevel}
                    onChange={(e) => setCustomerForm({...customerForm, membershipLevel: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="BRONZE">Bronze</option>
                    <option value="SILVER">Silver</option>
                    <option value="GOLD">Gold</option>
                    <option value="PLATINUM">Platinum</option>
                  </select>
                </div>
                <div className="flex justify-end space-x-3">
                  <button
                    type="button"
                    onClick={() => setShowAddModal(false)}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-lg hover:bg-gray-300"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700"
                  >
                    Add Customer
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Edit Customer Modal */}
      {showEditModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Edit Customer</h3>
              <form onSubmit={handleEditCustomer} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="text"
                    placeholder="First Name"
                    value={customerForm.firstName}
                    onChange={(e) => setCustomerForm({...customerForm, firstName: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    required
                  />
                  <input
                    type="text"
                    placeholder="Last Name"
                    value={customerForm.lastName}
                    onChange={(e) => setCustomerForm({...customerForm, lastName: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <input
                  type="tel"
                  placeholder="Phone Number"
                  value={customerForm.phone}
                  onChange={(e) => setCustomerForm({...customerForm, phone: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  required
                />
                <input
                  type="email"
                  placeholder="Email"
                  value={customerForm.email}
                  onChange={(e) => setCustomerForm({...customerForm, email: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                />
                <textarea
                  placeholder="Address"
                  value={customerForm.address}
                  onChange={(e) => setCustomerForm({...customerForm, address: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  rows="3"
                />
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="date"
                    value={customerForm.dateOfBirth}
                    onChange={(e) => setCustomerForm({...customerForm, dateOfBirth: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  />
                  <select
                    value={customerForm.membershipLevel}
                    onChange={(e) => setCustomerForm({...customerForm, membershipLevel: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="BRONZE">Bronze</option>
                    <option value="SILVER">Silver</option>
                    <option value="GOLD">Gold</option>
                    <option value="PLATINUM">Platinum</option>
                  </select>
                </div>
                <div className="flex justify-end space-x-3">
                  <button
                    type="button"
                    onClick={() => setShowEditModal(false)}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-lg hover:bg-gray-300"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700"
                  >
                    Update Customer
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Customer History Modal */}
      {showHistoryModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-10 mx-auto p-5 border w-4/5 max-w-4xl shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">
                  Appointment History - {selectedCustomer?.firstName} {selectedCustomer?.lastName}
                </h3>
                <button
                  onClick={() => setShowHistoryModal(false)}
                  className="text-gray-400 hover:text-gray-600"
                >
                  ✕
                </button>
              </div>
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Time</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Staff</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {customerHistory.map((appointment) => (
                      <tr key={appointment.appointmentId}>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {appointment.appointmentDate}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {appointment.startTime} - {appointment.endTime}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {appointment.staffName}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                            appointment.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                            appointment.status === 'CANCELLED' ? 'bg-red-100 text-red-800' :
                            'bg-yellow-100 text-yellow-800'
                          }`}>
                            {appointment.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          Rs. {appointment.totalAmount}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Customer Preferences Modal */}
      {showPreferencesModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-10 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">Customer Preferences</h3>
                <button
                  onClick={() => setShowPreferencesModal(false)}
                  className="text-gray-400 hover:text-gray-600"
                >
                  ✕
                </button>
              </div>
              <form onSubmit={handleUpdatePreferences} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Preferred Staff</label>
                  <select
                    value={preferencesForm.preferredStaffId || ''}
                    onChange={(e) => setPreferencesForm({...preferencesForm, preferredStaffId: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Select Staff</option>
                    {staff.map((s) => (
                      <option key={s.id} value={s.id}>
                        {s.firstName} {s.lastName}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Preferred Branch</label>
                  <select
                    value={preferencesForm.preferredBranchId || ''}
                    onChange={(e) => setPreferencesForm({...preferencesForm, preferredBranchId: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Select Branch</option>
                    {branches.map((b) => (
                      <option key={b.id} value={b.id}>
                        {b.name}
                      </option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Preferred Time Slot</label>
                  <select
                    value={preferencesForm.preferredTimeSlot || ''}
                    onChange={(e) => setPreferencesForm({...preferencesForm, preferredTimeSlot: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Select Time Slot</option>
                    <option value="MORNING">Morning (9AM - 12PM)</option>
                    <option value="AFTERNOON">Afternoon (12PM - 5PM)</option>
                    <option value="EVENING">Evening (5PM - 8PM)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Communication Preference</label>
                  <select
                    value={preferencesForm.communicationPreference}
                    onChange={(e) => setPreferencesForm({...preferencesForm, communicationPreference: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="SMS">SMS</option>
                    <option value="EMAIL">Email</option>
                    <option value="PHONE">Phone Call</option>
                  </select>
                </div>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input
                      type="checkbox"
                      checked={preferencesForm.receivePromotions}
                      onChange={(e) => setPreferencesForm({...preferencesForm, receivePromotions: e.target.checked})}
                      className="mr-2"
                    />
                    Receive Promotions
                  </label>
                  <label className="flex items-center">
                    <input
                      type="checkbox"
                      checked={preferencesForm.receiveReminders}
                      onChange={(e) => setPreferencesForm({...preferencesForm, receiveReminders: e.target.checked})}
                      className="mr-2"
                    />
                    Receive Appointment Reminders
                  </label>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Notes</label>
                  <textarea
                    value={preferencesForm.notes || ''}
                    onChange={(e) => setPreferencesForm({...preferencesForm, notes: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    rows="3"
                  />
                </div>
                <div className="flex justify-end space-x-3">
                  <button
                    type="button"
                    onClick={() => setShowPreferencesModal(false)}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-lg hover:bg-gray-300"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 text-sm font-medium text-white bg-green-600 rounded-lg hover:bg-green-700"
                  >
                    Save Preferences
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Loyalty Benefits Modal */}
      {showLoyaltyModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-10 mx-auto p-5 border w-4/5 max-w-6xl shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">Loyalty Benefits Management</h3>
                <div className="flex space-x-2">
                  <button
                    onClick={() => setShowLoyaltyModal(false)}
                    className="text-gray-400 hover:text-gray-600"
                  >
                    ✕
                  </button>
                </div>
              </div>
              
              {/* Add New Benefit Form */}
              <div className="mb-6 p-4 bg-gray-50 rounded-lg">
                <h4 className="text-md font-medium text-gray-900 mb-3">Add New Loyalty Benefit</h4>
                <form onSubmit={handleCreateLoyaltyBenefit} className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <select
                    value={loyaltyForm.membershipLevel}
                    onChange={(e) => setLoyaltyForm({...loyaltyForm, membershipLevel: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value="BRONZE">Bronze</option>
                    <option value="SILVER">Silver</option>
                    <option value="GOLD">Gold</option>
                    <option value="PLATINUM">Platinum</option>
                  </select>
                  <select
                    value={loyaltyForm.benefitType}
                    onChange={(e) => setLoyaltyForm({...loyaltyForm, benefitType: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    required
                  >
                    <option value="DISCOUNT">Discount</option>
                    <option value="FREE_SERVICE">Free Service</option>
                    <option value="PRIORITY_BOOKING">Priority Booking</option>
                    <option value="BONUS_POINTS">Bonus Points</option>
                  </select>
                  <input
                    type="text"
                    placeholder="Description"
                    value={loyaltyForm.description}
                    onChange={(e) => setLoyaltyForm({...loyaltyForm, description: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    required
                  />
                  <input
                    type="number"
                    placeholder="Discount %"
                    value={loyaltyForm.discountPercentage}
                    onChange={(e) => setLoyaltyForm({...loyaltyForm, discountPercentage: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    step="0.01"
                  />
                  <input
                    type="number"
                    placeholder="Discount Amount"
                    value={loyaltyForm.discountAmount}
                    onChange={(e) => setLoyaltyForm({...loyaltyForm, discountAmount: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    step="0.01"
                  />
                  <input
                    type="number"
                    placeholder="Points Multiplier"
                    value={loyaltyForm.bonusPointsMultiplier}
                    onChange={(e) => setLoyaltyForm({...loyaltyForm, bonusPointsMultiplier: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  />
                  <button
                    type="submit"
                    className="px-4 py-2 text-sm font-medium text-white bg-purple-600 rounded-lg hover:bg-purple-700"
                  >
                    Add Benefit
                  </button>
                </form>
      </div>

              {/* Benefits List */}
              <div className="overflow-x-auto">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Level</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Type</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Value</th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {loyaltyBenefits.map((benefit) => (
                      <tr key={benefit.id}>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getMembershipBadgeColor(benefit.membershipLevel)}`}>
                            {benefit.membershipLevel}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {benefit.benefitType.replace('_', ' ')}
                        </td>
                        <td className="px-6 py-4 text-sm text-gray-900">
                          {benefit.description}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {benefit.discountPercentage && `${benefit.discountPercentage}%`}
                          {benefit.discountAmount && `Rs. ${benefit.discountAmount}`}
                          {benefit.bonusPointsMultiplier && `${benefit.bonusPointsMultiplier}x points`}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                            benefit.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                          }`}>
                            {benefit.isActive ? 'Active' : 'Inactive'}
            </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
          </div>
        </div>
      </div>
        </div>
      )}
    </div>
  );
};

export default CustomerManagement;


