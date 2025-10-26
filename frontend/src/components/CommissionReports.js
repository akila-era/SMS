import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

const CommissionReports = () => {
  const [reportType, setReportType] = useState('dashboard');
  const [month, setMonth] = useState(new Date().toISOString().slice(0, 7));
  const [year, setYear] = useState(new Date().getFullYear());
  const [quarter, setQuarter] = useState(1);
  const [loading, setLoading] = useState(false);
  const [reportData, setReportData] = useState(null);

  useEffect(() => {
    if (reportType) {
      loadReport();
    }
  }, [reportType, month, year, quarter]);

  const loadReport = async () => {
    setLoading(true);
    try {
      let response;
      switch (reportType) {
        case 'dashboard':
          response = await api.get('/commissions/reports/dashboard');
          break;
        case 'branch-wise':
          response = await api.get(`/commissions/reports/branch-wise?month=${month}`);
          break;
        case 'staff-wise':
          response = await api.get(`/commissions/reports/staff-wise?month=${month}`);
          break;
        case 'monthly-trend':
          response = await api.get(`/commissions/reports/monthly-trend?year=${year}`);
          break;
        case 'quarterly':
          response = await api.get(`/commissions/reports/quarterly?year=${year}&quarter=${quarter}`);
          break;
        case 'year-end':
          response = await api.get(`/commissions/reports/year-end?year=${year}`);
          break;
        default:
          return;
      }
      setReportData(response.data);
    } catch (error) {
      console.error('Error loading report:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR'
    }).format(amount);
  };

  const renderDashboardReport = () => {
    if (!reportData?.dashboardData) return null;

    const { dashboardData } = reportData;
    
    return (
      <div className="space-y-6">
        {/* Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <div className="w-8 h-8 bg-green-500 rounded-md flex items-center justify-center">
                    <span className="text-white text-sm font-medium">₵</span>
                  </div>
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">Total This Month</dt>
                    <dd className="text-lg font-medium text-gray-900">
                      {formatCurrency(dashboardData.totalCommissionsThisMonth)}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <div className="w-8 h-8 bg-yellow-500 rounded-md flex items-center justify-center">
                    <span className="text-white text-sm font-medium">⏳</span>
                  </div>
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">Pending Approvals</dt>
                    <dd className="text-lg font-medium text-gray-900">
                      {dashboardData.totalPendingApprovals}
                    </dd>
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
                    <span className="text-white text-sm font-medium">💰</span>
                  </div>
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">Pending Payout</dt>
                    <dd className="text-lg font-medium text-gray-900">
                      {formatCurrency(dashboardData.totalPayoutPendingApproval)}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Top Earning Staff */}
        {dashboardData.topEarningStaff && (
          <div className="bg-white shadow overflow-hidden sm:rounded-md">
            <div className="px-4 py-5 sm:px-6">
              <h3 className="text-lg leading-6 font-medium text-gray-900">Top Earning Staff</h3>
              <p className="mt-1 max-w-2xl text-sm text-gray-500">
                Staff members with highest commission earnings
              </p>
            </div>
            <ul className="divide-y divide-gray-200">
              {dashboardData.topEarningStaff.map((staff, index) => (
                <li key={staff.staffId} className="px-4 py-4 sm:px-6">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <div className="flex-shrink-0">
                        <div className="h-10 w-10 rounded-full bg-indigo-500 flex items-center justify-center">
                          <span className="text-white text-sm font-medium">{index + 1}</span>
                        </div>
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">{staff.staffName}</div>
                        <div className="text-sm text-gray-500">{staff.branchName}</div>
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="text-sm font-medium text-gray-900">
                        {formatCurrency(staff.totalEarnings)}
                      </div>
                      <div className="text-sm text-gray-500">{staff.totalServices} services</div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        )}

        {/* Branch Comparisons */}
        {dashboardData.branchComparisons && (
          <div className="bg-white shadow overflow-hidden sm:rounded-md">
            <div className="px-4 py-5 sm:px-6">
              <h3 className="text-lg leading-6 font-medium text-gray-900">Branch Performance</h3>
              <p className="mt-1 max-w-2xl text-sm text-gray-500">
                Commission performance by branch
              </p>
            </div>
            <ul className="divide-y divide-gray-200">
              {dashboardData.branchComparisons.map((branch) => (
                <li key={branch.branchId} className="px-4 py-4 sm:px-6">
                  <div className="flex items-center justify-between">
                    <div className="text-sm font-medium text-gray-900">{branch.branchName}</div>
                    <div className="text-right">
                      <div className="text-sm font-medium text-gray-900">
                        {formatCurrency(branch.totalCommission)}
                      </div>
                      <div className="text-sm text-gray-500">{branch.totalServices} services</div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
    );
  };

  const renderBranchWiseReport = () => {
    if (!reportData?.branchSummaries) return null;

    return (
      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        <div className="px-4 py-5 sm:px-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">Branch-wise Commission Report</h3>
          <p className="mt-1 max-w-2xl text-sm text-gray-500">
            Commission performance by branch for {month}
          </p>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Branch
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Month
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Services
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Commission
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Avg per Service
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {reportData.branchSummaries.map((branch) => (
                <tr key={branch.branchId}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {branch.branchName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {branch.month}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {branch.totalServices}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatCurrency(branch.totalCommission)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatCurrency(branch.averageCommissionPerService)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  const renderStaffWiseReport = () => {
    if (!reportData?.staffSummaries) return null;

    return (
      <div className="bg-white shadow overflow-hidden sm:rounded-md">
        <div className="px-4 py-5 sm:px-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">Staff-wise Commission Report</h3>
          <p className="mt-1 max-w-2xl text-sm text-gray-500">
            Commission performance by staff for {month}
          </p>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Staff
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Branch
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Month
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Services
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Total Commission
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Avg per Service
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {reportData.staffSummaries.map((staff) => (
                <tr key={staff.staffId}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {staff.staffName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {staff.branchName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {staff.month}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {staff.totalServices}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatCurrency(staff.totalCommission)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {formatCurrency(staff.averageCommissionPerService)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    );
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">Commission Reports</h1>
        <p className="mt-1 text-sm text-gray-500">
          Generate and view detailed commission reports and analytics.
        </p>
      </div>

      {/* Report Type Selection */}
      <div className="bg-white shadow rounded-lg p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Report Type
            </label>
            <select
              value={reportType}
              onChange={(e) => setReportType(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            >
              <option value="dashboard">Dashboard Overview</option>
              <option value="branch-wise">Branch-wise Report</option>
              <option value="staff-wise">Staff-wise Report</option>
              <option value="monthly-trend">Monthly Trend</option>
              <option value="quarterly">Quarterly Report</option>
              <option value="year-end">Year-end Report</option>
            </select>
          </div>

          {reportType === 'branch-wise' || reportType === 'staff-wise' ? (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Month
              </label>
              <input
                type="month"
                value={month}
                onChange={(e) => setMonth(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              />
            </div>
          ) : reportType === 'monthly-trend' || reportType === 'year-end' ? (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Year
              </label>
              <input
                type="number"
                value={year}
                onChange={(e) => setYear(parseInt(e.target.value))}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              />
            </div>
          ) : reportType === 'quarterly' ? (
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Quarter
              </label>
              <select
                value={quarter}
                onChange={(e) => setQuarter(parseInt(e.target.value))}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option value={1}>Q1 (Jan-Mar)</option>
                <option value={2}>Q2 (Apr-Jun)</option>
                <option value={3}>Q3 (Jul-Sep)</option>
                <option value={4}>Q4 (Oct-Dec)</option>
              </select>
            </div>
          ) : null}
        </div>
      </div>

      {/* Report Content */}
      {loading ? (
        <div className="text-center py-12">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
          <p className="mt-2 text-sm text-gray-500">Loading report...</p>
        </div>
      ) : (
        <>
          {reportType === 'dashboard' && renderDashboardReport()}
          {reportType === 'branch-wise' && renderBranchWiseReport()}
          {reportType === 'staff-wise' && renderStaffWiseReport()}
          {reportType === 'monthly-trend' && (
            <div className="bg-white shadow rounded-lg p-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Monthly Trend Report</h3>
              <p className="text-gray-500">Monthly trend data for {year}</p>
              {/* Chart implementation would go here */}
            </div>
          )}
          {reportType === 'quarterly' && (
            <div className="bg-white shadow rounded-lg p-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Quarterly Report</h3>
              <p className="text-gray-500">Quarterly data for {year} Q{quarter}</p>
              {/* Chart implementation would go here */}
            </div>
          )}
          {reportType === 'year-end' && (
            <div className="bg-white shadow rounded-lg p-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Year-end Report</h3>
              <p className="text-gray-500">Year-end summary for {year}</p>
              {/* Chart implementation would go here */}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default CommissionReports;
