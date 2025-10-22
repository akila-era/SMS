import React, { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { appointmentAPI } from '../services/api';
import { CalendarIcon, PlusIcon, EyeIcon, PencilIcon, TrashIcon, ClockIcon } from '@heroicons/react/24/outline';
import toast from 'react-hot-toast';

const AppointmentManagement = () => {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const queryClient = useQueryClient();

  // Fetch appointments for selected date
  const { data: appointments, isLoading, error } = useQuery(
    ['appointments', selectedDate],
    () => appointmentAPI.getByDate(selectedDate),
    {
      enabled: !!selectedDate
    }
  );

  // Fetch today's appointments
  const { data: todaysAppointments } = useQuery(
    'todaysAppointments',
    () => appointmentAPI.getAll(),
    {
      select: (data) => data.data.filter(apt => 
        new Date(apt.appointmentDate).toDateString() === new Date().toDateString()
      )
    }
  );

  // Update appointment status mutation
  const updateStatusMutation = useMutation(
    ({ id, status }) => appointmentAPI.updateStatus(id, status),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['appointments', selectedDate]);
        queryClient.invalidateQueries('todaysAppointments');
        toast.success('Appointment status updated successfully');
      },
      onError: (error) => {
        toast.error(error.response?.data?.message || 'Failed to update status');
      }
    }
  );

  // Cancel appointment mutation
  const cancelMutation = useMutation(
    ({ id, reason }) => appointmentAPI.cancel(id, reason),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['appointments', selectedDate]);
        queryClient.invalidateQueries('todaysAppointments');
        toast.success('Appointment cancelled successfully');
      },
      onError: (error) => {
        toast.error(error.response?.data?.message || 'Failed to cancel appointment');
      }
    }
  );

  const handleStatusUpdate = (id, status) => {
    updateStatusMutation.mutate({ id, status });
  };

  const handleCancel = (id) => {
    const reason = prompt('Please enter cancellation reason (optional):');
    cancelMutation.mutate({ id, reason });
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'BOOKED': return 'bg-blue-100 text-blue-800';
      case 'IN_PROGRESS': return 'bg-yellow-100 text-yellow-800';
      case 'COMPLETED': return 'bg-green-100 text-green-800';
      case 'CANCELLED': return 'bg-red-100 text-red-800';
      case 'NO_SHOW': return 'bg-gray-100 text-gray-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const formatTime = (time) => {
    return new Date(`2000-01-01T${time}`).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div>
      <div className="mb-8">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Appointment Management</h1>
            <p className="mt-1 text-sm text-gray-500">
              Manage your salon appointments and scheduling.
            </p>
          </div>
          <button
            onClick={() => setShowCreateModal(true)}
            className="btn-primary flex items-center"
          >
            <PlusIcon className="h-5 w-5 mr-2" />
            New Appointment
          </button>
        </div>
      </div>

      {/* Date Selector */}
      <div className="mb-6">
        <div className="flex items-center space-x-4">
          <label className="text-sm font-medium text-gray-700">Select Date:</label>
          <input
            type="date"
            value={selectedDate}
            onChange={(e) => setSelectedDate(e.target.value)}
            className="input-field w-auto"
          />
          <div className="flex items-center text-sm text-gray-500">
            <CalendarIcon className="h-4 w-4 mr-1" />
            {new Date(selectedDate).toLocaleDateString('en-US', { 
              weekday: 'long', 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            })}
          </div>
        </div>
      </div>

      {/* Today's Summary */}
      {todaysAppointments && todaysAppointments.length > 0 && (
        <div className="mb-6 bg-blue-50 border border-blue-200 rounded-lg p-4">
          <h3 className="text-lg font-medium text-blue-900 mb-2">Today's Appointments</h3>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="text-center">
              <div className="text-2xl font-bold text-blue-600">
                {todaysAppointments.filter(apt => apt.status === 'BOOKED').length}
              </div>
              <div className="text-sm text-blue-700">Booked</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-yellow-600">
                {todaysAppointments.filter(apt => apt.status === 'IN_PROGRESS').length}
              </div>
              <div className="text-sm text-yellow-700">In Progress</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-green-600">
                {todaysAppointments.filter(apt => apt.status === 'COMPLETED').length}
              </div>
              <div className="text-sm text-green-700">Completed</div>
            </div>
          </div>
        </div>
      )}

      {/* Appointments List */}
      <div className="bg-white shadow rounded-lg">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">
            Appointments for {new Date(selectedDate).toLocaleDateString()}
          </h3>
        </div>
        
        {appointments && appointments.length > 0 ? (
          <div className="divide-y divide-gray-200">
            {appointments.map((appointment) => (
              <div key={appointment.id} className="p-6 hover:bg-gray-50">
                <div className="flex items-center justify-between">
                  <div className="flex-1">
                    <div className="flex items-center space-x-4">
                      <div className="flex-shrink-0">
                        <ClockIcon className="h-5 w-5 text-gray-400" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center space-x-2">
                          <h4 className="text-lg font-medium text-gray-900">
                            {appointment.customerName}
                          </h4>
                          <span className={`status-badge ${getStatusColor(appointment.status)}`}>
                            {appointment.status.replace('_', ' ')}
                          </span>
                        </div>
                        <div className="mt-1 text-sm text-gray-500">
                          <div className="flex items-center space-x-4">
                            <span>📞 {appointment.customerPhone}</span>
                            <span>👤 {appointment.staffName}</span>
                            <span>🏢 {appointment.branchName}</span>
                          </div>
                          <div className="mt-1">
                            <span className="font-medium">
                              {formatTime(appointment.startTime)} - {formatTime(appointment.endTime)}
                            </span>
                            <span className="ml-4 text-green-600 font-medium">
                              LKR {appointment.totalAmount.toFixed(2)}
                            </span>
                          </div>
                          {appointment.services && appointment.services.length > 0 && (
                            <div className="mt-1">
                              <span className="text-xs text-gray-400">Services: </span>
                              <span className="text-xs text-gray-600">
                                {appointment.services.map(s => s.serviceName).join(', ')}
                              </span>
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <div className="flex items-center space-x-2">
                    {appointment.status === 'BOOKED' && (
                      <>
                        <button
                          onClick={() => handleStatusUpdate(appointment.id, 'IN_PROGRESS')}
                          className="btn-success text-xs px-3 py-1"
                        >
                          Start
                        </button>
                        <button
                          onClick={() => handleCancel(appointment.id)}
                          className="btn-danger text-xs px-3 py-1"
                        >
                          Cancel
                        </button>
                      </>
                    )}
                    {appointment.status === 'IN_PROGRESS' && (
                      <button
                        onClick={() => handleStatusUpdate(appointment.id, 'COMPLETED')}
                        className="btn-success text-xs px-3 py-1"
                      >
                        Complete
                      </button>
                    )}
                    <button
                      onClick={() => {
                        setSelectedAppointment(appointment);
                        setShowEditModal(true);
                      }}
                      className="btn-outline text-xs px-3 py-1"
                    >
                      <EyeIcon className="h-4 w-4" />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="p-6 text-center text-gray-500">
            <CalendarIcon className="h-12 w-12 mx-auto mb-4 text-gray-300" />
            <p>No appointments scheduled for this date.</p>
          </div>
        )}
      </div>

      {/* Create Appointment Modal */}
      {showCreateModal && (
        <CreateAppointmentModal
          isOpen={showCreateModal}
          onClose={() => setShowCreateModal(false)}
          selectedDate={selectedDate}
        />
      )}

      {/* Edit Appointment Modal */}
      {showEditModal && selectedAppointment && (
        <EditAppointmentModal
          isOpen={showEditModal}
          onClose={() => {
            setShowEditModal(false);
            setSelectedAppointment(null);
          }}
          appointment={selectedAppointment}
        />
      )}
    </div>
  );
};

// Create Appointment Modal Component
const CreateAppointmentModal = ({ isOpen, onClose, selectedDate }) => {
  const [formData, setFormData] = useState({
    customerId: '',
    staffId: '',
    branchId: '',
    appointmentDate: selectedDate,
    startTime: '',
    endTime: '',
    notes: '',
    services: []
  });

  const queryClient = useQueryClient();

  // Fetch customers, staff, and services
  const { data: customers } = useQuery('customers', () => appointmentAPI.getAll());
  const { data: staff } = useQuery('staff', () => appointmentAPI.getAll());
  const { data: services } = useQuery('services', () => appointmentAPI.getAll());

  const createMutation = useMutation(
    (data) => appointmentAPI.create(data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['appointments', selectedDate]);
        toast.success('Appointment created successfully');
        onClose();
      },
      onError: (error) => {
        toast.error(error.response?.data?.message || 'Failed to create appointment');
      }
    }
  );

  const handleSubmit = (e) => {
    e.preventDefault();
    createMutation.mutate(formData);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="mt-3">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Create New Appointment</h3>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Customer</label>
              <select
                value={formData.customerId}
                onChange={(e) => setFormData({...formData, customerId: e.target.value})}
                className="input-field"
                required
              >
                <option value="">Select Customer</option>
                {/* Add customer options */}
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700">Staff</label>
              <select
                value={formData.staffId}
                onChange={(e) => setFormData({...formData, staffId: e.target.value})}
                className="input-field"
                required
              >
                <option value="">Select Staff</option>
                {/* Add staff options */}
              </select>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Start Time</label>
                <input
                  type="time"
                  value={formData.startTime}
                  onChange={(e) => setFormData({...formData, startTime: e.target.value})}
                  className="input-field"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">End Time</label>
                <input
                  type="time"
                  value={formData.endTime}
                  onChange={(e) => setFormData({...formData, endTime: e.target.value})}
                  className="input-field"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Notes</label>
              <textarea
                value={formData.notes}
                onChange={(e) => setFormData({...formData, notes: e.target.value})}
                className="input-field"
                rows={3}
              />
            </div>

            <div className="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                onClick={onClose}
                className="btn-outline"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="btn-primary"
                disabled={createMutation.isLoading}
              >
                {createMutation.isLoading ? 'Creating...' : 'Create Appointment'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

// Edit Appointment Modal Component
const EditAppointmentModal = ({ isOpen, onClose, appointment }) => {
  if (!isOpen || !appointment) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="mt-3">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Appointment Details</h3>
          <div className="space-y-3">
            <div>
              <label className="block text-sm font-medium text-gray-700">Customer</label>
              <p className="text-sm text-gray-900">{appointment.customerName}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Staff</label>
              <p className="text-sm text-gray-900">{appointment.staffName}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Time</label>
              <p className="text-sm text-gray-900">
                {new Date(`2000-01-01T${appointment.startTime}`).toLocaleTimeString('en-US', {
                  hour: '2-digit',
                  minute: '2-digit',
                  hour12: true
                })} - {new Date(`2000-01-01T${appointment.endTime}`).toLocaleTimeString('en-US', {
                  hour: '2-digit',
                  minute: '2-digit',
                  hour12: true
                })}
              </p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Status</label>
              <p className="text-sm text-gray-900">{appointment.status}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700">Total Amount</label>
              <p className="text-sm text-gray-900">LKR {appointment.totalAmount.toFixed(2)}</p>
            </div>
            {appointment.notes && (
              <div>
                <label className="block text-sm font-medium text-gray-700">Notes</label>
                <p className="text-sm text-gray-900">{appointment.notes}</p>
              </div>
            )}
          </div>
          
          <div className="flex justify-end pt-4">
            <button
              onClick={onClose}
              className="btn-outline"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AppointmentManagement;
