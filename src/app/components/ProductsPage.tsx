import { useNavigate } from 'react-router';
import { useCart, Product } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { ShoppingCart, LogOut, Plus } from 'lucide-react';
import { toast } from 'sonner';

const PRODUCTS: Product[] = [
  {
    id: 1,
    name: 'Croissant Klasik',
    price: 25000,
    image: 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=400&h=300&fit=crop',
    description: 'Croissant berlapis-lapis dengan mentega premium',
  },
  {
    id: 2,
    name: 'Roti Sourdough',
    price: 45000,
    image: 'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400&h=300&fit=crop',
    description: 'Roti artisan dengan fermentasi alami',
  },
  {
    id: 3,
    name: 'Baguette Prancis',
    price: 30000,
    image: 'https://images.unsplash.com/photo-1534620808146-d33bb39128b2?w=400&h=300&fit=crop',
    description: 'Baguette renyah khas Prancis',
  },
  {
    id: 4,
    name: 'Cinnamon Roll',
    price: 35000,
    image: 'https://images.unsplash.com/photo-1619985663533-2a6db1d1c3e0?w=400&h=300&fit=crop',
    description: 'Roti gulung kayu manis dengan frosting cream cheese',
  },
  {
    id: 5,
    name: 'Roti Gandum Whole Wheat',
    price: 40000,
    image: 'https://images.unsplash.com/photo-1585478259715-876acc5be8eb?w=400&h=300&fit=crop',
    description: 'Roti gandum utuh yang sehat dan bergizi',
  },
  {
    id: 6,
    name: 'Pain au Chocolat',
    price: 28000,
    image: 'https://images.unsplash.com/photo-1623334044303-241021148842?w=400&h=300&fit=crop',
    description: 'Pastry cokelat berlapis mentega',
  },
];

export default function ProductsPage() {
  const { addToCart, totalItems } = useCart();
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleAddToCart = (product: Product) => {
    addToCart(product);
    toast.success(`${product.name} ditambahkan ke keranjang`);
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 to-orange-50">
      <nav className="bg-white shadow-md sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-amber-500 rounded-full flex items-center justify-center">
                <ShoppingCart className="w-5 h-5 text-white" />
              </div>
              <h1 className="text-2xl font-bold text-amber-900">BreadSweat</h1>
            </div>

            <div className="flex items-center gap-4">
              <span className="text-gray-700">Halo, {user?.name}</span>
              <button
                onClick={() => navigate('/cart')}
                className="relative p-2 bg-amber-100 hover:bg-amber-200 rounded-lg transition-colors"
              >
                <ShoppingCart className="w-6 h-6 text-amber-700" />
                {totalItems > 0 && (
                  <span className="absolute -top-1 -right-1 w-5 h-5 bg-red-500 text-white text-xs rounded-full flex items-center justify-center">
                    {totalItems}
                  </span>
                )}
              </button>
              <button
                onClick={handleLogout}
                className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <LogOut className="w-6 h-6 text-gray-700" />
              </button>
            </div>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="mb-8">
          <h2 className="text-3xl font-bold text-amber-900 mb-2">Katalog Produk</h2>
          <p className="text-gray-600">Pilih roti favorit Anda</p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {PRODUCTS.map((product) => (
            <div
              key={product.id}
              className="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow"
            >
              <div className="h-48 overflow-hidden">
                <img
                  src={product.image}
                  alt={product.name}
                  className="w-full h-full object-cover"
                />
              </div>
              <div className="p-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-2">
                  {product.name}
                </h3>
                <p className="text-gray-600 text-sm mb-4">{product.description}</p>
                <div className="flex items-center justify-between">
                  <span className="text-2xl font-bold text-amber-600">
                    Rp {product.price.toLocaleString('id-ID')}
                  </span>
                  <button
                    onClick={() => handleAddToCart(product)}
                    className="flex items-center gap-2 px-4 py-2 bg-amber-500 hover:bg-amber-600 text-white font-semibold rounded-lg transition-colors"
                  >
                    <Plus className="w-4 h-4" />
                    Tambah
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
