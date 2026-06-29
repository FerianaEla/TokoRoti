import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { Search, Bell, Star, Plus, Flame, Sparkles } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { PRODUCTS, CATEGORIES, PROMO_BANNERS } from '../data/products';
import { Product } from '../context/CartContext';
import { toast } from 'sonner';

export default function HomePage() {
  const { user } = useAuth();
  const { addToCart, totalItems } = useCart();
  const navigate = useNavigate();
  const [search, setSearch] = useState('');
  const [activeCategory, setActiveCategory] = useState('Semua');
  const [bannerIndex, setBannerIndex] = useState(0);

  useEffect(() => {
    const timer = setInterval(() => {
      setBannerIndex((i) => (i + 1) % PROMO_BANNERS.length);
    }, 3500);
    return () => clearInterval(timer);
  }, []);

  const filtered = PRODUCTS.filter((p) => {
    const matchSearch =
      search === '' ||
      p.name.toLowerCase().includes(search.toLowerCase()) ||
      p.category.toLowerCase().includes(search.toLowerCase());
    const matchCategory = activeCategory === 'Semua' || p.category === activeCategory;
    return matchSearch && matchCategory;
  });

  const handleAddToCart = (product: Product, e: React.MouseEvent) => {
    e.stopPropagation();
    addToCart(product);
    toast.success(`${product.name} ditambahkan!`, { duration: 1500 });
  };

  const banner = PROMO_BANNERS[bannerIndex];

  return (
    <div className="flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-br from-amber-700 to-amber-500 px-5 pt-12 pb-6">
        <div className="flex justify-between items-start mb-5">
          <div>
            <p className="text-amber-200 text-sm">Selamat datang 👋</p>
            <h2 className="text-white text-xl mt-0.5 capitalize">{user?.name}</h2>
          </div>
          <button className="relative w-10 h-10 bg-white/20 rounded-xl flex items-center justify-center">
            <Bell className="w-5 h-5 text-white" />
            <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-400 rounded-full" />
          </button>
        </div>

        {/* Search */}
        <div className="relative">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-amber-400" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Cari roti favoritmu..."
            className="w-full pl-11 pr-4 py-3 bg-white rounded-2xl text-amber-900 placeholder:text-amber-300 focus:outline-none focus:ring-2 focus:ring-amber-300 text-sm shadow-sm"
          />
        </div>
      </div>

      <div className="px-5 pt-5 space-y-5">
        {/* Promo Banner */}
        <div
          className={`bg-gradient-to-r ${banner.bg} rounded-2xl p-5 flex items-center justify-between shadow-lg overflow-hidden relative cursor-pointer`}
          onClick={() => navigate('/app/categories')}
        >
          <div className="absolute -right-4 -top-4 w-24 h-24 bg-white/10 rounded-full" />
          <div className="absolute -right-2 -bottom-6 w-32 h-32 bg-white/10 rounded-full" />
          <div className="z-10">
            <span className="text-xs text-white/80 bg-white/20 px-2 py-0.5 rounded-full">Promo</span>
            <h3 className="text-white text-lg mt-1 leading-tight">{banner.title}</h3>
            <p className="text-white/80 text-xs mt-1 max-w-[180px] leading-relaxed">{banner.subtitle}</p>
          </div>
          <span className="text-5xl z-10 drop-shadow-lg">{banner.emoji}</span>
        </div>

        {/* Banner dots */}
        <div className="flex justify-center gap-1.5 -mt-3">
          {PROMO_BANNERS.map((_, i) => (
            <div
              key={i}
              className={`rounded-full transition-all ${i === bannerIndex ? 'w-4 h-1.5 bg-amber-500' : 'w-1.5 h-1.5 bg-amber-200'}`}
            />
          ))}
        </div>

        {/* Category chips */}
        <div>
          <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-hide">
            {CATEGORIES.map((cat) => (
              <button
                key={cat}
                onClick={() => setActiveCategory(cat)}
                className={`flex-shrink-0 px-4 py-2 rounded-xl text-sm transition-all ${
                  activeCategory === cat
                    ? 'bg-amber-600 text-white shadow-md shadow-amber-200'
                    : 'bg-white text-amber-700 border border-amber-100'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        {/* Section title */}
        <div className="flex items-center justify-between">
          <h3 className="text-amber-900 flex items-center gap-1.5">
            <Flame className="w-4 h-4 text-amber-500" />
            {activeCategory === 'Semua' ? 'Produk Populer' : activeCategory}
          </h3>
          <span className="text-amber-500 text-xs">{filtered.length} produk</span>
        </div>

        {/* Products Grid */}
        {filtered.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-4xl mb-3">🔍</p>
            <p className="text-amber-700">Produk tidak ditemukan</p>
          </div>
        ) : (
          <div className="grid grid-cols-2 gap-3 pb-4">
            {filtered.map((product) => (
              <div
                key={product.id}
                onClick={() => navigate(`/app/product/${product.id}`)}
                className="bg-white rounded-2xl overflow-hidden shadow-sm border border-amber-50 active:scale-95 transition-transform cursor-pointer"
              >
                <div className="relative">
                  <img
                    src={product.image}
                    alt={product.name}
                    className="w-full h-32 object-cover"
                  />
                  <div className="absolute top-2 left-2 flex gap-1">
                    {product.isNew && (
                      <span className="bg-green-500 text-white text-[10px] px-2 py-0.5 rounded-full flex items-center gap-0.5">
                        <Sparkles className="w-2.5 h-2.5" />Baru
                      </span>
                    )}
                    {product.isPromo && (
                      <span className="bg-red-500 text-white text-[10px] px-2 py-0.5 rounded-full">
                        Promo
                      </span>
                    )}
                  </div>
                </div>
                <div className="p-3">
                  <p className="text-[10px] text-amber-500 uppercase tracking-wide">{product.category}</p>
                  <h4 className="text-amber-900 text-sm mt-0.5 leading-tight line-clamp-1">{product.name}</h4>
                  <div className="flex items-center gap-1 mt-1">
                    <Star className="w-3 h-3 fill-amber-400 text-amber-400" />
                    <span className="text-[11px] text-amber-600">{product.rating}</span>
                    <span className="text-[11px] text-gray-400">({product.sold})</span>
                  </div>
                  <div className="flex items-center justify-between mt-2">
                    <div>
                      <p className="text-amber-700 text-sm">
                        Rp {product.price.toLocaleString('id-ID')}
                      </p>
                      {product.originalPrice && (
                        <p className="text-gray-400 text-[10px] line-through">
                          Rp {product.originalPrice.toLocaleString('id-ID')}
                        </p>
                      )}
                    </div>
                    <button
                      onClick={(e) => handleAddToCart(product, e)}
                      className="w-7 h-7 bg-amber-500 hover:bg-amber-600 rounded-lg flex items-center justify-center transition-colors shadow-sm"
                    >
                      <Plus className="w-4 h-4 text-white" strokeWidth={2.5} />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
