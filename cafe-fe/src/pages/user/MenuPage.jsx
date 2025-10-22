import { useState, useEffect } from 'react';
import { Button } from '../../components/ui/button';
import { Coffee, GlassWater, Wine, Soup } from 'lucide-react';

const MenuPage = () => {
    const [products, setProducts] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('ALL');
    const [cart, setCart] = useState([]);

    const categories = [
        { id: 'ALL', name: 'Tất cả', icon: Coffee },
        { id: 'COFFEE', name: 'Cà phê', icon: Coffee },
        { id: 'TEA', name: 'Trà', icon: Wine },
        { id: 'JUICE', name: 'Nước ép', icon: GlassWater },
        { id: 'FOOD', name: 'Đồ ăn', icon: Soup },
    ];

    const fetchProducts = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/products');
            if (response.ok) {
                const data = await response.json();
                setProducts(data);
            }
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    const filteredProducts = selectedCategory === 'ALL'
        ? products
        : products.filter(product => product.category === selectedCategory);

    const addToCart = (product) => {
        setCart(prevCart => {
            const existingItem = prevCart.find(item => item.id === product.id);
            if (existingItem) {
                return prevCart.map(item =>
                    item.id === product.id
                        ? { ...item, quantity: item.quantity + 1 }
                        : item
                );
            }
            return [...prevCart, { ...product, quantity: 1 }];
        });
    };

    const getCategoryIcon = (categoryId) => {
        const category = categories.find(c => c.id === categoryId);
        return category ? category.icon : Coffee;
    };

    return (
        <div className="min-h-screen bg-gray-50 pt-4">
            <div className="container mx-auto px-4 py-8">
                {/* Category Filter */}
                <div className="flex space-x-4 mb-8 overflow-x-auto pb-4">
                    {categories.map((category) => {
                        const Icon = category.icon;
                        return (
                            <Button
                                key={category.id}
                                variant={selectedCategory === category.id ? "default" : "outline"}
                                className="flex items-center space-x-2 px-4 py-2 min-w-[120px]"
                                onClick={() => setSelectedCategory(category.id)}
                            >
                                <Icon className="w-5 h-5" />
                                <span>{category.name}</span>
                            </Button>
                        );
                    })}
                </div>

                {/* Products Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                    {filteredProducts.map((product) => {
                        const Icon = getCategoryIcon(product.category);
                        return (
                            <div
                                key={product.id}
                                className="bg-white rounded-lg shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
                            >
                                <div className="aspect-w-16 aspect-h-9 bg-gray-100 flex items-center justify-center p-4">
                                    <Icon className="w-12 h-12 text-gray-400" />
                                </div>
                                <div className="p-4">
                                    <h3 className="text-lg font-semibold mb-2">{product.name}</h3>
                                    <p className="text-gray-600 text-sm mb-4 line-clamp-2">
                                        {product.description}
                                    </p>
                                    <div className="flex items-center justify-between">
                                        <span className="text-lg font-bold text-green-600">
                                            {product.price.toLocaleString()}đ
                                        </span>
                                        <Button
                                            size="sm"
                                            onClick={() => addToCart(product)}
                                        >
                                            Thêm vào giỏ
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        );
                    })}
                </div>

                {/* Cart Preview */}
                {cart.length > 0 && (
                    <div className="fixed bottom-0 right-0 mb-4 mr-4 bg-white rounded-lg shadow-lg p-4 max-w-md w-full">
                        <h3 className="font-semibold mb-2">Giỏ hàng ({cart.reduce((sum, item) => sum + item.quantity, 0)} món)</h3>
                        <div className="max-h-48 overflow-y-auto space-y-2">
                            {cart.map((item) => (
                                <div key={item.id} className="flex justify-between items-center">
                                    <span>{item.name} x{item.quantity}</span>
                                    <span>{(item.price * item.quantity).toLocaleString()}đ</span>
                                </div>
                            ))}
                        </div>
                        <div className="mt-4 pt-2 border-t">
                            <div className="flex justify-between items-center mb-4">
                                <span className="font-semibold">Tổng cộng:</span>
                                <span className="font-bold text-green-600">
                                    {cart.reduce((sum, item) => sum + (item.price * item.quantity), 0).toLocaleString()}đ
                                </span>
                            </div>
                            <div className="flex space-x-2">
                                <Button
                                    variant="outline"
                                    className="flex-1"
                                    onClick={() => setCart([])}
                                >
                                    Xóa giỏ hàng
                                </Button>
                                <Button className="flex-1">
                                    Đặt hàng
                                </Button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default MenuPage;