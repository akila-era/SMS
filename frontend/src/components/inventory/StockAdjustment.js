import React, { useState, useEffect } from 'react';
import { 
  PlusIcon, 
  MagnifyingGlassIcon, 
  AdjustmentsHorizontalIcon
} from '@heroicons/react/24/outline';

const StockAdjustment = ({ user }) => {
  const [products, setProducts] = useState([]);
  const [inventory, setInventory] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [adjustmentType, setAdjustmentType] = useState('ADDITION');
  const [quantity, setQuantity] = useState('');
  const [remarks, setRemarks] = useState('');
  const [loading, setLoading] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);

  useEffect(() => {
    fetchProducts();
    if (user?.branchId) {
      fetchInventory();
    }
  }, [user?.branchId]);

  const fetchProducts = async () => {
    try {
      const response = await fetch('/api/products/active');
      if (response.ok) {
        const data = await response.json();
        setProducts(data);
      }
    } catch (error) {
      console.error('Error fetching products:', error);
    }
  };

  const fetchInventory = async () => {
    try {
      const response = await fetch(`/api/inventory/branch/${user.branchId}`);
      if (response.ok) {
        const data = await response.json();
        setInventory(data);
      }
    } catch (error) {
      console.error('Error fetching inventory:', error);
    }
  };

  const handleProductSelect = (productId) => {
    const product = products.find(p => p.productId === parseInt(productId));
    setSelectedProduct(product);
    
    // Find current inventory for this product
    const currentInventory = inventory.find(inv => inv.productId === parseInt(productId));
    if (currentInventory) {
      setQuantity(currentInventory.quantity.toString());
    } else {
      setQuantity('0');
    }
  };

  const handleAdjustment = async (e) => {
    e.preventDefault();
    
    if (!selectedProduct) {
      alert('Please select a product');
      return;
    }

    if (!quantity || parseFloat(quantity) < 0) {
      alert('Please enter a valid quantity');
      return;
    }

    try {
      setLoading(true);
      const response = await fetch(`/api/inventory/branch/${user.branchId}/product/${selectedProduct.productId}/quantity`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          newQuantity: parseFloat(quantity),
          remarks: remarks,
          userId: user.id
        })
      });

      if (response.ok) {
        setShowSuccess(true);
        setSelectedProduct(null);
        setQuantity('');
        setRemarks('');
        fetchInventory();
        setTimeout(() => setShowSuccess(false), 3000);
      } else {
        alert('Error adjusting stock');
      }
    } catch (error) {
      console.error('Error adjusting stock:', error);
      alert('Error adjusting stock');
    } finally {
      setLoading(false);
    }
  };

  const getCurrentStock = (productId) => {
    const inv = inventory.find(inv => inv.productId === productId);
    return inv ? inv.quantity : 0;
  };

  const getStockStatus = (productId) => {
    const inv = inventory.find(inv => inv.productId === productId);
    if (!inv) return 'text-gray-500';
    
    const product = products.find(p => p.productId === productId);
    if (!product) return 'text-gray-500';
    
    if (inv.quantity <= 0) return 'text-red-600';
    if (inv.quantity <= product.alertQuantity) return 'text-yellow-600';
    return 'text-green-600';
  };

  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Stock Adjustment</h2>
        <p className="text-gray-600">Adjust inventory quantities for products</p>
      </div>

      {/* Success Message */}
      {showSuccess && (
        <div className="mb-6 bg-green-50 border border-green-200 rounded-md p-4">
          <div className="flex">
            <div className="flex-shrink-0">
              <AdjustmentsHorizontalIcon className="h-5 w-5 text-green-400" />
            </div>
            <div className="ml-3">
              <p className="text-sm font-medium text-green-800">
                Stock adjusted successfully!
              </p>
            </div>
          </div>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Adjustment Form */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Adjust Stock</h3>
          
          <form onSubmit={handleAdjustment} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Select Product *</label>
              <select
                required
                value={selectedProduct?.productId || ''}
                onChange={(e) => handleProductSelect(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Choose a product...</option>
                {products.map((product) => (
                  <option key={product.productId} value={product.productId}>
                    {product.name} ({product.code}) - Current: {getCurrentStock(product.productId)} {product.uom}
                  </option>
                ))}
              </select>
            </div>

            {selectedProduct && (
              <>
                <div className="bg-gray-50 p-4 rounded-lg">
                  <h4 className="text-sm font-medium text-gray-900 mb-2">Product Details</h4>
                  <div className="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <span className="text-gray-500">Name:</span>
                      <p className="font-medium">{selectedProduct.name}</p>
                    </div>
                    <div>
                      <span className="text-gray-500">Code:</span>
                      <p className="font-medium">{selectedProduct.code}</p>
                    </div>
                    <div>
                      <span className="text-gray-500">Category:</span>
                      <p className="font-medium">{selectedProduct.category}</p>
                    </div>
                    <div>
                      <span className="text-gray-500">UOM:</span>
                      <p className="font-medium">{selectedProduct.uom}</p>
                    </div>
                    <div>
                      <span className="text-gray-500">Current Stock:</span>
                      <p className={`font-medium ${getStockStatus(selectedProduct.productId)}`}>
                        {getCurrentStock(selectedProduct.productId)} {selectedProduct.uom}
                      </p>
                    </div>
                    <div>
                      <span className="text-gray-500">Alert Level:</span>
                      <p className="font-medium">{selectedProduct.alertQuantity} {selectedProduct.uom}</p>
                    </div>
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">New Quantity *</label>
                  <input
                    type="number"
                    step="0.01"
                    required
                    value={quantity}
                    onChange={(e) => setQuantity(e.target.value)}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Enter new quantity"
                  />
                  <p className="mt-1 text-sm text-gray-500">
                    Current: {getCurrentStock(selectedProduct.productId)} {selectedProduct.uom}
                  </p>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Remarks</label>
                  <textarea
                    value={remarks}
                    onChange={(e) => setRemarks(e.target.value)}
                    rows={3}
                    placeholder="Reason for adjustment..."
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <div className="flex justify-end space-x-3 pt-4">
                  <button
                    type="button"
                    onClick={() => {
                      setSelectedProduct(null);
                      setQuantity('');
                      setRemarks('');
                    }}
                    className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-50"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={loading}
                    className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50"
                  >
                    {loading ? 'Adjusting...' : 'Adjust Stock'}
                  </button>
                </div>
              </>
            )}
          </form>
        </div>

        {/* Current Inventory */}
        <div className="bg-white p-6 rounded-lg shadow">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Current Inventory</h3>
          
          <div className="space-y-4">
            {inventory.length === 0 ? (
              <p className="text-gray-500 text-center py-4">No inventory data available</p>
            ) : (
              inventory.map((item) => {
                const product = products.find(p => p.productId === item.productId);
                if (!product) return null;
                
                return (
                  <div key={item.id} className="border border-gray-200 rounded-lg p-4">
                    <div className="flex justify-between items-start">
                      <div>
                        <h4 className="font-medium text-gray-900">{product.name}</h4>
                        <p className="text-sm text-gray-500">{product.code}</p>
                        <p className="text-sm text-gray-500">{product.category}</p>
                      </div>
                      <div className="text-right">
                        <p className={`text-lg font-semibold ${getStockStatus(item.productId)}`}>
                          {item.quantity} {product.uom}
                        </p>
                        <p className="text-sm text-gray-500">
                          Alert: {product.alertQuantity} {product.uom}
                        </p>
                      </div>
                    </div>
                  </div>
                );
              })
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default StockAdjustment;
