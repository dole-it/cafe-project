import api from './axiosConfig';

export const getAllProducts = async () => {
    const response = await api.get('/products');
    return response.data;
};

export const getProductsByCategory = async (category) => {
    const response = await api.get(`/products?category=${category}`);
    return response.data;
};

export const getProductById = async (id) => {
    const response = await api.get(`/products/${id}`);
    return response.data;
};

export const createProduct = async (productData) => {
    const response = await api.post('/products', productData);
    return response.data;
};

export const updateProduct = async (id, productData) => {
    const response = await api.put(`/products/${id}`, productData);
    return response.data;
};

export const deleteProduct = async (id) => {
    const response = await api.delete(`/products/${id}`);
    return response.data;
};

export const uploadProductImage = async (id, imageFile) => {
    const formData = new FormData();
    formData.append('image', imageFile);
    const response = await api.post(`/products/${id}/image`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
    return response.data;
};