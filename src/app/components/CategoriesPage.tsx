import { useState } from 'react';
import { useNavigate } from 'react-router';
import { Star, Plus, ArrowLeft } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { PRODUCTS, CATEGORIES } from '../data/products';
import { Product } from '../context/CartContext';
import { toast } from 'sonner';

const CATEGORY_ICONS: Record<string, string> = {
  'Semua': '🍞',
  'Sweet Bread': '🍞',
  'Savory Bread': '🥖',
  'Pastry & Croissant': '🥐',
  'Cakes': '🎂',
};

export default function CategoriesPage() {
  const [activeCategory, setActiveCategory] = useState('Semua');
  const { addToCart } = useCart();
  const navigate = useNavigate();

  const filtered = activeCategory === 'Semua'
    ? PRODUCTS
    : PRODUCTS.filter((p) => p.category === activeCategory);

  const handleAdd = (product: Product, e: React.MouseEvent) => {
    e.stopPropagation();
    addToCart(product);
    toast.success(`${product.name} ditambahkan!`, { duration: 1500 });
  };

  return (
    <div className="flex flex-col">
      {/* Header */}
      <div className="bg-white px-5 pt-12 pb-4 sticky top-0 z-20 shadow-sm border-b border-amber-50">
        <h1 className="text-amber-900 text-2xl mb-4">Kategori</h1>

        {/* Category pills */}
        <div className="flex gap-2 overflow-x-auto pb-1 scrollbar-hide">
          {CATEGORIES.map((cat) => (
            <button
              key={cat}
              onClick={() => setActiveCategory(cat)}
              className={`flex-shrink-0 flex items-center gap-2 px-4 py-2.5 rounded-2xl text-sm transition-all ${
                activeCategory === cat
                  ? 'bg-amber-600 text-white shadow-md shadow-amber-200'
                  : 'bg-amber-50 text-amber-700 border border-amber-100'
              }`}
            >
              <span>{CATEGORY_ICONS[cat]}</span>
              {cat}
            </button>
          ))}
        </div>
      </div>

      <div className="px-5 pt-5">
        <p className="text-amber-600 text-sm mb-4">
          {filtered.length} produk{activeCategory !== 'Semua' ? ` dalam ${activeCategory}` : ''}
        </p>

        {/* Product list */}
        <div className="space-y-3 pb-4">
          {filtered.map((product) => (
            <div
              key={product.id}
              onClick={() => navigate(`/app/product/${product.id}`)}
              className="bg-white rounded-2xl p-3 flex gap-3 shadow-sm border border-amber-50 active:scale-98 transition-transform cursor-pointer"
            >
              <img
                src={product.image}
                alt={product.name}
                className="w-20 h-20 object-cover rounded-xl flex-shrink-0"
              />
              <div className="flex-1 min-w-0">
                <div className="flex items-center justify-between">
                  <span className="text-[10px] text-amber-500 uppercase tracking-wide">{product.category}</span>
                  {product.isNew && (
                    <span className="text-[10px] text-green-600 bg-green-50 px-2 py-0.5 rounded-full">Baru</span>
                  )}
                  {product.isPromo && (
                    <span className="text-[10px] text-red-500 bg-red-50 px-2 py-0.5 rounded-full">Promo</span>
                  )}
                </div>
                <h4 className="text-amber-900 text-sm mt-0.5 leading-tight">{product.name}</h4>
                <div className="flex items-center gap-1 mt-1">
                  <Star className="w-3 h-3 fill-amber-400 text-amber-400" />
                  <span className="text-[11px] text-amber-600">{product.rating}</span>
                  <span className="text-[11px] text-gray-400">· {product.sold} terjual</span>
                </div>
                <div className="flex items-center justify-between mt-2">
                  <p className="text-amber-700 text-sm">
                    Rp {product.price.toLocaleString('id-ID')}
                  </p>
                  <button
                    onClick={(e) => handleAdd(product, e)}
                    className="flex items-center gap-1 px-3 py-1.5 bg-amber-500 hover:bg-amber-600 text-white text-xs rounded-xl transition-colors"
                  >
                    <Plus className="w-3.5 h-3.5" strokeWidth={2.5} />
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
