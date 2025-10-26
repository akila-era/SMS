import React, { useState, useEffect } from 'react';
import api from '../services/api';

const ServiceManagement = () => {
  const [activeTab, setActiveTab] = useState('services');
  const [services, setServices] = useState([]);
  const [packages, setPackages] = useState([]);
  const [promotions, setPromotions] = useState([]);
  const [analytics, setAnalytics] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('');
  const [selectedService, setSelectedService] = useState(null);
  const [formData, setFormData] = useState({});

  // Service categories
  const categories = ['Hair', 'Face', 'Nails', 'Spa', 'Body', 'Makeup', 'Massage'];

  // Commission types
  const commissionTypes = [
    { value: 'PERCENTAGE', label: 'Percentage' },
    { value: 'FIXED_AMOUNT', label: 'Fixed Amount' }
  ];

  useEffect(() => {
    loadData();
  }, [activeTab]);

  const loadData = async () => {
    setLoading(true);
    try {
      switch (activeTab) {
        case 'services':
          const servicesResponse = await api.get('/services');
          setServices(servicesResponse.data);
          break;
        case 'packages':
          const packagesResponse = await api.get('/services/packages');
          setPackages(packagesResponse.data);
          break;
        case 'promotions':
          const promotionsResponse = await api.get('/services/promotions/active');
          setPromotions(promotionsResponse.data);
          break;
        case 'analytics':
          const analyticsResponse = await api.get('/services/analytics/most-popular', {
            params: {
              startDate: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
              endDate: new Date().toISOString().split('T')[0]
            }
          });
          setAnalytics(analyticsResponse.data);
          break;
      }
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateService = async (e) => {
    e.preventDefault();
    try {
      await api.post('/services', formData);
      setShowModal(false);
      setFormData({});
      loadData();
    } catch (error) {
      console.error('Error creating service:', error);
    }
  };

  const handleCreatePackage = async (e) => {
    e.preventDefault();
    try {
      await api.post('/services/packages', formData);
      setShowModal(false);
      setFormData({});
      loadData();
    } catch (error) {
      console.error('Error creating package:', error);
    }
  };

  const handleCreatePromotion = async (e) => {
    e.preventDefault();
    try {
      await api.post('/services/promotions', formData);
      setShowModal(false);
      setFormData({});
      loadData();
    } catch (error) {
      console.error('Error creating promotion:', error);
    }
  };

  const openModal = (type, service = null) => {
    setModalType(type);
    setSelectedService(service);
    setFormData(service || {});
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setFormData({});
    setSelectedService(null);
  };

  const renderServiceForm = () => (
    <form onSubmit={handleCreateService} className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Service Name</label>
          <input
            type="text"
            required
            value={formData.name || ''}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Category</label>
          <select
            value={formData.category || ''}
            onChange={(e) => setFormData({...formData, category: e.target.value})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          >
            <option value="">Select Category</option>
            {categories.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Price (LKR)</label>
          <input
            type="number"
            step="0.01"
            required
            value={formData.price || ''}
            onChange={(e) => setFormData({...formData, price: parseFloat(e.target.value)})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Duration (minutes)</label>
          <input
            type="number"
            required
            value={formData.durationMinutes || ''}
            onChange={(e) => setFormData({...formData, durationMinutes: parseInt(e.target.value)})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Preparation Buffer (minutes)</label>
          <input
            type="number"
            value={formData.preparationBufferMinutes || 0}
            onChange={(e) => setFormData({...formData, preparationBufferMinutes: parseInt(e.target.value) || 0})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Cleanup Buffer (minutes)</label>
          <input
            type="number"
            value={formData.cleanupBufferMinutes || 0}
            onChange={(e) => setFormData({...formData, cleanupBufferMinutes: parseInt(e.target.value) || 0})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Commission Type</label>
          <select
            value={formData.commissionType || 'PERCENTAGE'}
            onChange={(e) => setFormData({...formData, commissionType: e.target.value})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          >
            {commissionTypes.map(type => (
              <option key={type.value} value={type.value}>{type.label}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">
            {formData.commissionType === 'PERCENTAGE' ? 'Commission Rate (%)' : 'Fixed Commission (LKR)'}
          </label>
          <input
            type="number"
            step="0.01"
            value={formData.commissionType === 'PERCENTAGE' ? (formData.commissionRate || 0) : (formData.fixedCommissionAmount || 0)}
            onChange={(e) => {
              if (formData.commissionType === 'PERCENTAGE') {
                setFormData({...formData, commissionRate: parseFloat(e.target.value) || 0});
              } else {
                setFormData({...formData, fixedCommissionAmount: parseFloat(e.target.value) || 0});
              }
            }}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Description</label>
        <textarea
          value={formData.description || ''}
          onChange={(e) => setFormData({...formData, description: e.target.value})}
          rows={3}
          className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Required Skills</label>
        <input
          type="text"
          placeholder="e.g., Hair Styling, Skin Care"
          value={formData.requiredSkills || ''}
          onChange={(e) => setFormData({...formData, requiredSkills: e.target.value})}
          className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Resource Requirements</label>
        <input
          type="text"
          placeholder="e.g., Chair, Facial Room"
          value={formData.resourceRequirements || ''}
          onChange={(e) => setFormData({...formData, resourceRequirements: e.target.value})}
          className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
        />
      </div>
      <div className="flex items-center">
        <input
          type="checkbox"
          checked={formData.isTaxable || false}
          onChange={(e) => setFormData({...formData, isTaxable: e.target.checked})}
          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
        />
        <label className="ml-2 block text-sm text-gray-700">Taxable Service</label>
      </div>
      <div className="flex justify-end space-x-3">
        <button
          type="button"
          onClick={closeModal}
          className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
        >
          Cancel
        </button>
        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 border border-transparent rounded-md text-sm font-medium text-white hover:bg-blue-700"
        >
          {selectedService ? 'Update' : 'Create'} Service
        </button>
      </div>
    </form>
  );

  const renderPackageForm = () => (
    <form onSubmit={handleCreatePackage} className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Package Name</label>
          <input
            type="text"
            required
            value={formData.name || ''}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Price (LKR)</label>
          <input
            type="number"
            step="0.01"
            required
            value={formData.price || ''}
            onChange={(e) => setFormData({...formData, price: parseFloat(e.target.value)})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Discount Percentage</label>
          <input
            type="number"
            step="0.01"
            value={formData.discountPercentage || 0}
            onChange={(e) => setFormData({...formData, discountPercentage: parseFloat(e.target.value) || 0})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Max Validity Days</label>
          <input
            type="number"
            value={formData.maxValidityDays || ''}
            onChange={(e) => setFormData({...formData, maxValidityDays: parseInt(e.target.value) || null})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Description</label>
        <textarea
          value={formData.description || ''}
          onChange={(e) => setFormData({...formData, description: e.target.value})}
          rows={3}
          className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
        />
      </div>
      <div className="flex items-center space-x-4">
        <div className="flex items-center">
          <input
            type="checkbox"
            checked={formData.canSplitSessions || false}
            onChange={(e) => setFormData({...formData, canSplitSessions: e.target.checked})}
            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
          />
          <label className="ml-2 block text-sm text-gray-700">Can Split Sessions</label>
        </div>
        <div className="flex items-center">
          <input
            type="checkbox"
            checked={formData.isActive !== false}
            onChange={(e) => setFormData({...formData, isActive: e.target.checked})}
            className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
          />
          <label className="ml-2 block text-sm text-gray-700">Active</label>
        </div>
      </div>
      <div className="flex justify-end space-x-3">
        <button
          type="button"
          onClick={closeModal}
          className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
        >
          Cancel
        </button>
        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 border border-transparent rounded-md text-sm font-medium text-white hover:bg-blue-700"
        >
          {selectedService ? 'Update' : 'Create'} Package
        </button>
      </div>
    </form>
  );

  const renderPromotionForm = () => (
    <form onSubmit={handleCreatePromotion} className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Promotion Name</label>
          <input
            type="text"
            required
            value={formData.name || ''}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Service ID (Optional)</label>
          <input
            type="number"
            value={formData.serviceId || ''}
            onChange={(e) => setFormData({...formData, serviceId: parseInt(e.target.value) || null})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Discount Percentage</label>
          <input
            type="number"
            step="0.01"
            value={formData.discountPercentage || 0}
            onChange={(e) => setFormData({...formData, discountPercentage: parseFloat(e.target.value) || 0})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Discount Amount (LKR)</label>
          <input
            type="number"
            step="0.01"
            value={formData.discountAmount || 0}
            onChange={(e) => setFormData({...formData, discountAmount: parseFloat(e.target.value) || 0})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Start Date</label>
          <input
            type="date"
            required
            value={formData.startDate || ''}
            onChange={(e) => setFormData({...formData, startDate: e.target.value})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">End Date</label>
          <input
            type="date"
            required
            value={formData.endDate || ''}
            onChange={(e) => setFormData({...formData, endDate: e.target.value})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Max Uses</label>
          <input
            type="number"
            value={formData.maxUses || ''}
            onChange={(e) => setFormData({...formData, maxUses: parseInt(e.target.value) || null})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Min Service Amount (LKR)</label>
          <input
            type="number"
            step="0.01"
            value={formData.minServiceAmount || ''}
            onChange={(e) => setFormData({...formData, minServiceAmount: parseFloat(e.target.value) || null})}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Description</label>
        <textarea
          value={formData.description || ''}
          onChange={(e) => setFormData({...formData, description: e.target.value})}
          rows={3}
          className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-gray-700">Applicable Branches (comma-separated IDs)</label>
        <input
          type="text"
          placeholder="e.g., 1,2,3"
          value={formData.applicableBranches || ''}
          onChange={(e) => setFormData({...formData, applicableBranches: e.target.value})}
          className="mt-1 block w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
        />
      </div>
      <div className="flex justify-end space-x-3">
        <button
          type="button"
          onClick={closeModal}
          className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
        >
          Cancel
        </button>
        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 border border-transparent rounded-md text-sm font-medium text-white hover:bg-blue-700"
        >
          Create Promotion
        </button>
      </div>
    </form>
  );

  const renderServicesTab = () => (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-lg font-medium text-gray-900">Services</h2>
        <button
          onClick={() => openModal('service')}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
          Add Service
        </button>
      </div>
      
      {loading ? (
        <div className="text-center py-8">Loading services...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {services.map(service => (
            <div key={service.id} className="bg-white border border-gray-200 rounded-lg p-6 shadow-sm">
              <div className="flex justify-between items-start mb-4">
                <h3 className="text-lg font-medium text-gray-900">{service.name}</h3>
                <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                  service.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                }`}>
                  {service.isActive ? 'Active' : 'Inactive'}
                </span>
              </div>
              <div className="space-y-2 text-sm text-gray-600">
                <p><span className="font-medium">Category:</span> {service.category}</p>
                <p><span className="font-medium">Price:</span> LKR {service.price}</p>
                <p><span className="font-medium">Duration:</span> {service.durationMinutes} min</p>
                <p><span className="font-medium">Total Duration:</span> {service.totalDurationMinutes} min</p>
                <p><span className="font-medium">Commission:</span> {service.commissionType === 'PERCENTAGE' ? `${service.commissionRate}%` : `LKR ${service.fixedCommissionAmount}`}</p>
                {service.requiredSkills && (
                  <p><span className="font-medium">Skills:</span> {service.requiredSkills}</p>
                )}
              </div>
              <div className="mt-4 flex space-x-2">
                <button
                  onClick={() => openModal('service', service)}
                  className="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200"
                >
                  Edit
                </button>
                <button
                  onClick={() => {/* Handle toggle status */}}
                  className="px-3 py-1 text-sm bg-yellow-100 text-yellow-700 rounded hover:bg-yellow-200"
                >
                  {service.isActive ? 'Deactivate' : 'Activate'}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  const renderPackagesTab = () => (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-lg font-medium text-gray-900">Service Packages</h2>
        <button
          onClick={() => openModal('package')}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
          Add Package
        </button>
      </div>
      
      {loading ? (
        <div className="text-center py-8">Loading packages...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {packages.map(pkg => (
            <div key={pkg.id} className="bg-white border border-gray-200 rounded-lg p-6 shadow-sm">
              <div className="flex justify-between items-start mb-4">
                <h3 className="text-lg font-medium text-gray-900">{pkg.name}</h3>
                <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                  pkg.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                }`}>
                  {pkg.isActive ? 'Active' : 'Inactive'}
                </span>
              </div>
              <div className="space-y-2 text-sm text-gray-600">
                <p><span className="font-medium">Price:</span> LKR {pkg.price}</p>
                {pkg.discountPercentage > 0 && (
                  <p><span className="font-medium">Discount:</span> {pkg.discountPercentage}%</p>
                )}
                <p><span className="font-medium">Duration:</span> {pkg.totalDurationMinutes} min</p>
                <p><span className="font-medium">Split Sessions:</span> {pkg.canSplitSessions ? 'Yes' : 'No'}</p>
                {pkg.maxValidityDays && (
                  <p><span className="font-medium">Validity:</span> {pkg.maxValidityDays} days</p>
                )}
              </div>
              <div className="mt-4 flex space-x-2">
                <button
                  onClick={() => openModal('package', pkg)}
                  className="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200"
                >
                  Edit
                </button>
                <button
                  onClick={() => {/* Handle toggle status */}}
                  className="px-3 py-1 text-sm bg-yellow-100 text-yellow-700 rounded hover:bg-yellow-200"
                >
                  {pkg.isActive ? 'Deactivate' : 'Activate'}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  const renderPromotionsTab = () => (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-lg font-medium text-gray-900">Active Promotions</h2>
        <button
          onClick={() => openModal('promotion')}
          className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
          Add Promotion
        </button>
      </div>
      
      {loading ? (
        <div className="text-center py-8">Loading promotions...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {promotions.map(promotion => (
            <div key={promotion.id} className="bg-white border border-gray-200 rounded-lg p-6 shadow-sm">
              <div className="flex justify-between items-start mb-4">
                <h3 className="text-lg font-medium text-gray-900">{promotion.name}</h3>
                <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                  promotion.isCurrentlyActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                }`}>
                  {promotion.isCurrentlyActive ? 'Active' : 'Inactive'}
                </span>
              </div>
              <div className="space-y-2 text-sm text-gray-600">
                <p><span className="font-medium">Service:</span> {promotion.serviceName || 'All Services'}</p>
                <p><span className="font-medium">Discount:</span> {promotion.discountPercentage > 0 ? `${promotion.discountPercentage}%` : `LKR ${promotion.discountAmount}`}</p>
                <p><span className="font-medium">Period:</span> {promotion.startDate} to {promotion.endDate}</p>
                <p><span className="font-medium">Uses:</span> {promotion.usedCount}/{promotion.maxUses || '∞'}</p>
                {promotion.minServiceAmount && (
                  <p><span className="font-medium">Min Amount:</span> LKR {promotion.minServiceAmount}</p>
                )}
              </div>
              <div className="mt-4 flex space-x-2">
                <button
                  onClick={() => {/* Handle edit */}}
                  className="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded hover:bg-gray-200"
                >
                  Edit
                </button>
                <button
                  onClick={() => {/* Handle deactivate */}}
                  className="px-3 py-1 text-sm bg-red-100 text-red-700 rounded hover:bg-red-200"
                >
                  Deactivate
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  const renderAnalyticsTab = () => (
    <div className="space-y-6">
      <h2 className="text-lg font-medium text-gray-900">Service Analytics</h2>
      
      {loading ? (
        <div className="text-center py-8">Loading analytics...</div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="bg-white border border-gray-200 rounded-lg p-6">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Most Popular Services</h3>
            <div className="space-y-3">
              {analytics.slice(0, 5).map((service, index) => (
                <div key={service.id} className="flex justify-between items-center">
                  <span className="text-sm text-gray-600">{index + 1}. {service.serviceName}</span>
                  <span className="text-sm font-medium text-gray-900">{service.totalBookings} bookings</span>
                </div>
              ))}
            </div>
          </div>
          
          <div className="bg-white border border-gray-200 rounded-lg p-6">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Revenue Summary</h3>
            <div className="space-y-3">
              {analytics.slice(0, 5).map(service => (
                <div key={service.id} className="flex justify-between items-center">
                  <span className="text-sm text-gray-600">{service.serviceName}</span>
                  <span className="text-sm font-medium text-gray-900">LKR {service.totalRevenue}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Service Management</h1>
        <p className="mt-2 text-sm text-gray-600">
          Manage your salon services, packages, promotions, and analytics.
        </p>
      </div>

      {/* Tab Navigation */}
      <div className="border-b border-gray-200 mb-8">
        <nav className="-mb-px flex space-x-8">
          {[
            { id: 'services', name: 'Services', count: services.length },
            { id: 'packages', name: 'Packages', count: packages.length },
            { id: 'promotions', name: 'Promotions', count: promotions.length },
            { id: 'analytics', name: 'Analytics', count: analytics.length }
          ].map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`py-2 px-1 border-b-2 font-medium text-sm ${
                activeTab === tab.id
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              {tab.name} ({tab.count})
            </button>
          ))}
        </nav>
      </div>

      {/* Tab Content */}
      {activeTab === 'services' && renderServicesTab()}
      {activeTab === 'packages' && renderPackagesTab()}
      {activeTab === 'promotions' && renderPromotionsTab()}
      {activeTab === 'analytics' && renderAnalyticsTab()}

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-medium text-gray-900">
                  {modalType === 'service' && (selectedService ? 'Edit Service' : 'Add Service')}
                  {modalType === 'package' && (selectedService ? 'Edit Package' : 'Add Package')}
                  {modalType === 'promotion' && 'Add Promotion'}
                </h3>
                <button
                  onClick={closeModal}
                  className="text-gray-400 hover:text-gray-600"
                >
                  <span className="sr-only">Close</span>
                  <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
              
              {modalType === 'service' && renderServiceForm()}
              {modalType === 'package' && renderPackageForm()}
              {modalType === 'promotion' && renderPromotionForm()}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ServiceManagement;


