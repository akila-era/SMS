import React, { useState, useEffect } from 'react';
import { 
  PlusIcon, 
  MagnifyingGlassIcon, 
  FunnelIcon,
  ChartBarIcon,
  UserGroupIcon,
  BuildingOfficeIcon,
  MapPinIcon,
  PhoneIcon,
  EnvelopeIcon,
  PencilIcon,
  TrashIcon,
  EyeIcon,
  XMarkIcon,
  CheckIcon
} from '@heroicons/react/24/outline';
import api from '../services/api';

const BranchManagement = () => {
  const [branches, setBranches] = useState([]);
  const [filteredBranches, setFilteredBranches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [showAccessModal, setShowAccessModal] = useState(false);
  const [showDashboard, setShowDashboard] = useState(false);
  const [editingBranch, setEditingBranch] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [cityFilter, setCityFilter] = useState('ALL');
  const [dashboardData, setDashboardData] = useState(null);
  const [selectedBranch, setSelectedBranch] = useState(null);

  // Form state
  const [formData, setFormData] = useState({
    branchCode: '',
    branchName: '',
    address: '',
    city: '',
    postalCode: '',
    contactNumber: '',
    email: '',
    managerId: '',
    workingHours: '{"open":"09:00","close":"19:00","days":["MON","TUE","WED","THU","FRI","SAT"]}',
    status: 'ACTIVE',
    logoUrl: ''
  });

  useEffect(() => {
    fetchBranches();
  }, []);

  useEffect(() => {
    filterBranches();
  }, [branches, searchTerm, statusFilter, cityFilter]);

  const fetchBranches = async () => {
    try {
      setLoading(true);
      const response = await api.get('/branches');
      setBranches(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch branches');
      console.error('Error fetching branches:', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchDashboard = async () => {
    try {
      const response = await api.get('/branches/dashboard');
      setDashboardData(response.data);
    } catch (err) {
      console.error('Error fetching dashboard:', err);
    }
  };

  const filterBranches = () => {
    let filtered = branches;

    if (searchTerm) {
      filtered = filtered.filter(branch =>
        branch.branchName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        branch.branchCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
        branch.city.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(branch => branch.status === statusFilter);
    }

    if (cityFilter !== 'ALL') {
      filtered = filtered.filter(branch => branch.city === cityFilter);
    }

    setFilteredBranches(filtered);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingBranch) {
        await api.put(`/branches/${editingBranch.id}`, formData);
      } else {
        await api.post('/branches', formData);
      }
      setShowModal(false);
      setEditingBranch(null);
      resetForm();
      fetchBranches();
    } catch (err) {
      setError('Failed to save branch');
      console.error('Error saving branch:', err);
    }
  };

  const handleEdit = (branch) => {
    setEditingBranch(branch);
    setFormData({
      branchCode: branch.branchCode,
      branchName: branch.branchName,
      address: branch.address || '',
      city: branch.city || '',
      postalCode: branch.postalCode || '',
      contactNumber: branch.contactNumber || '',
      email: branch.email || '',
      managerId: branch.managerId || '',
      workingHours: branch.workingHours || '{"open":"09:00","close":"19:00","days":["MON","TUE","WED","THU","FRI","SAT"]}',
      status: branch.status,
      logoUrl: branch.logoUrl || ''
    });
    setShowModal(true);
  };

  const handleDelete = async (branchId) => {
    if (window.confirm('Are you sure you want to delete this branch?')) {
      try {
        await api.delete(`/branches/${branchId}`);
        fetchBranches();
      } catch (err) {
        setError('Failed to delete branch');
        console.error('Error deleting branch:', err);
      }
    }
  };

  const handleToggleStatus = async (branchId) => {
    try {
      await api.patch(`/branches/${branchId}/toggle-status`);
      fetchBranches();
    } catch (err) {
      setError('Failed to toggle branch status');
      console.error('Error toggling status:', err);
    }
  };

  const resetForm = () => {
    setFormData({
      branchCode: '',
      branchName: '',
      address: '',
      city: '',
      postalCode: '',
      contactNumber: '',
      email: '',
      managerId: '',
      workingHours: '{"open":"09:00","close":"19:00","days":["MON","TUE","WED","THU","FRI","SAT"]}',
      status: 'ACTIVE',
      logoUrl: ''
    });
  };

  const openModal = () => {
    setEditingBranch(null);
    resetForm();
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setEditingBranch(null);
    resetForm();
  };

  const openAccessModal = (branch) => {
    setSelectedBranch(branch);
    setShowAccessModal(true);
  };

  const openDashboard = () => {
    setShowDashboard(true);
    fetchDashboard();
  };

  const getStatusBadge = (status) => {
    const baseClasses = "inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium";
    if (status === 'ACTIVE') {
      return `${baseClasses} bg-green-100 text-green-800`;
    }
    return `${baseClasses} bg-red-100 text-red-800`;
  };

  const cities = [...new Set(branches.map(branch => branch.city).filter(Boolean))];

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
    <div>
        <h1 className="text-2xl font-bold text-gray-900">Branch Management</h1>
        <p className="mt-1 text-sm text-gray-500">
            Manage your salon branches and locations across multiple cities.
          </p>
        </div>
        <div className="flex space-x-3">
          <button
            onClick={openDashboard}
            className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
          >
            <ChartBarIcon className="h-4 w-4 mr-2" />
            Dashboard
          </button>
          <button
            onClick={openModal}
            className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
          >
            <PlusIcon className="h-4 w-4 mr-2" />
            Add Branch
          </button>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white shadow rounded-lg p-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Search</label>
            <div className="relative">
              <MagnifyingGlassIcon className="h-5 w-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
              <input
                type="text"
                placeholder="Search branches..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="ALL">All Status</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">City</label>
            <select
              value={cityFilter}
              onChange={(e) => setCityFilter(e.target.value)}
              className="block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="ALL">All Cities</option>
              {cities.map(city => (
                <option key={city} value={city}>{city}</option>
              ))}
            </select>
          </div>
          <div className="flex items-end">
            <button
              onClick={() => {
                setSearchTerm('');
                setStatusFilter('ALL');
                setCityFilter('ALL');
              }}
              className="w-full inline-flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <FunnelIcon className="h-4 w-4 mr-2" />
              Clear Filters
            </button>
          </div>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="bg-red-50 border border-red-200 rounded-md p-4">
          <div className="flex">
            <div className="flex-shrink-0">
              <XMarkIcon className="h-5 w-5 text-red-400" />
            </div>
            <div className="ml-3">
              <p className="text-sm text-red-800">{error}</p>
            </div>
          </div>
        </div>
      )}

      {/* Branches Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredBranches.map((branch) => (
          <div key={branch.id} className="bg-white shadow rounded-lg overflow-hidden">
            <div className="p-6">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center">
                  <BuildingOfficeIcon className="h-8 w-8 text-blue-600 mr-3" />
                  <div>
                    <h3 className="text-lg font-medium text-gray-900">{branch.branchName}</h3>
                    <p className="text-sm text-gray-500">{branch.branchCode}</p>
                  </div>
                </div>
                <span className={getStatusBadge(branch.status)}>
                  {branch.status}
                </span>
              </div>

              <div className="space-y-2 mb-4">
                <div className="flex items-center text-sm text-gray-600">
                  <MapPinIcon className="h-4 w-4 mr-2" />
                  <span>{branch.city}, {branch.postalCode}</span>
                </div>
                {branch.contactNumber && (
                  <div className="flex items-center text-sm text-gray-600">
                    <PhoneIcon className="h-4 w-4 mr-2" />
                    <span>{branch.contactNumber}</span>
                  </div>
                )}
                {branch.email && (
                  <div className="flex items-center text-sm text-gray-600">
                    <EnvelopeIcon className="h-4 w-4 mr-2" />
                    <span>{branch.email}</span>
                  </div>
                )}
                {branch.managerName && (
                  <div className="flex items-center text-sm text-gray-600">
                    <UserGroupIcon className="h-4 w-4 mr-2" />
                    <span>Manager: {branch.managerName}</span>
                  </div>
                )}
              </div>

              <div className="flex items-center justify-between text-sm text-gray-500 mb-4">
                <span>{branch.staffCount} Staff</span>
                <span>{branch.appointmentCount} Appointments</span>
                <span>{branch.customerCount} Customers</span>
              </div>

              <div className="flex space-x-2">
                <button
                  onClick={() => handleEdit(branch)}
                  className="flex-1 inline-flex items-center justify-center px-3 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <PencilIcon className="h-4 w-4 mr-1" />
                  Edit
                </button>
                <button
                  onClick={() => openAccessModal(branch)}
                  className="flex-1 inline-flex items-center justify-center px-3 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <UserGroupIcon className="h-4 w-4 mr-1" />
                  Access
                </button>
                <button
                  onClick={() => handleToggleStatus(branch.id)}
                  className={`px-3 py-2 rounded-md text-sm font-medium ${
                    branch.status === 'ACTIVE'
                      ? 'bg-red-100 text-red-700 hover:bg-red-200'
                      : 'bg-green-100 text-green-700 hover:bg-green-200'
                  }`}
                >
                  {branch.status === 'ACTIVE' ? 'Deactivate' : 'Activate'}
                </button>
                <button
                  onClick={() => handleDelete(branch.id)}
                  className="px-3 py-2 text-red-600 hover:text-red-800"
                >
                  <TrashIcon className="h-4 w-4" />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredBranches.length === 0 && (
        <div className="text-center py-12">
          <BuildingOfficeIcon className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">No branches found</h3>
          <p className="mt-1 text-sm text-gray-500">
            {searchTerm || statusFilter !== 'ALL' || cityFilter !== 'ALL'
              ? 'Try adjusting your search criteria.'
              : 'Get started by creating a new branch.'}
          </p>
        </div>
      )}

      {/* Add/Edit Branch Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-medium text-gray-900">
                {editingBranch ? 'Edit Branch' : 'Add New Branch'}
              </h3>
              <button onClick={closeModal} className="text-gray-400 hover:text-gray-600">
                <XMarkIcon className="h-6 w-6" />
              </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Branch Code *</label>
                  <input
                    type="text"
                    required
                    value={formData.branchCode}
                    onChange={(e) => setFormData({...formData, branchCode: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    placeholder="e.g., COL001"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Branch Name *</label>
                  <input
                    type="text"
                    required
                    value={formData.branchName}
                    onChange={(e) => setFormData({...formData, branchName: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    placeholder="e.g., Main Branch"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Address</label>
                <textarea
                  value={formData.address}
                  onChange={(e) => setFormData({...formData, address: e.target.value})}
                  rows={2}
                  className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Full address"
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">City</label>
                  <input
                    type="text"
                    value={formData.city}
                    onChange={(e) => setFormData({...formData, city: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    placeholder="e.g., Colombo"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Postal Code</label>
                  <input
                    type="text"
                    value={formData.postalCode}
                    onChange={(e) => setFormData({...formData, postalCode: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    placeholder="e.g., 00100"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Status</label>
                  <select
                    value={formData.status}
                    onChange={(e) => setFormData({...formData, status: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                  </select>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Contact Number</label>
                  <input
                    type="tel"
                    value={formData.contactNumber}
                    onChange={(e) => setFormData({...formData, contactNumber: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    placeholder="+94-11-234-5678"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Email</label>
                  <input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({...formData, email: e.target.value})}
                    className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    placeholder="branch@salon.com"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Logo URL</label>
                <input
                  type="url"
                  value={formData.logoUrl}
                  onChange={(e) => setFormData({...formData, logoUrl: e.target.value})}
                  className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                  placeholder="https://example.com/logo.png"
                />
              </div>

              <div className="flex justify-end space-x-3 pt-4">
                <button
                  type="button"
                  onClick={closeModal}
                  className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
                >
                  {editingBranch ? 'Update Branch' : 'Create Branch'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Branch Access Modal */}
      {showAccessModal && selectedBranch && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-medium text-gray-900">
                User Access - {selectedBranch.branchName}
              </h3>
              <button onClick={() => setShowAccessModal(false)} className="text-gray-400 hover:text-gray-600">
                <XMarkIcon className="h-6 w-6" />
              </button>
            </div>
            <div className="text-center py-8">
              <UserGroupIcon className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">User Access Management</h3>
              <p className="mt-1 text-sm text-gray-500">
                This feature will be implemented in the next phase.
              </p>
            </div>
          </div>
        </div>
      )}

      {/* Dashboard Modal */}
      {showDashboard && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-10 mx-auto p-5 border w-11/12 md:w-4/5 lg:w-3/4 shadow-lg rounded-md bg-white">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-medium text-gray-900">Branch Dashboard</h3>
              <button onClick={() => setShowDashboard(false)} className="text-gray-400 hover:text-gray-600">
                <XMarkIcon className="h-6 w-6" />
              </button>
            </div>
            <div className="text-center py-8">
              <ChartBarIcon className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">Branch Analytics Dashboard</h3>
              <p className="mt-1 text-sm text-gray-500">
                This feature will be implemented in the next phase.
              </p>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BranchManagement;


