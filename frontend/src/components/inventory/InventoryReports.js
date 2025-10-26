import React, { useState, useEffect } from 'react';
import { 
  ChartBarIcon, 
  DocumentArrowDownIcon,
  CalendarIcon
} from '@heroicons/react/24/outline';

const InventoryReports = ({ user }) => {
  const [reportType, setReportType] = useState('stock-summary');
  const [dateRange, setDateRange] = useState({
    from: new Date().toISOString().split('T')[0],
    to: new Date().toISOString().split('T')[0]
  });
  const [branchId, setBranchId] = useState(user?.branchId || '');
  const [reportData, setReportData] = useState(null);
  const [loading, setLoading] = useState(false);

  const reportTypes = [
    { id: 'stock-summary', name: 'Stock Summary', description: 'Current stock levels by product' },
    { id: 'low-stock', name: 'Low Stock Report', description: 'Products below alert threshold' },
    { id: 'usage-report', name: 'Usage Report', description: 'Product usage by service and staff' },
    { id: 'purchase-history', name: 'Purchase History', description: 'Purchase transactions and costs' },
    { id: 'cost-analysis', name: 'Cost Analysis', description: 'Product costs vs service revenue' }
  ];

  const generateReport = async () => {
    try {
      setLoading(true);
      let url = '';
      
      switch (reportType) {
        case 'stock-summary':
          url = `/api/inventory/search?branchId=${branchId || ''}`;
          break;
        case 'low-stock':
          url = `/api/inventory/low-stock?branchId=${branchId || ''}`;
          break;
        case 'usage-report':
          url = `/api/inventory/usage/search?branchId=${branchId || ''}&fromDate=${dateRange.from}&toDate=${dateRange.to}`;
          break;
        case 'purchase-history':
          url = `/api/inventory/transactions/search?branchId=${branchId || ''}&type=PURCHASE&fromDate=${dateRange.from}&toDate=${dateRange.to}`;
          break;
        default:
          return;
      }

      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setReportData(data);
      }
    } catch (error) {
      console.error('Error generating report:', error);
    } finally {
      setLoading(false);
    }
  };

  const exportReport = () => {
    if (!reportData) return;
    
    // Simple CSV export
    let csvContent = '';
    
    switch (reportType) {
      case 'stock-summary':
        csvContent = generateStockSummaryCSV(reportData);
        break;
      case 'low-stock':
        csvContent = generateLowStockCSV(reportData);
        break;
      case 'usage-report':
        csvContent = generateUsageReportCSV(reportData);
        break;
      case 'purchase-history':
        csvContent = generatePurchaseHistoryCSV(reportData);
        break;
    }
    
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${reportType}-${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
  };

  const generateStockSummaryCSV = (data) => {
    let csv = 'Product Name,Product Code,Category,Brand,Current Stock,UOM,Alert Level,Status\n';
    data.forEach(item => {
      csv += `"${item.productName || ''}","${item.productCode || ''}","${item.productCategory || ''}","${item.productBrand || ''}","${item.quantity || 0}","${item.productUom || ''}","${item.productAlertQuantity || 0}","${item.quantity <= item.productAlertQuantity ? 'Low Stock' : 'OK'}"\n`;
    });
    return csv;
  };

  const generateLowStockCSV = (data) => {
    let csv = 'Product Name,Product Code,Current Stock,Alert Level,UOM,Branch,Status\n';
    data.forEach(item => {
      csv += `"${item.productName || ''}","${item.productCode || ''}","${item.quantity || 0}","${item.productAlertQuantity || 0}","${item.productUom || ''}","${item.branchName || ''}","${item.quantity <= 0 ? 'Out of Stock' : 'Low Stock'}"\n`;
    });
    return csv;
  };

  const generateUsageReportCSV = (data) => {
    let csv = 'Date,Product Name,Service Name,Staff Name,Customer Name,Quantity Used,Unit Cost,Total Cost\n';
    data.forEach(item => {
      csv += `"${item.usedAt ? new Date(item.usedAt).toLocaleDateString() : ''}","${item.productName || ''}","${item.serviceName || ''}","${item.staffName || ''}","${item.customerName || ''}","${item.quantityUsed || 0}","${item.unitCost || 0}","${item.totalCost || 0}"\n`;
    });
    return csv;
  };

  const generatePurchaseHistoryCSV = (data) => {
    let csv = 'Date,Product Name,Supplier,Quantity,Unit Price,Total Amount,Reference Number\n';
    data.forEach(item => {
      csv += `"${item.createdAt ? new Date(item.createdAt).toLocaleDateString() : ''}","${item.productName || ''}","${item.supplierName || ''}","${item.quantity || 0}","${item.unitPrice || 0}","${item.totalAmount || 0}","${item.referenceNumber || ''}"\n`;
    });
    return csv;
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR'
    }).format(amount);
  };

  const renderReportData = () => {
    if (!reportData) return null;

    switch (reportType) {
      case 'stock-summary':
        return (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Product</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Category</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Current Stock</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Alert Level</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {reportData.map((item, index) => (
                  <tr key={index}>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{item.productName}</div>
                        <div className="text-sm text-gray-500">{item.productCode}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{item.productCategory}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {item.quantity} {item.productUom}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {item.productAlertQuantity} {item.productUom}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        item.quantity <= 0 ? 'bg-red-100 text-red-800' :
                        item.quantity <= item.productAlertQuantity ? 'bg-yellow-100 text-yellow-800' :
                        'bg-green-100 text-green-800'
                      }`}>
                        {item.quantity <= 0 ? 'Out of Stock' :
                         item.quantity <= item.productAlertQuantity ? 'Low Stock' : 'OK'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        );

      case 'low-stock':
        return (
          <div className="space-y-4">
            {reportData.map((item, index) => (
              <div key={index} className="border border-gray-200 rounded-lg p-4">
                <div className="flex justify-between items-start">
                  <div>
                    <h4 className="font-medium text-gray-900">{item.productName}</h4>
                    <p className="text-sm text-gray-500">{item.productCode} • {item.branchName}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-lg font-semibold text-red-600">
                      {item.quantity} {item.productUom}
                    </p>
                    <p className="text-sm text-gray-500">
                      Alert: {item.productAlertQuantity} {item.productUom}
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        );

      case 'usage-report':
        return (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Product</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Service</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Staff</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Cost</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {reportData.map((item, index) => (
                  <tr key={index}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {item.usedAt ? new Date(item.usedAt).toLocaleDateString() : ''}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{item.productName}</div>
                        <div className="text-sm text-gray-500">{item.productCode}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{item.serviceName}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{item.staffName}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {item.quantityUsed} {item.productUom}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatCurrency(item.totalCost)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        );

      case 'purchase-history':
        return (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Product</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Supplier</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantity</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Unit Price</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Amount</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {reportData.map((item, index) => (
                  <tr key={index}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {item.createdAt ? new Date(item.createdAt).toLocaleDateString() : ''}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div>
                        <div className="text-sm font-medium text-gray-900">{item.productName}</div>
                        <div className="text-sm text-gray-500">{item.productCode}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{item.supplierName}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{item.quantity}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatCurrency(item.unitPrice)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatCurrency(item.totalAmount)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        );

      default:
        return null;
    }
  };

  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Inventory Reports</h2>
        <p className="text-gray-600">Generate and export inventory reports</p>
      </div>

      {/* Report Controls */}
      <div className="bg-white p-6 rounded-lg shadow mb-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Report Type</label>
            <select
              value={reportType}
              onChange={(e) => setReportType(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              {reportTypes.map((type) => (
                <option key={type.id} value={type.id}>
                  {type.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">From Date</label>
            <input
              type="date"
              value={dateRange.from}
              onChange={(e) => setDateRange({ ...dateRange, from: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">To Date</label>
            <input
              type="date"
              value={dateRange.to}
              onChange={(e) => setDateRange({ ...dateRange, to: e.target.value })}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="flex items-end">
            <button
              onClick={generateReport}
              disabled={loading}
              className="w-full px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? 'Generating...' : 'Generate Report'}
            </button>
          </div>
        </div>

        <div className="mt-4">
          <p className="text-sm text-gray-600">
            {reportTypes.find(t => t.id === reportType)?.description}
          </p>
        </div>
      </div>

      {/* Report Data */}
      {reportData && (
        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
            <h3 className="text-lg font-medium text-gray-900">
              {reportTypes.find(t => t.id === reportType)?.name}
            </h3>
            <button
              onClick={exportReport}
              className="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700"
            >
              <DocumentArrowDownIcon className="h-4 w-4 mr-2" />
              Export CSV
            </button>
          </div>
          <div className="p-6">
            {renderReportData()}
          </div>
        </div>
      )}
    </div>
  );
};

export default InventoryReports;
