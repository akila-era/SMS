import React, { useState, useEffect } from 'react';
import { 
  ShoppingCartIcon, 
  PlusIcon, 
  ExclamationTriangleIcon,
  ChartBarIcon,
  ClipboardDocumentListIcon,
  TruckIcon,
  UserGroupIcon
} from '@heroicons/react/24/outline';
import ProductManagement from '../components/inventory/ProductManagement';
import InventoryDashboard from '../components/inventory/InventoryDashboard';
import PurchaseEntry from '../components/inventory/PurchaseEntry';
import SupplierManagement from '../components/inventory/SupplierManagement';
import InventoryReports from '../components/inventory/InventoryReports';
import StockAdjustment from '../components/inventory/StockAdjustment';

const InventoryManagement = () => {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Get user from localStorage or context
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const tabs = [
    { id: 'dashboard', name: 'Dashboard', icon: ChartBarIcon, description: 'Overview and analytics' },
    { id: 'products', name: 'Products', icon: ShoppingCartIcon, description: 'Manage products and inventory' },
    { id: 'suppliers', name: 'Suppliers', icon: TruckIcon, description: 'Manage suppliers and vendors' },
    { id: 'purchase', name: 'Purchase Entry', icon: PlusIcon, description: 'Record new purchases' },
    { id: 'adjustment', name: 'Stock Adjustment', icon: ClipboardDocumentListIcon, description: 'Adjust stock levels' },
    { id: 'reports', name: 'Reports', icon: ChartBarIcon, description: 'Inventory reports and analytics' }
  ];

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <InventoryDashboard user={user} />;
      case 'products':
        return <ProductManagement user={user} />;
      case 'suppliers':
        return <SupplierManagement user={user} />;
      case 'purchase':
        return <PurchaseEntry user={user} />;
      case 'adjustment':
        return <StockAdjustment user={user} />;
      case 'reports':
        return <InventoryReports user={user} />;
      default:
        return <InventoryDashboard user={user} />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">Inventory Management</h1>
              <p className="mt-1 text-sm text-gray-500">
                Manage products, suppliers, and inventory across all branches
              </p>
            </div>
            <div className="flex items-center space-x-4">
              <div className="text-sm text-gray-500">
                Welcome, {user?.firstName || 'User'}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Tab Navigation */}
        <div className="mb-8">
          <div className="border-b border-gray-200">
            <nav className="-mb-px flex space-x-8">
              {tabs.map((tab) => {
                const Icon = tab.icon;
                return (
                  <button
                    key={tab.id}
                    onClick={() => setActiveTab(tab.id)}
                    className={`${
                      activeTab === tab.id
                        ? 'border-blue-500 text-blue-600'
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    } whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm flex items-center space-x-2`}
                  >
                    <Icon className="h-5 w-5" />
                    <span>{tab.name}</span>
                  </button>
                );
              })}
            </nav>
          </div>
        </div>

        {/* Tab Content */}
        <div className="bg-white rounded-lg shadow">
          {renderContent()}
        </div>
      </div>
    </div>
  );
};

export default InventoryManagement;