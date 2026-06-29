import { useEffect } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router';
import { Home, Grid3x3, ShoppingCart, User } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';

const NAV_ITEMS = [
  { path: '/app/home', icon: Home, label: 'Home' },
  { path: '/app/categories', icon: Grid3x3, label: 'Kategori' },
  { path: '/app/cart', icon: ShoppingCart, label: 'Keranjang' },
  { path: '/app/profile', icon: User, label: 'Profil' },
];

export default function MobileLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const { totalItems } = useCart();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

  return (
    <div className="min-h-screen bg-amber-50 flex items-center justify-center">
      <div className="w-full max-w-[390px] min-h-screen bg-amber-50 flex flex-col relative">
        {/* Page content */}
        <div className="flex-1 overflow-y-auto pb-20">
          <Outlet />
        </div>

        {/* Bottom Navigation */}
        <div className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] bg-white border-t border-amber-100 shadow-2xl z-50">
          <div className="flex">
            {NAV_ITEMS.map(({ path, icon: Icon, label }) => {
              const isActive = location.pathname === path;
              const isCart = path === '/app/cart';
              return (
                <button
                  key={path}
                  onClick={() => navigate(path)}
                  className="flex-1 py-3 flex flex-col items-center gap-0.5 transition-colors relative"
                >
                  <div className={`relative p-1.5 rounded-xl transition-all ${isActive ? 'bg-amber-100' : ''}`}>
                    <Icon
                      className={`w-5 h-5 transition-colors ${isActive ? 'text-amber-600' : 'text-gray-400'}`}
                      strokeWidth={isActive ? 2.5 : 2}
                    />
                    {isCart && totalItems > 0 && (
                      <span className="absolute -top-1 -right-1 w-4 h-4 bg-red-500 text-white text-[10px] rounded-full flex items-center justify-center leading-none">
                        {totalItems > 9 ? '9+' : totalItems}
                      </span>
                    )}
                  </div>
                  <span className={`text-[11px] transition-colors ${isActive ? 'text-amber-600' : 'text-gray-400'}`}>
                    {label}
                  </span>
                  {isActive && (
                    <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-5 h-0.5 bg-amber-500 rounded-full" />
                  )}
                </button>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
}
