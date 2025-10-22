import api from './axiosConfig';

export const login = async (usernameOrCredentials, password) => {
    const payload = typeof usernameOrCredentials === 'object'
        ? usernameOrCredentials
        : { username: usernameOrCredentials, password };
    const response = await api.post('/auth/login', payload);
    return response.data;
};

export const register = async (userData) => {
    const response = await api.post('/auth/register', userData);
    return response.data;
};

export const getCurrentUser = async () => {
    const response = await api.get('/auth/me');
    return response.data;
};