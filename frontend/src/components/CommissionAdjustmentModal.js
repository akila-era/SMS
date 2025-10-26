import React, { useState } from 'react';

const CommissionAdjustmentModal = ({ isOpen, onClose, commission, onAdjust }) => {
  const [formData, setFormData] = useState({
    newAmount: '',
    reason: '',
    adjustmentType: 'MANUAL_ADJUSTMENT'
  });

  const [loading, setLoading] = useState(false);

  React.useEffect(() => {
    if (commission && isOpen) {
      setFormData({
        newAmount: commission.amount || '',
        reason: '',
        adjustmentType: 'MANUAL_ADJUSTMENT'
      });
    }
  }, [commission, isOpen]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.newAmount || !formData.reason) return;

    setLoading(true);
    try {
      await onAdjust({
        commissionId: commission.id,
        newAmount: parseFloat(formData.newAmount),
        reason: formData.reason,
        adjustmentType: formData.adjustmentType
      });
      onClose();
      setFormData({ newAmount: '', reason: '', adjustmentType: 'MANUAL_ADJUSTMENT' });
    } catch (error) {
      console.error('Error adjusting commission:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setFormData({ newAmount: '', reason: '', adjustmentType: 'MANUAL_ADJUSTMENT' });
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="mt-3">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-medium text-gray-900">Adjust Commission</h3>
            <button
              onClick={handleClose}
              className="text-gray-400 hover:text-gray-600"
            >
              <span className="sr-only">Close</span>
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          {commission && (
            <div className="mb-4 p-3 bg-gray-50 rounded-md">
              <div className="text-sm text-gray-600">
                <p><strong>Staff:</strong> {commission.staffName}</p>
                <p><strong>Service:</strong> {commission.serviceName}</p>
                <p><strong>Current Amount:</strong> LKR {commission.amount}</p>
                <p><strong>Appointment:</strong> #{commission.appointmentId}</p>
              </div>
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                New Amount (LKR)
              </label>
              <input
                type="number"
                step="0.01"
                min="0"
                value={formData.newAmount}
                onChange={(e) => setFormData({...formData, newAmount: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
            </div>

            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Adjustment Type
              </label>
              <select
                value={formData.adjustmentType}
                onChange={(e) => setFormData({...formData, adjustmentType: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option value="MANUAL_ADJUSTMENT">Manual Adjustment</option>
                <option value="BONUS">Bonus</option>
                <option value="CORRECTION">Correction</option>
                <option value="REFUND">Refund</option>
                <option value="CANCELLATION">Cancellation</option>
                <option value="PRICE_CHANGE">Price Change</option>
              </select>
            </div>

            <div className="mb-6">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Reason
              </label>
              <textarea
                value={formData.reason}
                onChange={(e) => setFormData({...formData, reason: e.target.value})}
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="Please provide a reason for this adjustment..."
                required
              />
            </div>

            <div className="flex justify-end space-x-3">
              <button
                type="button"
                onClick={handleClose}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={loading || !formData.newAmount || !formData.reason}
                className="px-4 py-2 text-sm font-medium text-white bg-indigo-600 border border-transparent rounded-md shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? 'Adjusting...' : 'Adjust Commission'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CommissionAdjustmentModal;
