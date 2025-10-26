import React, { useState, useEffect } from 'react';
import { 
  PlusIcon, 
  PrinterIcon, 
  EyeIcon, 
  PencilIcon, 
  TrashIcon,
  CreditCardIcon,
  BanknotesIcon,
  ChartBarIcon,
  FunnelIcon,
  MagnifyingGlassIcon
} from '@heroicons/react/24/outline';
import { api } from '../services/api';

const BillingManagement = () => {
  const [bills, setBills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedBill, setSelectedBill] = useState(null);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [showBillModal, setShowBillModal] = useState(false);
  const [filters, setFilters] = useState({
    status: '',
    paymentMethod: '',
    startDate: '',
    endDate: '',
    search: ''
  });
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [stats, setStats] = useState({
    totalRevenue: 0,
    paidBills: 0,
    partialBills: 0,
    unpaidBills: 0
  });

  // Payment form state
  const [paymentForm, setPaymentForm] = useState({
    payments: [
      { paymentMethod: 'CASH', amount: '', referenceNo: '' }
    ],
    discountAmount: 0,
    loyaltyRedeemed: 0,
    remarks: ''
  });

  useEffect(() => {
    fetchBills();
    fetchStats();
  }, [currentPage, filters]);

  const fetchBills = async () => {
    try {
      setLoading(true);
      const params = new URLSearchParams({
        page: currentPage - 1,
        size: 10,
        ...filters
      });
      
      const response = await api.get(`/billing/branch/1?${params}`);
      setBills(response.data.content || []);
      setTotalPages(response.data.totalPages || 1);
    } catch (error) {
      console.error('Error fetching bills:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchStats = async () => {
    try {
      const today = new Date().toISOString().split('T')[0];
      const response = await api.get(`/billing/report/branch/1?startDate=${today}&endDate=${today}`);
      const data = response.data;
      setStats({
        totalRevenue: data.totalRevenue || 0,
        paidBills: data.paidBills || 0,
        partialBills: data.partialBills || 0,
        unpaidBills: data.unpaidBills || 0
      });
    } catch (error) {
      console.error('Error fetching stats:', error);
    }
  };

  const handlePayment = async (billId) => {
    try {
      await api.post(`/billing/${billId}/payment`, paymentForm);
      setShowPaymentModal(false);
      fetchBills();
      fetchStats();
      // Reset payment form
      setPaymentForm({
        payments: [{ paymentMethod: 'CASH', amount: '', referenceNo: '' }],
        discountAmount: 0,
        loyaltyRedeemed: 0,
        remarks: ''
      });
    } catch (error) {
      console.error('Error processing payment:', error);
    }
  };

  const handleRefund = async (billId) => {
    if (window.confirm('Are you sure you want to refund this bill?')) {
      try {
        await api.post(`/billing/${billId}/refund?reason=Customer request`);
        fetchBills();
        fetchStats();
      } catch (error) {
        console.error('Error processing refund:', error);
      }
    }
  };

  const addPaymentMethod = () => {
    setPaymentForm({
      ...paymentForm,
      payments: [...paymentForm.payments, { paymentMethod: 'CASH', amount: '', referenceNo: '' }]
    });
  };

  const removePaymentMethod = (index) => {
    if (paymentForm.payments.length > 1) {
      const newPayments = paymentForm.payments.filter((_, i) => i !== index);
      setPaymentForm({ ...paymentForm, payments: newPayments });
    }
  };

  const updatePaymentMethod = (index, field, value) => {
    const newPayments = [...paymentForm.payments];
    newPayments[index][field] = value;
    setPaymentForm({ ...paymentForm, payments: newPayments });
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PAID': return 'bg-green-100 text-green-800';
      case 'PARTIAL': return 'bg-yellow-100 text-yellow-800';
      case 'UNPAID': return 'bg-red-100 text-red-800';
      case 'REFUNDED': return 'bg-gray-100 text-gray-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-LK', {
      style: 'currency',
      currency: 'LKR'
    }).format(amount);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-LK', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div className="p-6">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Billing & Payment Management</h1>
        <p className="text-gray-600 mt-2">Manage bills, process payments, and track revenue</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="p-2 bg-green-100 rounded-lg">
              <BanknotesIcon className="h-6 w-6 text-green-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Total Revenue</p>
              <p className="text-2xl font-bold text-gray-900">{formatCurrency(stats.totalRevenue)}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="p-2 bg-green-100 rounded-lg">
              <ChartBarIcon className="h-6 w-6 text-green-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Paid Bills</p>
              <p className="text-2xl font-bold text-gray-900">{stats.paidBills}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="p-2 bg-yellow-100 rounded-lg">
              <ChartBarIcon className="h-6 w-6 text-yellow-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Partial Bills</p>
              <p className="text-2xl font-bold text-gray-900">{stats.partialBills}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center">
            <div className="p-2 bg-red-100 rounded-lg">
              <ChartBarIcon className="h-6 w-6 text-red-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-600">Unpaid Bills</p>
              <p className="text-2xl font-bold text-gray-900">{stats.unpaidBills}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-lg shadow mb-6 p-6">
        <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Status</label>
            <select
              value={filters.status}
              onChange={(e) => setFilters({ ...filters, status: e.target.value })}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Status</option>
              <option value="PAID">Paid</option>
              <option value="PARTIAL">Partial</option>
              <option value="UNPAID">Unpaid</option>
              <option value="REFUNDED">Refunded</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Payment Method</label>
            <select
              value={filters.paymentMethod}
              onChange={(e) => setFilters({ ...filters, paymentMethod: e.target.value })}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">All Methods</option>
              <option value="CASH">Cash</option>
              <option value="CARD">Card</option>
              <option value="ONLINE">Online</option>
              <option value="MIXED">Mixed</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Start Date</label>
            <input
              type="date"
              value={filters.startDate}
              onChange={(e) => setFilters({ ...filters, startDate: e.target.value })}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">End Date</label>
            <input
              type="date"
              value={filters.endDate}
              onChange={(e) => setFilters({ ...filters, endDate: e.target.value })}
              className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Search</label>
            <div className="relative">
              <input
                type="text"
                placeholder="Bill number, customer..."
                value={filters.search}
                onChange={(e) => setFilters({ ...filters, search: e.target.value })}
                className="w-full border border-gray-300 rounded-md px-3 py-2 pl-10 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <MagnifyingGlassIcon className="h-5 w-5 text-gray-400 absolute left-3 top-2.5" />
            </div>
          </div>
        </div>
      </div>

      {/* Bills Table */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">Bills</h3>
        </div>

        {loading ? (
          <div className="p-6 text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-2 text-gray-600">Loading bills...</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Bill Number
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Customer
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Date
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Amount
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Payment Method
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {bills.map((bill) => (
                  <tr key={bill.billId} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {bill.billNumber}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      <div>
                        <div className="font-medium">{bill.customerName}</div>
                        <div className="text-gray-500">{bill.customerPhone}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {formatDate(bill.billDate)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      <div>
                        <div className="font-medium">{formatCurrency(bill.totalAmount)}</div>
                        {bill.balanceAmount > 0 && (
                          <div className="text-red-600">Balance: {formatCurrency(bill.balanceAmount)}</div>
                        )}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(bill.status)}`}>
                        {bill.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {bill.paymentMethod || '-'}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex space-x-2">
                        <button
                          onClick={() => {
                            setSelectedBill(bill);
                            setShowBillModal(true);
                          }}
                          className="text-blue-600 hover:text-blue-900"
                        >
                          <EyeIcon className="h-4 w-4" />
                        </button>
                        {bill.status !== 'PAID' && bill.status !== 'REFUNDED' && (
                          <button
                            onClick={() => {
                              setSelectedBill(bill);
                              setShowPaymentModal(true);
                            }}
                            className="text-green-600 hover:text-green-900"
                          >
                            <CreditCardIcon className="h-4 w-4" />
                          </button>
                        )}
                        <button
                          onClick={() => window.print()}
                          className="text-gray-600 hover:text-gray-900"
                        >
                          <PrinterIcon className="h-4 w-4" />
                        </button>
                        {bill.status === 'PAID' && (
                          <button
                            onClick={() => handleRefund(bill.billId)}
                            className="text-red-600 hover:text-red-900"
                          >
                            <TrashIcon className="h-4 w-4" />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="px-6 py-4 border-t border-gray-200">
            <div className="flex items-center justify-between">
              <div className="text-sm text-gray-700">
                Page {currentPage} of {totalPages}
              </div>
              <div className="flex space-x-2">
                <button
                  onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                  disabled={currentPage === 1}
                  className="px-3 py-1 border border-gray-300 rounded-md text-sm disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Previous
                </button>
                <button
                  onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                  disabled={currentPage === totalPages}
                  className="px-3 py-1 border border-gray-300 rounded-md text-sm disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Next
                </button>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Payment Modal */}
      {showPaymentModal && selectedBill && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                Process Payment - {selectedBill.billNumber}
              </h3>
              
              <div className="mb-4">
                <p className="text-sm text-gray-600">Total Amount: {formatCurrency(selectedBill.totalAmount)}</p>
                <p className="text-sm text-gray-600">Balance: {formatCurrency(selectedBill.balanceAmount)}</p>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Discount Amount</label>
                  <input
                    type="number"
                    value={paymentForm.discountAmount}
                    onChange={(e) => setPaymentForm({ ...paymentForm, discountAmount: parseFloat(e.target.value) || 0 })}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Loyalty Points Redeemed</label>
                  <input
                    type="number"
                    value={paymentForm.loyaltyRedeemed}
                    onChange={(e) => setPaymentForm({ ...paymentForm, loyaltyRedeemed: parseFloat(e.target.value) || 0 })}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Payment Methods</label>
                  {paymentForm.payments.map((payment, index) => (
                    <div key={index} className="flex space-x-2 mb-2">
                      <select
                        value={payment.paymentMethod}
                        onChange={(e) => updatePaymentMethod(index, 'paymentMethod', e.target.value)}
                        className="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                      >
                        <option value="CASH">Cash</option>
                        <option value="CARD">Card</option>
                        <option value="ONLINE">Online</option>
                        <option value="UPI">UPI</option>
                        <option value="GIFT_CARD">Gift Card</option>
                        <option value="LOYALTY_POINTS">Loyalty Points</option>
                      </select>
                      <input
                        type="number"
                        placeholder="Amount"
                        value={payment.amount}
                        onChange={(e) => updatePaymentMethod(index, 'amount', parseFloat(e.target.value) || 0)}
                        className="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                      <input
                        type="text"
                        placeholder="Reference"
                        value={payment.referenceNo}
                        onChange={(e) => updatePaymentMethod(index, 'referenceNo', e.target.value)}
                        className="flex-1 border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                      {paymentForm.payments.length > 1 && (
                        <button
                          onClick={() => removePaymentMethod(index)}
                          className="px-2 py-2 text-red-600 hover:text-red-800"
                        >
                          <TrashIcon className="h-4 w-4" />
                        </button>
                      )}
                    </div>
                  ))}
                  <button
                    onClick={addPaymentMethod}
                    className="text-blue-600 hover:text-blue-800 text-sm"
                  >
                    + Add Payment Method
                  </button>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Remarks</label>
                  <textarea
                    value={paymentForm.remarks}
                    onChange={(e) => setPaymentForm({ ...paymentForm, remarks: e.target.value })}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    rows="3"
                  />
                </div>
              </div>

              <div className="flex justify-end space-x-3 mt-6">
                <button
                  onClick={() => setShowPaymentModal(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  onClick={() => handlePayment(selectedBill.billId)}
                  className="px-4 py-2 bg-blue-600 border border-transparent rounded-md text-sm font-medium text-white hover:bg-blue-700"
                >
                  Process Payment
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Bill Details Modal */}
      {showBillModal && selectedBill && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-2xl shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                Bill Details - {selectedBill.billNumber}
              </h3>
              
              <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Customer</label>
                    <p className="text-sm text-gray-900">{selectedBill.customerName}</p>
                    <p className="text-sm text-gray-600">{selectedBill.customerPhone}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Bill Date</label>
                    <p className="text-sm text-gray-900">{formatDate(selectedBill.billDate)}</p>
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Services</label>
                  <div className="border border-gray-200 rounded-md">
                    <table className="min-w-full divide-y divide-gray-200">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Service</th>
                          <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Staff</th>
                          <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Qty</th>
                          <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Price</th>
                          <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Total</th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {selectedBill.billingItems?.map((item, index) => (
                          <tr key={index}>
                            <td className="px-4 py-2 text-sm text-gray-900">{item.serviceName}</td>
                            <td className="px-4 py-2 text-sm text-gray-900">{item.staffName}</td>
                            <td className="px-4 py-2 text-sm text-gray-900">{item.quantity}</td>
                            <td className="px-4 py-2 text-sm text-gray-900">{formatCurrency(item.unitPrice)}</td>
                            <td className="px-4 py-2 text-sm text-gray-900">{formatCurrency(item.total)}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>

                <div className="border-t pt-4">
                  <div className="space-y-2">
                    <div className="flex justify-between">
                      <span className="text-sm font-medium text-gray-700">Subtotal:</span>
                      <span className="text-sm text-gray-900">{formatCurrency(selectedBill.subtotal)}</span>
                    </div>
                    {selectedBill.discountAmount > 0 && (
                      <div className="flex justify-between">
                        <span className="text-sm font-medium text-gray-700">Discount:</span>
                        <span className="text-sm text-red-600">-{formatCurrency(selectedBill.discountAmount)}</span>
                      </div>
                    )}
                    {selectedBill.loyaltyRedeemed > 0 && (
                      <div className="flex justify-between">
                        <span className="text-sm font-medium text-gray-700">Loyalty Redeemed:</span>
                        <span className="text-sm text-red-600">-{formatCurrency(selectedBill.loyaltyRedeemed)}</span>
                      </div>
                    )}
                    <div className="flex justify-between">
                      <span className="text-sm font-medium text-gray-700">Tax:</span>
                      <span className="text-sm text-gray-900">{formatCurrency(selectedBill.taxAmount)}</span>
                    </div>
                    <div className="flex justify-between font-bold text-lg">
                      <span>Total:</span>
                      <span>{formatCurrency(selectedBill.totalAmount)}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-sm font-medium text-gray-700">Paid:</span>
                      <span className="text-sm text-green-600">{formatCurrency(selectedBill.paidAmount)}</span>
                    </div>
                    {selectedBill.balanceAmount > 0 && (
                      <div className="flex justify-between">
                        <span className="text-sm font-medium text-gray-700">Balance:</span>
                        <span className="text-sm text-red-600">{formatCurrency(selectedBill.balanceAmount)}</span>
                      </div>
                    )}
                  </div>
                </div>
              </div>

              <div className="flex justify-end space-x-3 mt-6">
                <button
                  onClick={() => setShowBillModal(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
                >
                  Close
                </button>
                <button
                  onClick={() => window.print()}
                  className="px-4 py-2 bg-blue-600 border border-transparent rounded-md text-sm font-medium text-white hover:bg-blue-700"
                >
                  Print Invoice
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BillingManagement;