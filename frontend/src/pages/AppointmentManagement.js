import React, { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { appointmentAPI } from '../services/api';
import { 
  CalendarIcon, 
  PlusIcon, 
  EyeIcon, 
  PencilIcon, 
  TrashIcon, 
  ClockIcon,
  UserGroupIcon,
  ChartBarIcon,
  MagnifyingGlassIcon,
  CheckCircleIcon,
  XCircleIcon,
  ArrowPathIcon,
  CalendarDaysIcon,
  UserIcon,
  BuildingOfficeIcon,
  CurrencyDollarIcon,
  ChatBubbleLeftRightIcon,
  HeartIcon,
  SparklesIcon
} from '@heroicons/react/24/outline';
import { 
  CheckCircleIcon as CheckCircleSolidIcon,
  XCircleIcon as XCircleSolidIcon,
  ExclamationTriangleIcon as ExclamationTriangleSolidIcon,
  ClockIcon as ClockSolidIconFilled
} from '@heroicons/react/24/solid';
import toast from 'react-hot-toast';

const AppointmentManagement = () => {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showTemplateModal, setShowTemplateModal] = useState(false);
  const [showWaitlistModal, setShowWaitlistModal] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [viewMode, setViewMode] = useState('calendar'); // 'calendar', 'list', 'timeline'
  const [filterStatus, setFilterStatus] = useState('all');
  const [searchTerm, setSearchTerm] = useState('');
  const queryClient = useQueryClient();

  // Fetch appointments for selected date
  const { data: appointments, isLoading } = useQuery(
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
      select: (data) => {
        const appointments = Array.isArray(data?.data) ? data.data : [];
        return appointments.filter(apt => 
          new Date(apt.appointmentDate).toDateString() === new Date().toDateString()
        );
      }
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


  // Filter appointments based on search and filters
  const filteredAppointments = (Array.isArray(appointments) ? appointments : []).filter(appointment => {
    const matchesSearch = searchTerm === '' || 
      appointment.customerName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      appointment.staffName.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesStatus = filterStatus === 'all' || appointment.status === filterStatus;
    const matchesStaff = true; // Staff filtering removed for now
    
    return matchesSearch && matchesStatus && matchesStaff;
  });

  if (isLoading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-4 border-blue-200 border-t-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600 font-medium">Loading appointments...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50">
      {/* Header Section */}
      <div className="bg-white shadow-sm border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center">
                <CalendarDaysIcon className="h-8 w-8 text-blue-600 mr-3" />
                Appointment Management
              </h1>
              <p className="mt-2 text-gray-600">
                Manage your salon appointments with advanced scheduling tools
              </p>
            </div>
            <div className="flex items-center space-x-3">
              <button
                onClick={() => setShowTemplateModal(true)}
                className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200"
              >
                <SparklesIcon className="h-4 w-4 mr-2" />
                Templates
              </button>
              <button
                onClick={() => setShowWaitlistModal(true)}
                className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200"
              >
                <UserGroupIcon className="h-4 w-4 mr-2" />
                Waitlist
              </button>
              <button
                onClick={() => setShowCreateModal(true)}
                className="inline-flex items-center px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 text-white font-medium rounded-lg hover:from-blue-700 hover:to-blue-800 transition-all duration-200 shadow-lg hover:shadow-xl"
              >
                <PlusIcon className="h-5 w-5 mr-2" />
                New Appointment
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Statistics Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <StatCard
            title="Today's Appointments"
            value={todaysAppointments?.length || 0}
            icon={CalendarDaysIcon}
            color="blue"
            trend="+12%"
          />
          <StatCard
            title="Completed"
            value={todaysAppointments?.filter(apt => apt.status === 'COMPLETED').length || 0}
            icon={CheckCircleSolidIcon}
            color="green"
            trend="+8%"
          />
          <StatCard
            title="In Progress"
            value={todaysAppointments?.filter(apt => apt.status === 'IN_PROGRESS').length || 0}
            icon={ClockSolidIconFilled}
            color="yellow"
            trend="+5%"
          />
          <StatCard
            title="Revenue"
            value={`LKR ${todaysAppointments?.reduce((sum, apt) => sum + parseFloat(apt.totalAmount), 0).toFixed(0) || 0}`}
            icon={CurrencyDollarIcon}
            color="purple"
            trend="+15%"
          />
        </div>

        {/* Controls Section */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-8">
          <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between space-y-4 lg:space-y-0">
            {/* View Mode Toggle */}
            <div className="flex items-center space-x-2">
              <span className="text-sm font-medium text-gray-700">View:</span>
              <div className="flex bg-gray-100 rounded-lg p-1">
                {[
                  { key: 'calendar', label: 'Calendar', icon: CalendarIcon },
                  { key: 'list', label: 'List', icon: ClockIcon },
                  { key: 'timeline', label: 'Timeline', icon: ChartBarIcon }
                ].map(({ key, label, icon: Icon }) => (
                  <button
                    key={key}
                    onClick={() => setViewMode(key)}
                    className={`flex items-center px-3 py-2 rounded-md text-sm font-medium transition-colors duration-200 ${
                      viewMode === key
                        ? 'bg-white text-blue-600 shadow-sm'
                        : 'text-gray-600 hover:text-gray-900'
                    }`}
                  >
                    <Icon className="h-4 w-4 mr-2" />
                    {label}
                  </button>
                ))}
              </div>
            </div>

            {/* Search and Filters */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center space-y-3 sm:space-y-0 sm:space-x-4">
              {/* Search */}
              <div className="relative">
                <MagnifyingGlassIcon className="h-5 w-5 absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  placeholder="Search appointments..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent w-64"
                />
              </div>

              {/* Status Filter */}
              <select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="all">All Status</option>
                <option value="BOOKED">Booked</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="COMPLETED">Completed</option>
                <option value="CANCELLED">Cancelled</option>
                <option value="NO_SHOW">No Show</option>
              </select>

              {/* Date Picker */}
              <div className="flex items-center space-x-2">
                <CalendarIcon className="h-5 w-5 text-gray-400" />
                <input
                  type="date"
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                  className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>
          </div>
        </div>

        {/* Appointments Display */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200 bg-gradient-to-r from-gray-50 to-blue-50">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-semibold text-gray-900 flex items-center">
                <ClockIcon className="h-5 w-5 mr-2 text-blue-600" />
                Appointments for {new Date(selectedDate).toLocaleDateString('en-US', { 
                  weekday: 'long', 
                  year: 'numeric', 
                  month: 'long', 
                  day: 'numeric' 
                })}
              </h3>
              <div className="flex items-center space-x-2 text-sm text-gray-500">
                <span>{filteredAppointments.length} appointments</span>
              </div>
            </div>
          </div>
          
          {filteredAppointments.length > 0 ? (
            <div className="divide-y divide-gray-200">
              {filteredAppointments.map((appointment) => (
                <AppointmentCard
                  key={appointment.id}
                  appointment={appointment}
                  onStatusUpdate={handleStatusUpdate}
                  onCancel={handleCancel}
                  onView={() => {
                    setSelectedAppointment(appointment);
                    setShowEditModal(true);
                  }}
                />
              ))}
            </div>
          ) : (
            <EmptyState
              title="No appointments found"
              description="No appointments match your current filters"
              icon={CalendarIcon}
            />
          )}
        </div>
      </div>

      {/* Modals */}
      {showCreateModal && (
        <CreateAppointmentModal
          isOpen={showCreateModal}
          onClose={() => setShowCreateModal(false)}
          selectedDate={selectedDate}
        />
      )}

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

      {showTemplateModal && (
        <TemplateModal
          isOpen={showTemplateModal}
          onClose={() => setShowTemplateModal(false)}
        />
      )}

      {showWaitlistModal && (
        <WaitlistModal
          isOpen={showWaitlistModal}
          onClose={() => setShowWaitlistModal(false)}
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
    services: [],
    isRecurring: false,
    recurrencePattern: 'NONE',
    recurrenceInterval: 1,
    recurrenceEndDate: ''
  });

  const [selectedServices, setSelectedServices] = useState([]);
  const [step, setStep] = useState(1);

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
        setStep(1);
        setFormData({
          customerId: '',
          staffId: '',
          branchId: '',
          appointmentDate: selectedDate,
          startTime: '',
          endTime: '',
          notes: '',
          services: [],
          isRecurring: false,
          recurrencePattern: 'NONE',
          recurrenceInterval: 1,
          recurrenceEndDate: ''
        });
        setSelectedServices([]);
      },
      onError: (error) => {
        toast.error(error.response?.data?.message || 'Failed to create appointment');
      }
    }
  );

  const handleSubmit = (e) => {
    e.preventDefault();
    const appointmentData = {
      ...formData,
      services: selectedServices.map(service => ({
        serviceId: service.id,
        price: service.price,
        commissionRate: service.commissionRate || 0
      }))
    };
    createMutation.mutate(appointmentData);
  };

  const handleServiceToggle = (service) => {
    setSelectedServices(prev => {
      const exists = prev.find(s => s.id === service.id);
      if (exists) {
        return prev.filter(s => s.id !== service.id);
      } else {
        return [...prev, { ...service, price: service.price || 0, commissionRate: 0 }];
      }
    });
  };

  const calculateDuration = () => {
    return selectedServices.reduce((total, service) => total + (service.durationMinutes || 0), 0);
  };

  const calculateTotal = () => {
    return selectedServices.reduce((total, service) => total + (service.price || 0), 0);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-10 mx-auto p-0 border w-full max-w-4xl shadow-xl rounded-xl bg-white">
        {/* Header */}
        <div className="px-6 py-4 border-b border-gray-200 bg-gradient-to-r from-blue-50 to-indigo-50">
          <div className="flex justify-between items-center">
            <h3 className="text-xl font-semibold text-gray-900 flex items-center">
              <PlusIcon className="h-6 w-6 mr-2 text-blue-600" />
              Create New Appointment
            </h3>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 transition-colors duration-200"
            >
              <XCircleIcon className="h-6 w-6" />
            </button>
          </div>
        </div>

        {/* Progress Steps */}
        <div className="px-6 py-4 border-b border-gray-200">
          <div className="flex items-center space-x-4">
            {[
              { step: 1, label: 'Basic Info', icon: UserIcon },
              { step: 2, label: 'Services', icon: SparklesIcon },
              { step: 3, label: 'Schedule', icon: ClockIcon },
              { step: 4, label: 'Review', icon: CheckCircleIcon }
            ].map(({ step: stepNum, label, icon: Icon }) => (
              <div key={stepNum} className="flex items-center">
                <div className={`flex items-center justify-center w-8 h-8 rounded-full text-sm font-medium ${
                  step >= stepNum 
                    ? 'bg-blue-600 text-white' 
                    : 'bg-gray-200 text-gray-600'
                }`}>
                  {step > stepNum ? <CheckCircleIcon className="h-5 w-5" /> : <Icon className="h-4 w-4" />}
                </div>
                <span className={`ml-2 text-sm font-medium ${
                  step >= stepNum ? 'text-blue-600' : 'text-gray-500'
                }`}>
                  {label}
                </span>
                {stepNum < 4 && (
                  <div className={`w-8 h-0.5 ml-4 ${
                    step > stepNum ? 'bg-blue-600' : 'bg-gray-200'
                  }`} />
                )}
              </div>
            ))}
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="p-6">
            {/* Step 1: Basic Info */}
            {step === 1 && (
              <div className="space-y-6">
                <h4 className="text-lg font-medium text-gray-900">Customer & Staff Information</h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Customer</label>
                    <select
                      value={formData.customerId}
                      onChange={(e) => setFormData({...formData, customerId: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      required
                    >
                      <option value="">Select Customer</option>
                      {customers?.data?.map(customer => (
                        <option key={customer.id} value={customer.id}>
                          {customer.firstName} {customer.lastName}
                        </option>
                      ))}
                    </select>
                  </div>
                  
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Staff Member</label>
                    <select
                      value={formData.staffId}
                      onChange={(e) => setFormData({...formData, staffId: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      required
                    >
                      <option value="">Select Staff</option>
                      {staff?.data?.map(staffMember => (
                        <option key={staffMember.id} value={staffMember.id}>
                          {staffMember.firstName} {staffMember.lastName}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>
            )}

            {/* Step 2: Services */}
            {step === 2 && (
              <div className="space-y-6">
                <h4 className="text-lg font-medium text-gray-900">Select Services</h4>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {services?.data?.map(service => (
                    <div
                      key={service.id}
                      onClick={() => handleServiceToggle(service)}
                      className={`p-4 border-2 rounded-lg cursor-pointer transition-all duration-200 ${
                        selectedServices.find(s => s.id === service.id)
                          ? 'border-blue-500 bg-blue-50'
                          : 'border-gray-200 hover:border-gray-300'
                      }`}
                    >
                      <div className="flex items-center justify-between">
                        <div>
                          <h5 className="font-medium text-gray-900">{service.name}</h5>
                          <p className="text-sm text-gray-500">{service.category}</p>
                          <p className="text-sm font-medium text-green-600">
                            LKR {service.price} • {service.durationMinutes}min
                          </p>
                        </div>
                        {selectedServices.find(s => s.id === service.id) && (
                          <CheckCircleIcon className="h-5 w-5 text-blue-600" />
                        )}
                      </div>
                    </div>
                  ))}
                </div>
                
                {selectedServices.length > 0 && (
                  <div className="bg-gray-50 rounded-lg p-4">
                    <h5 className="font-medium text-gray-900 mb-2">Selected Services</h5>
                    <div className="space-y-2">
                      {selectedServices.map(service => (
                        <div key={service.id} className="flex justify-between items-center">
                          <span className="text-sm text-gray-700">{service.name}</span>
                          <span className="text-sm font-medium text-gray-900">
                            LKR {service.price} • {service.durationMinutes}min
                          </span>
                        </div>
                      ))}
                      <div className="border-t pt-2 mt-2">
                        <div className="flex justify-between items-center font-medium">
                          <span>Total Duration:</span>
                          <span>{calculateDuration()} minutes</span>
                        </div>
                        <div className="flex justify-between items-center font-medium">
                          <span>Total Price:</span>
                          <span>LKR {calculateTotal().toFixed(2)}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            )}

            {/* Step 3: Schedule */}
            {step === 3 && (
              <div className="space-y-6">
                <h4 className="text-lg font-medium text-gray-900">Schedule & Recurrence</h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Date</label>
                    <input
                      type="date"
                      value={formData.appointmentDate}
                      onChange={(e) => setFormData({...formData, appointmentDate: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      required
                    />
                  </div>
                  
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Start Time</label>
                    <input
                      type="time"
                      value={formData.startTime}
                      onChange={(e) => setFormData({...formData, startTime: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      required
                    />
                  </div>
                </div>

                <div className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    id="recurring"
                    checked={formData.isRecurring}
                    onChange={(e) => setFormData({...formData, isRecurring: e.target.checked})}
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  <label htmlFor="recurring" className="text-sm font-medium text-gray-700">
                    Make this a recurring appointment
                  </label>
                </div>

                {formData.isRecurring && (
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 p-4 bg-blue-50 rounded-lg">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">Pattern</label>
                      <select
                        value={formData.recurrencePattern}
                        onChange={(e) => setFormData({...formData, recurrencePattern: e.target.value})}
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      >
                        <option value="NONE">None</option>
                        <option value="DAILY">Daily</option>
                        <option value="WEEKLY">Weekly</option>
                        <option value="MONTHLY">Monthly</option>
                      </select>
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">Interval</label>
                      <input
                        type="number"
                        min="1"
                        value={formData.recurrenceInterval}
                        onChange={(e) => setFormData({...formData, recurrenceInterval: parseInt(e.target.value)})}
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">End Date</label>
                      <input
                        type="date"
                        value={formData.recurrenceEndDate}
                        onChange={(e) => setFormData({...formData, recurrenceEndDate: e.target.value})}
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      />
                    </div>
                  </div>
                )}

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Notes</label>
                  <textarea
                    value={formData.notes}
                    onChange={(e) => setFormData({...formData, notes: e.target.value})}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    rows={3}
                    placeholder="Add any special notes or instructions..."
                  />
                </div>
              </div>
            )}

            {/* Step 4: Review */}
            {step === 4 && (
              <div className="space-y-6">
                <h4 className="text-lg font-medium text-gray-900">Review Appointment</h4>
                <div className="bg-gray-50 rounded-lg p-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                      <h5 className="font-medium text-gray-900 mb-3">Appointment Details</h5>
                      <div className="space-y-2 text-sm">
                        <div className="flex justify-between">
                          <span className="text-gray-600">Date:</span>
                          <span className="font-medium">{formData.appointmentDate}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-gray-600">Time:</span>
                          <span className="font-medium">{formData.startTime}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-gray-600">Duration:</span>
                          <span className="font-medium">{calculateDuration()} minutes</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-gray-600">Total Price:</span>
                          <span className="font-medium text-green-600">LKR {calculateTotal().toFixed(2)}</span>
                        </div>
                        {formData.isRecurring && (
                          <div className="flex justify-between">
                            <span className="text-gray-600">Recurrence:</span>
                            <span className="font-medium">{formData.recurrencePattern} every {formData.recurrenceInterval}</span>
                          </div>
                        )}
                      </div>
                    </div>
                    <div>
                      <h5 className="font-medium text-gray-900 mb-3">Services</h5>
                      <div className="space-y-1">
                        {selectedServices.map(service => (
                          <div key={service.id} className="text-sm text-gray-700">
                            • {service.name} - LKR {service.price}
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Footer */}
          <div className="px-6 py-4 border-t border-gray-200 bg-gray-50 rounded-b-xl">
            <div className="flex justify-between">
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                Cancel
              </button>
              <div className="flex space-x-3">
                {step > 1 && (
                  <button
                    type="button"
                    onClick={() => setStep(step - 1)}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    Previous
                  </button>
                )}
                {step < 4 ? (
                  <button
                    type="button"
                    onClick={() => setStep(step + 1)}
                    className="px-4 py-2 text-sm font-medium text-white bg-blue-600 border border-transparent rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    Next
                  </button>
                ) : (
                  <button
                    type="submit"
                    disabled={createMutation.isLoading}
                    className="px-6 py-2 text-sm font-medium text-white bg-green-600 border border-transparent rounded-lg hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50"
                  >
                    {createMutation.isLoading ? 'Creating...' : 'Create Appointment'}
                  </button>
                )}
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

// Edit Appointment Modal Component
const EditAppointmentModal = ({ isOpen, onClose, appointment }) => {
  const [activeTab, setActiveTab] = useState('details');
  
  if (!isOpen || !appointment) return null;

  const formatTime = (time) => {
    return new Date(`2000-01-01T${time}`).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });
  };

  const getStatusConfig = (status) => {
    const configs = {
      BOOKED: { color: 'blue', icon: ClockIcon },
      IN_PROGRESS: { color: 'yellow', icon: ClockSolidIconFilled },
      COMPLETED: { color: 'green', icon: CheckCircleSolidIcon },
      CANCELLED: { color: 'red', icon: XCircleSolidIcon },
      NO_SHOW: { color: 'gray', icon: ExclamationTriangleSolidIcon }
    };
    return configs[status] || configs.BOOKED;
  };

  const statusConfig = getStatusConfig(appointment.status);
  const StatusIcon = statusConfig.icon;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-10 mx-auto p-0 border w-full max-w-4xl shadow-xl rounded-xl bg-white">
        {/* Header */}
        <div className="px-6 py-4 border-b border-gray-200 bg-gradient-to-r from-blue-50 to-indigo-50">
          <div className="flex justify-between items-center">
            <h3 className="text-xl font-semibold text-gray-900 flex items-center">
              <EyeIcon className="h-6 w-6 mr-2 text-blue-600" />
              Appointment Details
            </h3>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 transition-colors duration-200"
            >
              <XCircleIcon className="h-6 w-6" />
            </button>
          </div>
        </div>

        {/* Tabs */}
        <div className="px-6 py-4 border-b border-gray-200">
          <div className="flex space-x-8">
            {[
              { key: 'details', label: 'Details', icon: UserIcon },
              { key: 'services', label: 'Services', icon: SparklesIcon },
              { key: 'history', label: 'History', icon: ClockIcon },
              { key: 'feedback', label: 'Feedback', icon: HeartIcon }
            ].map(({ key, label, icon: Icon }) => (
              <button
                key={key}
                onClick={() => setActiveTab(key)}
                className={`flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors duration-200 ${
                  activeTab === key
                    ? 'bg-blue-100 text-blue-700'
                    : 'text-gray-500 hover:text-gray-700 hover:bg-gray-100'
                }`}
              >
                <Icon className="h-4 w-4 mr-2" />
                {label}
              </button>
            ))}
          </div>
        </div>

        {/* Content */}
        <div className="p-6">
          {/* Details Tab */}
          {activeTab === 'details' && (
            <div className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Customer</label>
                    <div className="flex items-center space-x-3">
                      <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <UserIcon className="h-5 w-5 text-blue-600" />
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">{appointment.customerName}</p>
                        <p className="text-sm text-gray-500">📞 {appointment.customerPhone}</p>
                      </div>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Staff Member</label>
                    <div className="flex items-center space-x-3">
                      <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center">
                        <UserIcon className="h-5 w-5 text-green-600" />
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">{appointment.staffName}</p>
                        <p className="text-sm text-gray-500">🏢 {appointment.branchName}</p>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Schedule</label>
                    <div className="bg-gray-50 rounded-lg p-4">
                      <div className="flex items-center space-x-2 mb-2">
                        <CalendarIcon className="h-4 w-4 text-gray-400" />
                        <span className="text-sm font-medium text-gray-900">
                          {new Date(appointment.appointmentDate).toLocaleDateString('en-US', {
                            weekday: 'long',
                            year: 'numeric',
                            month: 'long',
                            day: 'numeric'
                          })}
                        </span>
                      </div>
                      <div className="flex items-center space-x-2">
                        <ClockIcon className="h-4 w-4 text-gray-400" />
                        <span className="text-sm text-gray-700">
                          {formatTime(appointment.startTime)} - {formatTime(appointment.endTime)}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Status</label>
                    <div className="flex items-center space-x-2">
                      <div className={`p-2 rounded-lg bg-${statusConfig.color}-50 border border-${statusConfig.color}-200`}>
                        <StatusIcon className={`h-4 w-4 text-${statusConfig.color}-600`} />
                      </div>
                      <span className={`text-sm font-medium text-${statusConfig.color}-700`}>
                        {appointment.status.replace('_', ' ')}
                      </span>
                      {appointment.isRecurring && (
                        <span className="inline-flex items-center px-2 py-1 rounded-md text-xs font-medium bg-purple-100 text-purple-800">
                          <ArrowPathIcon className="h-3 w-3 mr-1" />
                          Recurring
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              </div>

              {appointment.notes && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Notes</label>
                  <div className="bg-gray-50 rounded-lg p-4">
                    <p className="text-sm text-gray-700">{appointment.notes}</p>
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Services Tab */}
          {activeTab === 'services' && (
            <div className="space-y-6">
              <h4 className="text-lg font-medium text-gray-900">Services & Pricing</h4>
              {appointment.services && appointment.services.length > 0 ? (
                <div className="space-y-4">
                  {appointment.services.map((service, index) => (
                    <div key={index} className="bg-gray-50 rounded-lg p-4">
                      <div className="flex justify-between items-start">
                        <div>
                          <h5 className="font-medium text-gray-900">{service.serviceName}</h5>
                          <p className="text-sm text-gray-500">{service.serviceCategory}</p>
                          <p className="text-sm text-gray-600">Duration: {service.durationMinutes} minutes</p>
                        </div>
                        <div className="text-right">
                          <p className="text-lg font-bold text-gray-900">LKR {service.price.toFixed(2)}</p>
                          {service.commissionRate > 0 && (
                            <p className="text-sm text-gray-500">
                              Commission: {service.commissionRate}%
                            </p>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                  <div className="border-t pt-4">
                    <div className="flex justify-between items-center text-lg font-bold">
                      <span>Total Amount:</span>
                      <span className="text-green-600">LKR {appointment.totalAmount.toFixed(2)}</span>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="text-center py-8">
                  <SparklesIcon className="h-12 w-12 text-gray-300 mx-auto mb-4" />
                  <p className="text-gray-500">No services assigned to this appointment</p>
                </div>
              )}
            </div>
          )}

          {/* History Tab */}
          {activeTab === 'history' && (
            <div className="space-y-6">
              <h4 className="text-lg font-medium text-gray-900">Appointment History</h4>
              <div className="space-y-4">
                <div className="bg-blue-50 rounded-lg p-4">
                  <div className="flex items-center space-x-3">
                    <CheckCircleIcon className="h-5 w-5 text-blue-600" />
                    <div>
                      <p className="text-sm font-medium text-blue-900">Appointment Created</p>
                      <p className="text-sm text-blue-700">
                        {appointment.createdAt ? new Date(appointment.createdAt).toLocaleString() : 'Unknown'}
                      </p>
                    </div>
                  </div>
                </div>
                
                {appointment.updatedAt && appointment.updatedAt !== appointment.createdAt && (
                  <div className="bg-yellow-50 rounded-lg p-4">
                    <div className="flex items-center space-x-3">
                      <PencilIcon className="h-5 w-5 text-yellow-600" />
                      <div>
                        <p className="text-sm font-medium text-yellow-900">Last Updated</p>
                        <p className="text-sm text-yellow-700">
                          {new Date(appointment.updatedAt).toLocaleString()}
                        </p>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}

          {/* Feedback Tab */}
          {activeTab === 'feedback' && (
            <div className="space-y-6">
              <h4 className="text-lg font-medium text-gray-900">Customer Feedback</h4>
              <div className="text-center py-8">
                <HeartIcon className="h-12 w-12 text-gray-300 mx-auto mb-4" />
                <p className="text-gray-500">Feedback system coming soon...</p>
              </div>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="px-6 py-4 border-t border-gray-200 bg-gray-50 rounded-b-xl">
          <div className="flex justify-end space-x-3">
            <button
              onClick={onClose}
              className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

// StatCard Component
const StatCard = ({ title, value, icon: Icon, color, trend }) => {
  const colorClasses = {
    blue: 'from-blue-500 to-blue-600',
    green: 'from-green-500 to-green-600',
    yellow: 'from-yellow-500 to-yellow-600',
    purple: 'from-purple-500 to-purple-600',
    red: 'from-red-500 to-red-600'
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition-shadow duration-200">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="text-3xl font-bold text-gray-900 mt-2">{value}</p>
          {trend && (
            <p className="text-sm text-green-600 mt-1 flex items-center">
              <span className="text-green-500 mr-1">↗</span>
              {trend} from yesterday
            </p>
          )}
        </div>
        <div className={`p-3 rounded-lg bg-gradient-to-r ${colorClasses[color] || colorClasses.blue}`}>
          <Icon className="h-6 w-6 text-white" />
        </div>
      </div>
    </div>
  );
};

// AppointmentCard Component
const AppointmentCard = ({ appointment, onStatusUpdate, onCancel, onView }) => {
  const getStatusConfig = (status) => {
    const configs = {
      BOOKED: {
        color: 'blue',
        icon: ClockIcon,
        bgColor: 'bg-blue-50',
        textColor: 'text-blue-700',
        borderColor: 'border-blue-200'
      },
      IN_PROGRESS: {
        color: 'yellow',
        icon: ClockSolidIconFilled,
        bgColor: 'bg-yellow-50',
        textColor: 'text-yellow-700',
        borderColor: 'border-yellow-200'
      },
      COMPLETED: {
        color: 'green',
        icon: CheckCircleSolidIcon,
        bgColor: 'bg-green-50',
        textColor: 'text-green-700',
        borderColor: 'border-green-200'
      },
      CANCELLED: {
        color: 'red',
        icon: XCircleSolidIcon,
        bgColor: 'bg-red-50',
        textColor: 'text-red-700',
        borderColor: 'border-red-200'
      },
      NO_SHOW: {
        color: 'gray',
        icon: ExclamationTriangleSolidIcon,
        bgColor: 'bg-gray-50',
        textColor: 'text-gray-700',
        borderColor: 'border-gray-200'
      }
    };
    return configs[status] || configs.BOOKED;
  };

  const statusConfig = getStatusConfig(appointment.status);
  const StatusIcon = statusConfig.icon;

  const formatTime = (time) => {
    return new Date(`2000-01-01T${time}`).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });
  };

  return (
    <div className="p-6 hover:bg-gray-50 transition-colors duration-200 group">
      <div className="flex items-start justify-between">
        <div className="flex-1 min-w-0">
          <div className="flex items-start space-x-4">
            {/* Status Indicator */}
            <div className={`flex-shrink-0 p-2 rounded-lg ${statusConfig.bgColor} ${statusConfig.borderColor} border`}>
              <StatusIcon className={`h-5 w-5 ${statusConfig.textColor}`} />
            </div>

            {/* Appointment Details */}
            <div className="flex-1 min-w-0">
              <div className="flex items-center space-x-3 mb-2">
                <h4 className="text-lg font-semibold text-gray-900 truncate">
                  {appointment.customerName}
                </h4>
                <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${statusConfig.bgColor} ${statusConfig.textColor}`}>
                  {appointment.status.replace('_', ' ')}
                </span>
                {appointment.isRecurring && (
                  <span className="inline-flex items-center px-2 py-1 rounded-md text-xs font-medium bg-purple-100 text-purple-800">
                    <ArrowPathIcon className="h-3 w-3 mr-1" />
                    Recurring
                  </span>
                )}
              </div>

              {/* Time and Duration */}
              <div className="flex items-center space-x-6 text-sm text-gray-600 mb-3">
                <div className="flex items-center space-x-1">
                  <ClockIcon className="h-4 w-4" />
                  <span className="font-medium">
                    {formatTime(appointment.startTime)} - {formatTime(appointment.endTime)}
                  </span>
                </div>
                <div className="flex items-center space-x-1">
                  <UserIcon className="h-4 w-4" />
                  <span>{appointment.staffName}</span>
                </div>
                <div className="flex items-center space-x-1">
                  <BuildingOfficeIcon className="h-4 w-4" />
                  <span>{appointment.branchName}</span>
                </div>
              </div>

              {/* Services and Price */}
              <div className="flex items-center justify-between">
                <div className="flex-1">
                  {appointment.services && appointment.services.length > 0 && (
                    <div className="flex flex-wrap gap-2 mb-2">
                      {appointment.services.map((service, index) => (
                        <span
                          key={index}
                          className="inline-flex items-center px-2 py-1 rounded-md text-xs font-medium bg-gray-100 text-gray-800"
                        >
                          {service.serviceName}
                        </span>
                      ))}
                    </div>
                  )}
                  <div className="flex items-center space-x-4 text-sm text-gray-500">
                    <span>📞 {appointment.customerPhone}</span>
                    {appointment.notes && (
                      <span className="flex items-center">
                        <ChatBubbleLeftRightIcon className="h-4 w-4 mr-1" />
                        Has notes
                      </span>
                    )}
                  </div>
                </div>
                <div className="text-right">
                  <div className="text-lg font-bold text-gray-900">
                    LKR {appointment.totalAmount.toFixed(2)}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center space-x-2 ml-4">
          {appointment.status === 'BOOKED' && (
            <>
              <button
                onClick={() => onStatusUpdate(appointment.id, 'IN_PROGRESS')}
                className="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-lg text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200"
              >
                <CheckCircleIcon className="h-4 w-4 mr-1" />
                Start
              </button>
              <button
                onClick={() => onCancel(appointment.id)}
                className="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-lg text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 transition-colors duration-200"
              >
                <XCircleIcon className="h-4 w-4 mr-1" />
                Cancel
              </button>
            </>
          )}
          {appointment.status === 'IN_PROGRESS' && (
            <button
              onClick={() => onStatusUpdate(appointment.id, 'COMPLETED')}
              className="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-lg text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200"
            >
              <CheckCircleIcon className="h-4 w-4 mr-1" />
              Complete
            </button>
          )}
          <button
            onClick={onView}
            className="inline-flex items-center px-3 py-2 border border-gray-300 text-sm font-medium rounded-lg text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors duration-200"
          >
            <EyeIcon className="h-4 w-4 mr-1" />
            View
          </button>
        </div>
      </div>
    </div>
  );
};

// EmptyState Component
const EmptyState = ({ title, description, icon: Icon }) => (
  <div className="text-center py-12">
    <div className="mx-auto h-24 w-24 text-gray-300 mb-4">
      <Icon className="h-full w-full" />
    </div>
    <h3 className="text-lg font-medium text-gray-900 mb-2">{title}</h3>
    <p className="text-gray-500">{description}</p>
  </div>
);

// TemplateModal Component
const TemplateModal = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-xl bg-white">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-xl font-semibold text-gray-900">Appointment Templates</h3>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors duration-200"
          >
            <XCircleIcon className="h-6 w-6" />
          </button>
        </div>
        <div className="text-center py-8">
          <SparklesIcon className="h-12 w-12 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-500">Template management coming soon...</p>
        </div>
      </div>
    </div>
  );
};

// WaitlistModal Component
const WaitlistModal = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-full max-w-4xl shadow-lg rounded-xl bg-white">
        <div className="flex justify-between items-center mb-6">
          <h3 className="text-xl font-semibold text-gray-900">Waitlist Management</h3>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors duration-200"
          >
            <XCircleIcon className="h-6 w-6" />
          </button>
        </div>
        <div className="text-center py-8">
          <UserGroupIcon className="h-12 w-12 text-gray-300 mx-auto mb-4" />
          <p className="text-gray-500">Waitlist management coming soon...</p>
        </div>
      </div>
    </div>
  );
};

export default AppointmentManagement;
