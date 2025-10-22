import { useState, useEffect } from 'react';
import { Clock, Coffee, Tag } from 'lucide-react';
import { Button } from '../../components/ui/button';

export default function OrderManagement() {
  const [orders, setOrders] = useState([]);
  const [filterStatus, setFilterStatus] = useState('ALL'); // ALL, PENDING, COMPLETED, CANCELLED

  const fetchOrders = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/orders', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setOrders(data);
      }
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleStatusChange = async (orderId, newStatus) => {
    try {
      const response = await fetch(`http://localhost:8080/api/orders/${orderId}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({ status: newStatus })
      });

      if (response.ok) {
        fetchOrders();
      }
    } catch (error) {
      console.error('Error updating order status:', error);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'PENDING':
        return 'Đang chờ';
      case 'COMPLETED':
        return 'Hoàn thành';
      case 'CANCELLED':
        return 'Đã hủy';
      default:
        return status;
    }
  };

  const filteredOrders = filterStatus === 'ALL' 
    ? orders 
    : orders.filter(order => order.status === filterStatus);

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Quản lý đơn hàng</h1>
        <div className="flex space-x-2">
          {['ALL', 'PENDING', 'COMPLETED', 'CANCELLED'].map((status) => (
            <Button
              key={status}
              variant={filterStatus === status ? 'default' : 'outline'}
              onClick={() => setFilterStatus(status)}
            >
              {status === 'ALL' ? 'Tất cả' : getStatusText(status)}
            </Button>
          ))}
        </div>
      </div>

      {/* Orders Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredOrders.map((order) => (
          <div key={order.id} className="bg-white rounded-lg shadow p-6">
            <div className="flex justify-between items-start mb-4">
              <h3 className="text-xl font-bold">Bàn {order.tableNumber}</h3>
              <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                {getStatusText(order.status)}
              </span>
            </div>

            <div className="space-y-4">
              <div className="flex items-center text-gray-600">
                <Clock className="w-4 h-4 mr-2" />
                <span>{new Date(order.orderTime).toLocaleString()}</span>
              </div>

              <div className="flex items-center text-gray-600">
                <Coffee className="w-4 h-4 mr-2" />
                <span>{order.items?.length || 0} món</span>
              </div>

              <div className="flex items-center text-gray-600">
                <Tag className="w-4 h-4 mr-2" />
                <span>{order.total?.toLocaleString()}đ</span>
              </div>

              {/* Order Items */}
              <div className="mt-4 space-y-2">
                {order.items?.map((item, index) => (
                  <div key={index} className="flex justify-between text-sm">
                    <span>{item.name} x{item.quantity}</span>
                    <span>{(item.price * item.quantity).toLocaleString()}đ</span>
                  </div>
                ))}
              </div>

              {/* Action Buttons */}
              {order.status === 'PENDING' && (
                <div className="flex space-x-2 mt-4">
                  <Button
                    className="flex-1"
                    onClick={() => handleStatusChange(order.id, 'COMPLETED')}
                  >
                    Hoàn thành
                  </Button>
                  <Button
                    variant="outline"
                    className="flex-1"
                    onClick={() => handleStatusChange(order.id, 'CANCELLED')}
                  >
                    Hủy
                  </Button>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}