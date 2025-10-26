import React, { useState, useEffect } from 'react';
import api from '../services/api';

const StaffManagement = () => {
  const [activeTab, setActiveTab] = useState('staff-list');
  const [staff, setStaff] = useState([]);
  const [attendance, setAttendance] = useState([]);
  const [commissionSummaries, setCommissionSummaries] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showAttendanceModal, setShowAttendanceModal] = useState(false);
  const [selectedStaff, setSelectedStaff] = useState(null);
  const [filters, setFilters] = useState({
    status: '',
    designation: '',
    skill: '',
    branch: ''
  });

  // Form states
  const [staffForm, setStaffForm] = useState({
    employeeCode: '',
    firstName: '',
    lastName: '',
    phone: '',
    email: '',
    gender: '',
    address: '',
    designation: '',
    skillSet: '',
    joinDate: '',
    salaryType: 'FIXED',
    baseSalary: '',
    commissionRate: '',
    bankDetails: '',
    status: 'ACTIVE',
    branchId: ''
  });

  const [attendanceForm, setAttendanceForm] = useState({
    workDate: new Date().toISOString().split('T')[0],
    status: 'PRESENT',
    notes: ''
  });

  useEffect(() => {
    fetchStaff();
    fetchAttendance();
    fetchCommissionSummaries();
  }, []);

  const fetchStaff = async () => {
    try {
      setLoading(true);
      const response = await api.get('/staff');
      setStaff(response.data);
    } catch (error) {
      console.error('Error fetching staff:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchAttendance = async () => {
    try {
      const response = await api.get('/staff/attendance/range', {
        params: {
          startDate: new Date().toISOString().split('T')[0],
          endDate: new Date().toISOString().split('T')[0]
        }
      });
      setAttendance(response.data);
    } catch (error) {
      console.error('Error fetching attendance:', error);
    }
  };

  const fetchCommissionSummaries = async () => {
    try {
      const currentMonth = new Date().toISOString().slice(0, 7);
      const response = await api.get(`/staff/commission-summary/month/${currentMonth}`);
      setCommissionSummaries(response.data);
    } catch (error) {
      console.error('Error fetching commission summaries:', error);
    }
  };

  const handleAddStaff = async (e) => {
    e.preventDefault();
    try {
      await api.post('/staff', staffForm);
      setShowAddModal(false);
      setStaffForm({
        employeeCode: '',
        firstName: '',
        lastName: '',
        phone: '',
        email: '',
        gender: '',
        address: '',
        designation: '',
        skillSet: '',
        joinDate: '',
        salaryType: 'FIXED',
        baseSalary: '',
        commissionRate: '',
        bankDetails: '',
        status: 'ACTIVE',
        branchId: ''
      });
      fetchStaff();
    } catch (error) {
      console.error('Error adding staff:', error);
    }
  };

  const handleEditStaff = async (e) => {
    e.preventDefault();
    try {
      await api.put(`/staff/${selectedStaff.id}`, staffForm);
      setShowEditModal(false);
      fetchStaff();
    } catch (error) {
      console.error('Error updating staff:', error);
    }
  };

  const handleCheckIn = async (staffId) => {
    try {
      await api.post(`/staff/${staffId}/check-in`);
      fetchAttendance();
    } catch (error) {
      console.error('Error checking in:', error);
    }
  };

  const handleCheckOut = async (staffId) => {
    try {
      await api.post(`/staff/${staffId}/check-out`);
      fetchAttendance();
    } catch (error) {
      console.error('Error checking out:', error);
    }
  };

  const handleMarkAbsent = async (staffId) => {
    try {
      await api.post(`/staff/${staffId}/mark-absent`, null, {
        params: { notes: 'Marked absent by admin' }
      });
      fetchAttendance();
    } catch (error) {
      console.error('Error marking absent:', error);
    }
  };

  const handleGenerateCommissionSummary = async (staffId) => {
    try {
      const currentMonth = new Date().toISOString().slice(0, 7);
      await api.post(`/staff/${staffId}/commission-summary`, null, {
        params: { month: currentMonth }
      });
      fetchCommissionSummaries();
    } catch (error) {
      console.error('Error generating commission summary:', error);
    }
  };

  const filteredStaff = staff.filter(staffMember => {
    if (filters.status && staffMember.status !== filters.status) return false;
    if (filters.designation && staffMember.designation !== filters.designation) return false;
    if (filters.skill && !staffMember.skillSet?.toLowerCase().includes(filters.skill.toLowerCase())) return false;
    if (filters.branch && staffMember.branchId !== parseInt(filters.branch)) return false;
    return true;
  });

  const getStatusColor = (status) => {
    switch (status) {
      case 'ACTIVE': return 'bg-green-100 text-green-800';
      case 'INACTIVE': return 'bg-red-100 text-red-800';
      case 'ON_LEAVE': return 'bg-yellow-100 text-yellow-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const getAttendanceStatusColor = (status) => {
    switch (status) {
      case 'PRESENT': return 'bg-green-100 text-green-800';
      case 'ABSENT': return 'bg-red-100 text-red-800';
      case 'LEAVE': return 'bg-blue-100 text-blue-800';
      case 'HALF_DAY': return 'bg-yellow-100 text-yellow-800';
      case 'LATE': return 'bg-orange-100 text-orange-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Staff Management</h1>
        <p className="mt-1 text-sm text-gray-500">
          Manage your salon staff, attendance, and commission tracking.
        </p>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="-mb-px flex space-x-8">
          {[
            { id: 'staff-list', name: 'Staff List', icon: '👥' },
            { id: 'attendance', name: 'Attendance', icon: '⏰' },
            { id: 'commission', name: 'Commission', icon: '💰' },
            { id: 'reports', name: 'Reports', icon: '📊' }
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === tab.id
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              <span className="mr-2">{tab.icon}</span>
              {tab.name}
            </button>
          ))}
        </nav>
      </div>

      {/* Staff List Tab */}
      {activeTab === 'staff-list' && (
        <div className="space-y-6">
          {/* Filters and Actions */}
          <div className="bg-white p-6 rounded-lg shadow">
            <div className="flex flex-wrap gap-4 mb-4">
              <select
                value={filters.status}
                onChange={(e) => setFilters({...filters, status: e.target.value})}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Status</option>
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
                <option value="ON_LEAVE">On Leave</option>
              </select>
              
              <select
                value={filters.designation}
                onChange={(e) => setFilters({...filters, designation: e.target.value})}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">All Designations</option>
                <option value="Senior Stylist">Senior Stylist</option>
                <option value="Beautician">Beautician</option>
                <option value="Receptionist">Receptionist</option>
                <option value="Manager">Manager</option>
              </select>
              
              <input
                type="text"
                placeholder="Search by skill..."
                value={filters.skill}
                onChange={(e) => setFilters({...filters, skill: e.target.value})}
                className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            
            <button
              onClick={() => setShowAddModal(true)}
              className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
            >
              Add New Staff
            </button>
          </div>

          {/* Staff Table */}
          <div className="bg-white shadow overflow-hidden sm:rounded-md">
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Staff
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Designation
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Salary Type
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Performance
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filteredStaff.map((staffMember) => (
                    <tr key={staffMember.id}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          <div className="flex-shrink-0 h-10 w-10">
                            <div className="h-10 w-10 rounded-full bg-gray-300 flex items-center justify-center">
                              <span className="text-sm font-medium text-gray-700">
                                {staffMember.firstName?.charAt(0)}{staffMember.lastName?.charAt(0)}
                              </span>
                            </div>
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900">
                              {staffMember.firstName} {staffMember.lastName}
                            </div>
                            <div className="text-sm text-gray-500">
                              {staffMember.employeeCode}
                            </div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {staffMember.designation}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                          {staffMember.salaryType}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(staffMember.status)}`}>
                          {staffMember.status}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        <div className="space-y-1">
                          <div>Services: {staffMember.totalServices || 0}</div>
                          <div>Present: {staffMember.presentDays || 0} days</div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                        <button
                          onClick={() => {
                            setSelectedStaff(staffMember);
                            setStaffForm(staffMember);
                            setShowEditModal(true);
                          }}
                          className="text-blue-600 hover:text-blue-900"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleCheckIn(staffMember.id)}
                          className="text-green-600 hover:text-green-900"
                        >
                          Check In
                        </button>
                        <button
                          onClick={() => handleCheckOut(staffMember.id)}
                          className="text-orange-600 hover:text-orange-900"
                        >
                          Check Out
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {/* Attendance Tab */}
      {activeTab === 'attendance' && (
        <div className="space-y-6">
          <div className="bg-white p-6 rounded-lg shadow">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Today's Attendance</h3>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Staff
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Check In
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Check Out
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Hours
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {attendance.map((record) => (
                    <tr key={record.id}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {record.staffName}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {record.checkIn || 'Not checked in'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {record.checkOut || 'Not checked out'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {record.totalHours ? `${record.totalHours}h` : '0h'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getAttendanceStatusColor(record.status)}`}>
                          {record.status}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        {!record.checkIn && (
                          <button
                            onClick={() => handleCheckIn(record.staffId)}
                            className="text-green-600 hover:text-green-900 mr-2"
                          >
                            Check In
                          </button>
                        )}
                        {record.checkIn && !record.checkOut && (
                          <button
                            onClick={() => handleCheckOut(record.staffId)}
                            className="text-orange-600 hover:text-orange-900 mr-2"
                          >
                            Check Out
                          </button>
                        )}
                        {!record.checkIn && (
                          <button
                            onClick={() => handleMarkAbsent(record.staffId)}
                            className="text-red-600 hover:text-red-900"
                          >
                            Mark Absent
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {/* Commission Tab */}
      {activeTab === 'commission' && (
        <div className="space-y-6">
          <div className="bg-white p-6 rounded-lg shadow">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Commission Summary - {new Date().toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}</h3>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Staff
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Services
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Base Salary
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Commission
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Total Payout
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {commissionSummaries.map((summary) => (
                    <tr key={summary.id}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {summary.staffName}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {summary.totalServices}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        LKR {summary.baseSalary?.toLocaleString() || '0'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        LKR {summary.totalCommission?.toLocaleString() || '0'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        LKR {summary.totalPayout?.toLocaleString() || '0'}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button
                          onClick={() => handleGenerateCommissionSummary(summary.staffId)}
                          className="text-blue-600 hover:text-blue-900"
                        >
                          Generate
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {/* Reports Tab */}
      {activeTab === 'reports' && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-medium text-gray-900 mb-2">Total Staff</h3>
              <p className="text-3xl font-bold text-blue-600">{staff.length}</p>
            </div>
            <div className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-medium text-gray-900 mb-2">Active Staff</h3>
              <p className="text-3xl font-bold text-green-600">
                {staff.filter(s => s.status === 'ACTIVE').length}
              </p>
            </div>
            <div className="bg-white p-6 rounded-lg shadow">
              <h3 className="text-lg font-medium text-gray-900 mb-2">Present Today</h3>
              <p className="text-3xl font-bold text-orange-600">
                {attendance.filter(a => a.status === 'PRESENT' || a.status === 'LATE').length}
              </p>
            </div>
          </div>
        </div>
      )}

      {/* Add Staff Modal */}
      {showAddModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Add New Staff</h3>
              <form onSubmit={handleAddStaff} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="text"
                    placeholder="Employee Code"
                    value={staffForm.employeeCode}
                    onChange={(e) => setStaffForm({...staffForm, employeeCode: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                  <input
                    type="text"
                    placeholder="First Name"
                    value={staffForm.firstName}
                    onChange={(e) => setStaffForm({...staffForm, firstName: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="text"
                    placeholder="Last Name"
                    value={staffForm.lastName}
                    onChange={(e) => setStaffForm({...staffForm, lastName: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                  <input
                    type="email"
                    placeholder="Email"
                    value={staffForm.email}
                    onChange={(e) => setStaffForm({...staffForm, email: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="tel"
                    placeholder="Phone"
                    value={staffForm.phone}
                    onChange={(e) => setStaffForm({...staffForm, phone: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <select
                    value={staffForm.gender}
                    onChange={(e) => setStaffForm({...staffForm, gender: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Select Gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="text"
                    placeholder="Designation"
                    value={staffForm.designation}
                    onChange={(e) => setStaffForm({...staffForm, designation: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <select
                    value={staffForm.salaryType}
                    onChange={(e) => setStaffForm({...staffForm, salaryType: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="FIXED">Fixed</option>
                    <option value="COMMISSION">Commission</option>
                    <option value="HYBRID">Hybrid</option>
                  </select>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <input
                    type="number"
                    placeholder="Base Salary"
                    value={staffForm.baseSalary}
                    onChange={(e) => setStaffForm({...staffForm, baseSalary: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <input
                    type="number"
                    placeholder="Commission Rate %"
                    value={staffForm.commissionRate}
                    onChange={(e) => setStaffForm({...staffForm, commissionRate: e.target.value})}
                    className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <input
                  type="text"
                  placeholder="Skills (comma separated)"
                  value={staffForm.skillSet}
                  onChange={(e) => setStaffForm({...staffForm, skillSet: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <textarea
                  placeholder="Address"
                  value={staffForm.address}
                  onChange={(e) => setStaffForm({...staffForm, address: e.target.value})}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  rows="3"
                />
                <div className="flex justify-end space-x-2">
                  <button
                    type="button"
                    onClick={() => setShowAddModal(false)}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700"
                  >
                    Add Staff
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}

      {/* Edit Staff Modal */}
      {showEditModal && selectedStaff && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Edit Staff</h3>
              <form onSubmit={handleEditStaff} className="space-y-4">
                {/* Similar form fields as Add Modal */}
                <div className="flex justify-end space-x-2">
                  <button
                    type="button"
                    onClick={() => setShowEditModal(false)}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700"
                  >
                    Update Staff
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default StaffManagement;


