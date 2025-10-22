import api from './axiosConfig';

export const getAllTables = async () => {
    const res = await api.get('/tables');
    return res.data;
};

export const getTable = async (id) => {
    const res = await api.get(`/tables/${id}`);
    return res.data;
};

export const createTable = async (tableData) => {
    const res = await api.post('/tables', tableData);
    return res.data;
};

export const updateTable = async (id, tableData) => {
    const res = await api.put(`/tables/${id}`, tableData);
    return res.data;
};

export const deleteTable = async (id) => {
    const res = await api.delete(`/tables/${id}`);
    return res.data;
};

// Reserve a table by creating an order tied to the table
export const reserveTable = async (id, reservation) => {
    const payload = {
        tableId: id,
        items: reservation.items || [],
        deliveryTime: reservation.reservationTime || null
    };
    const res = await api.post('/orders', payload);
    return res.data;
};

export const updateTableStatus = async (id, status) => {
    // backend expects a raw enum value as body (e.g. "OCCUPIED")
    const res = await api.patch(`/tables/${id}/status`, status);
    return res.data;
};

export default { getAllTables, getTable, createTable, updateTable, deleteTable, reserveTable, updateTableStatus };