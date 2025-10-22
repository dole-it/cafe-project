import { useState, useEffect } from 'react';
import { Plus, Users, Coffee, Clock } from 'lucide-react';
import { Button } from '../../components/ui/button';

export default function TableManagement() {
  const [tables, setTables] = useState([]);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    tableNumber: '',
    capacity: '',
    status: 'AVAILABLE' // AVAILABLE, OCCUPIED, RESERVED
  });

  const fetchTables = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/tables', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setTables(data);
      }
    } catch (error) {
      console.error('Error fetching tables:', error);
    }
  };

  useEffect(() => {
    fetchTables();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/api/tables', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        setIsAddModalOpen(false);
        fetchTables();
      }
    } catch (error) {
      console.error('Error adding table:', error);
    }
  };

  const handleStatusChange = async (tableId, newStatus) => {
    try {
      const response = await fetch(`http://localhost:8080/api/tables/${tableId}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify({ status: newStatus })
      });

      if (response.ok) {
        fetchTables();
      }
    } catch (error) {
      console.error('Error updating table status:', error);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'AVAILABLE':
        return 'bg-green-100 text-green-800';
      case 'OCCUPIED':
        return 'bg-red-100 text-red-800';
      case 'RESERVED':
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'AVAILABLE':
        return 'Trống';
      case 'OCCUPIED':
        return 'Có khách';
      case 'RESERVED':
        return 'Đã đặt';
      default:
        return status;
    }
  };

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Quản lý Bàn</h1>
        <Button onClick={() => setIsAddModalOpen(true)}>
          <Plus className="w-4 h-4 mr-2" />
          Thêm bàn mới
        </Button>
      </div>

      {/* Tables Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {tables.map((table) => (
          <div key={table.id} className="bg-white rounded-lg shadow p-6">
            <div className="flex justify-between items-start mb-4">
              <h3 className="text-xl font-bold">Bàn {table.tableNumber}</h3>
              <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(table.status)}`}>
                {getStatusText(table.status)}
              </span>
            </div>
            
            <div className="space-y-2 mb-4">
              <div className="flex items-center text-gray-600">
                <Users className="w-4 h-4 mr-2" />
                <span>Sức chứa: {table.capacity} người</span>
              </div>
              {table.currentOrder && (
                <>
                  <div className="flex items-center text-gray-600">
                    <Coffee className="w-4 h-4 mr-2" />
                    <span>Số món: {table.currentOrder.items?.length || 0}</span>
                  </div>
                  <div className="flex items-center text-gray-600">
                    <Clock className="w-4 h-4 mr-2" />
                    <span>Thời gian: {new Date(table.currentOrder.orderTime).toLocaleTimeString()}</span>
                  </div>
                </>
              )}
            </div>

            <div className="space-x-2">
              <Button
                variant={table.status === 'AVAILABLE' ? 'default' : 'outline'}
                size="sm"
                onClick={() => handleStatusChange(table.id, 'AVAILABLE')}
              >
                Trống
              </Button>
              <Button
                variant={table.status === 'OCCUPIED' ? 'default' : 'outline'}
                size="sm"
                onClick={() => handleStatusChange(table.id, 'OCCUPIED')}
              >
                Có khách
              </Button>
              <Button
                variant={table.status === 'RESERVED' ? 'default' : 'outline'}
                size="sm"
                onClick={() => handleStatusChange(table.id, 'RESERVED')}
              >
                Đặt trước
              </Button>
            </div>
          </div>
        ))}
      </div>

      {/* Add Table Modal */}
      {isAddModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white p-6 rounded-lg w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">Thêm bàn mới</h2>
            <form onSubmit={handleSubmit}>
              <div className="space-y-4">
                <div>
                  <label className="block mb-1">Số bàn</label>
                  <input
                    type="number"
                    className="w-full p-2 border rounded"
                    value={formData.tableNumber}
                    onChange={(e) => setFormData({...formData, tableNumber: e.target.value})}
                    required
                  />
                </div>
                <div>
                  <label className="block mb-1">Sức chứa (số người)</label>
                  <input
                    type="number"
                    className="w-full p-2 border rounded"
                    value={formData.capacity}
                    onChange={(e) => setFormData({...formData, capacity: e.target.value})}
                    required
                  />
                </div>
              </div>
              <div className="flex justify-end space-x-4 mt-6">
                <Button 
                  type="button" 
                  variant="outline"
                  onClick={() => setIsAddModalOpen(false)}
                >
                  Hủy
                </Button>
                <Button type="submit">
                  Thêm bàn
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
