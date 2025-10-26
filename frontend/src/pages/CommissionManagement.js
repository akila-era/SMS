import React, { useState, useEffect } from 'react';
import { api } from '../services/api';
import CommissionAdjustmentModal from '../components/CommissionAdjustmentModal';
import CommissionReports from '../components/CommissionReports';

const CommissionManagement = () => {
  const [activeTab, setActiveTab] = useState('commissions');
  const [commissions, setCommissions] = useState([]);
  const [summaries, setSummaries] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filters, setFilters] = useState({
    staffId: '',
    branchId: '',
    status: '',
    month: ''
  });
  const [stats, setStats] = useState({
    totalPending: 0,
    totalApproved: 0,
    totalThisMonth: 0,
    pendingCount: 0
  });
  const [adjustmentModal, setAdjustmentModal] = useState({
    isOpen: false,
    commission: null
  });

  useEffect(() => {
    loadStats();
    if (activeTab === 'commissions') {
      loadCommissions();
    } else if (activeTab === 'summaries') {
      loadSummaries();
    }
  }, [activeTab, filters]);

  const loadStats = async () => {
    try {
      const [pendingTotal, approvedTotal, pendingCount] = await Promise.all([
        api.get('/commissions/statistics/pending-total'),
        api.get('/commissions/statistics/approved-current-month'),
        api.get('/commissions/statistics/pending-count')
      ]);
      
      setStats({
        totalPending: pendingTotal.data,
        totalApproved: approvedTotal.data,
        totalThisMonth: approvedTotal.data,
        pendingCount: pendingCount.data
      });
    } catch (error) {
      console.error('Error loading stats:', error);
    }
  };

  const loadCommissions = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams();
      if (filters.staffId) params.append('staffId', filters.staffId);
      if (filters.branchId) params.append('branchId', filters.branchId);
      if (filters.status) params.append('status', filters.status);
      
      const response = await api.get(`/commissions?${params.toString()}`);
      setCommissions(response.data.content || response.data);
    } catch (error) {
      console.error('Error loading commissions:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadSummaries = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams();
      if (filters.staffId) params.append('staffId', filters.staffId);
      if (filters.branchId) params.append('branchId', filters.branchId);
      if (filters.status) params.append('status', filters.status);
      if (filters.month) params.append('month', filters.month);
      
      const response = await api.get(`/commissions/summaries?${params.toString()}`);
      setSummaries(response.data.content || response.data);
    } catch (error) {
      console.error('Error loading summaries:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleApproveCommission = async (commissionId) => {
    try {
      await api.post(`/commissions/${commissionId}/approve`);
      loadCommissions();
      loadStats();
    } catch (error) {
      console.error('Error approving commission:', error);
    }
  };

  const handleLockCommission = async (commissionId) => {
    try {
      await api.post(`/commissions/${commissionId}/lock`);
      loadCommissions();
    } catch (error) {
      console.error('Error locking commission:', error);
    }
  };

  const handleReverseCommission = async (commissionId) => {
    if (window.confirm('Are you sure you want to reverse this commission?')) {
      try {
        await api.post(`/commissions/${commissionId}/reverse`);
        loadCommissions();
        loadStats();
      } catch (error) {
        console.error('Error reversing commission:', error);
      }
    }
  };

  const handleApproveSummary = async (summaryId) => {
    try {
      await api.post(`/commissions/summaries/${summaryId}/approve`);
      loadSummaries();
      loadStats();
    } catch (error) {
      console.error('Error approving summary:', error);
    }
  };

  const handleLockSummary = async (summaryId) => {
    try {
      await api.post(`/commissions/summaries/${summaryId}/lock`);
      loadSummaries();
    } catch (error) {
      console.error('Error locking summary:', error);
    }
  };

  const handleAdjustCommission = async (adjustmentData) => {
    try {
      await api.post('/commissions/adjust', adjustmentData);
      loadCommissions();
      loadStats();
    } catch (error) {
      console.error('Error adjusting commission:', error);
      throw error;
    }
  };

  const openAdjustmentModal = (commission) => {
    setAdjustmentModal({
      isOpen: true,
      commission: commission
    });
  };

  const closeAdjustmentModal = () => {
    setAdjustmentModal({
      isOpen: false,
      commission: null
    });
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR'
    }).format(amount);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-LK');
  };

  const getStatusBadge = (status) => {
    const statusClasses = {
      PENDING: 'bg-yellow-100 text-yellow-800',
      APPROVED: 'bg-green-100 text-green-800',
      LOCKED: 'bg-blue-100 text-blue-800',
      REVERSED: 'bg-red-100 text-red-800'
    };
    
    return (
      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${statusClasses[status] || 'bg-gray-100 text-gray-800'}`}>
        {status}
      </span>
    );
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">Commission Management</h1>
        <p className="mt-1 text-sm text-gray-500">
          Track and manage staff commissions and earnings.
        </p>
      </div>

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className="w-8 h-8 bg-yellow-500 rounded-md flex items-center justify-center">
                  <span className="text-white text-sm font-medium">₵</span>
                </div>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">Pending Commissions</dt>
                  <dd className="text-lg font-medium text-gray-900">{formatCurrency(stats.totalPending)}</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className="w-8 h-8 bg-green-500 rounded-md flex items-center justify-center">
                  <span className="text-white text-sm font-medium">✓</span>
                </div>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">Approved This Month</dt>
                  <dd className="text-lg font-medium text-gray-900">{formatCurrency(stats.totalApproved)}</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className="w-8 h-8 bg-blue-500 rounded-md flex items-center justify-center">
                  <span className="text-white text-sm font-medium">📊</span>
                </div>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">Pending Approvals</dt>
                  <dd className="text-lg font-medium text-gray-900">{stats.pendingCount}</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <div className="w-8 h-8 bg-purple-500 rounded-md flex items-center justify-center">
                  <span className="text-white text-sm font-medium">💰</span>
                </div>
              </div>
              <div className="ml-5 w-0 flex-1">
                <dl>
                  <dt className="text-sm font-medium text-gray-500 truncate">Total This Month</dt>
                  <dd className="text-lg font-medium text-gray-900">{formatCurrency(stats.totalThisMonth)}</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="-mb-px flex space-x-8">
          <button
            onClick={() => setActiveTab('commissions')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'commissions'
                ? 'border-indigo-500 text-indigo-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            Individual Commissions
          </button>
          <button
            onClick={() => setActiveTab('summaries')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'summaries'
                ? 'border-indigo-500 text-indigo-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            Monthly Summaries
          </button>
          <button
            onClick={() => setActiveTab('reports')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'reports'
                ? 'border-indigo-500 text-indigo-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            Reports & Analytics
          </button>
        </nav>
      </div>

      {/* Filters */}
      <div className="bg-white shadow rounded-lg p-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Staff</label>
            <select
              value={filters.staffId}
              onChange={(e) => setFilters({...filters, staffId: e.target.value})}
              className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            >
              <option value="">All Staff</option>
              {/* Staff options would be loaded from API */}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Branch</label>
            <select
              value={filters.branchId}
              onChange={(e) => setFilters({...filters, branchId: e.target.value})}
              className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            >
              <option value="">All Branches</option>
              {/* Branch options would be loaded from API */}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Status</label>
            <select
              value={filters.status}
              onChange={(e) => setFilters({...filters, status: e.target.value})}
              className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            >
              <option value="">All Statuses</option>
              <option value="PENDING">Pending</option>
              <option value="APPROVED">Approved</option>
              <option value="LOCKED">Locked</option>
              <option value="REVERSED">Reversed</option>
            </select>
          </div>
          {activeTab === 'summaries' && (
            <div>
              <label className="block text-sm font-medium text-gray-700">Month</label>
              <input
                type="month"
                value={filters.month}
                onChange={(e) => setFilters({...filters, month: e.target.value})}
                className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              />
            </div>
          )}
        </div>
      </div>

      {/* Content */}
      {activeTab === 'commissions' && (
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <div className="px-4 py-5 sm:px-6">
            <h3 className="text-lg leading-6 font-medium text-gray-900">Individual Commissions</h3>
            <p className="mt-1 max-w-2xl text-sm text-gray-500">
              Detailed view of all commission transactions
            </p>
          </div>
          {loading ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
              <p className="mt-2 text-sm text-gray-500">Loading commissions...</p>
            </div>
          ) : (
            <ul className="divide-y divide-gray-200">
              {commissions.map((commission) => (
                <li key={commission.id} className="px-4 py-4 sm:px-6">
                  <div className="flex items-center justify-between">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center space-x-3">
                        <div className="flex-shrink-0">
                          <div className="h-10 w-10 rounded-full bg-gray-300 flex items-center justify-center">
                            <span className="text-sm font-medium text-gray-700">
                              {commission.staffName?.charAt(0) || 'S'}
                            </span>
                          </div>
                        </div>
                        <div className="flex-1 min-w-0">
                          <p className="text-sm font-medium text-gray-900 truncate">
                            {commission.staffName}
                          </p>
                          <p className="text-sm text-gray-500">
                            {commission.serviceName} • {commission.branchName}
                          </p>
                          <p className="text-xs text-gray-400">
                            Appointment #{commission.appointmentId} • {formatDate(commission.calculatedOn)}
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center space-x-4">
                      <div className="text-right">
                        <p className="text-sm font-medium text-gray-900">
                          {formatCurrency(commission.amount)}
                        </p>
                        <p className="text-xs text-gray-500">
                          {commission.commissionType} • {commission.rate}%
                        </p>
                      </div>
                      <div className="flex items-center space-x-2">
                        {getStatusBadge(commission.status)}
                        {commission.status === 'PENDING' && (
                          <button
                            onClick={() => handleApproveCommission(commission.id)}
                            className="text-green-600 hover:text-green-900 text-sm font-medium"
                          >
                            Approve
                          </button>
                        )}
                        {commission.status === 'APPROVED' && (
                          <button
                            onClick={() => handleLockCommission(commission.id)}
                            className="text-blue-600 hover:text-blue-900 text-sm font-medium"
                          >
                            Lock
                          </button>
                        )}
                        {commission.status !== 'LOCKED' && commission.status !== 'REVERSED' && (
                          <>
                            <button
                              onClick={() => openAdjustmentModal(commission)}
                              className="text-blue-600 hover:text-blue-900 text-sm font-medium"
                            >
                              Adjust
                            </button>
                            <button
                              onClick={() => handleReverseCommission(commission.id)}
                              className="text-red-600 hover:text-red-900 text-sm font-medium"
                            >
                              Reverse
                            </button>
                          </>
                        )}
                      </div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}

      {activeTab === 'summaries' && (
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <div className="px-4 py-5 sm:px-6">
            <h3 className="text-lg leading-6 font-medium text-gray-900">Monthly Summaries</h3>
            <p className="mt-1 max-w-2xl text-sm text-gray-500">
              Monthly commission summaries for staff and branches
            </p>
          </div>
          {loading ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
              <p className="mt-2 text-sm text-gray-500">Loading summaries...</p>
            </div>
          ) : (
            <ul className="divide-y divide-gray-200">
              {summaries.map((summary) => (
                <li key={summary.id} className="px-4 py-4 sm:px-6">
                  <div className="flex items-center justify-between">
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center space-x-3">
                        <div className="flex-shrink-0">
                          <div className="h-10 w-10 rounded-full bg-gray-300 flex items-center justify-center">
                            <span className="text-sm font-medium text-gray-700">
                              {summary.staffName?.charAt(0) || 'S'}
                            </span>
                          </div>
                        </div>
                        <div className="flex-1 min-w-0">
                          <p className="text-sm font-medium text-gray-900 truncate">
                            {summary.staffName}
                          </p>
                          <p className="text-sm text-gray-500">
                            {summary.branchName} • {summary.month}
                          </p>
                          <p className="text-xs text-gray-400">
                            {summary.totalServices} services • Avg: {formatCurrency(summary.averageCommissionPerService)}
                          </p>
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center space-x-4">
                      <div className="text-right">
                        <p className="text-sm font-medium text-gray-900">
                          {formatCurrency(summary.totalCommission)}
                        </p>
                        <p className="text-xs text-gray-500">
                          {summary.totalServices} services
                        </p>
                      </div>
                      <div className="flex items-center space-x-2">
                        {getStatusBadge(summary.status)}
                        {summary.status === 'PENDING' && (
                          <button
                            onClick={() => handleApproveSummary(summary.id)}
                            className="text-green-600 hover:text-green-900 text-sm font-medium"
                          >
                            Approve
                          </button>
                        )}
                        {summary.status === 'APPROVED' && (
                          <button
                            onClick={() => handleLockSummary(summary.id)}
                            className="text-blue-600 hover:text-blue-900 text-sm font-medium"
                          >
                            Lock
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}

      {activeTab === 'reports' && (
        <CommissionReports />
      )}

      {/* Adjustment Modal */}
      <CommissionAdjustmentModal
        isOpen={adjustmentModal.isOpen}
        onClose={closeAdjustmentModal}
        commission={adjustmentModal.commission}
        onAdjust={handleAdjustCommission}
      />
    </div>
  );
};

export default CommissionManagement;


