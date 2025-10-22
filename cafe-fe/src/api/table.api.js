import api from './axiosConfig';

export const getAllTables = async () => {
    const response = await api.get('/tables');
    return response.data;
};

export const getTableById = async (id) => {
    const response = await api.get(`/tables/${id}`);
    return response.data;
};

export const createTable = async (tableData) => {
    const response = await api.post('/tables', tableData);
    return response.data;
};

export const updateTable = async (id, tableData) => {
    const response = await api.put(`/tables/${id}`, tableData);
    return response.data;
};

export const deleteTable = async (id) => {
    const response = await api.delete(`/tables/${id}`);
    return response.data;
};

export const getAvailableTables = async () => {
    const response = await api.get('/tables/available');
    return response.data;
};

export const reserveTable = async (id, reservationData) => {
    const response = await api.post(`/tables/${id}/reserve`, reservationData);
    return response.data;
};

export const cancelReservation = async (id) => {
    const response = await api.post(`/tables/${id}/cancel-reservation`);
    return response.data;
};

export const updateTableStatus = async (id, status) => {
    const response = await api.patch(`/tables/${id}/status`, { status });
    return response.data;
};