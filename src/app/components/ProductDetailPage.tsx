import { useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import { ArrowLeft, Star, Minus, Plus, ShoppingCart, Heart, Share2, CheckCircle } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { PRODUCTS } from '../data/products';
import { toast } from 'sonner';

export default function ProductDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addToCart, items } = useCart();

  const product = PRODUCTS.find((p) => p.id === Number(id));
  const [selectedVariant, setSelectedVariant] = useState(product?.variants[0] || '');
  const [quantity, setQuantity] = useState(1);
  const [isWishlisted, setIsWishlisted] = useState(false);
  const [adding, setAdding] = useState(false);

  if (!product) {
    return (
      <div className="min-h-screen bg-amber-50 flex items-center justify-center">
        <div className="text-center px-8">
          <p className="text-5xl mb-3">🍞</p>
          <p className="text-amber-700">Produk tidak ditemukan</p>
          <button onClick={() => navigate(-1)} className="mt-4 text-amber-600 underline text-sm">
            Kembali
          </button>
        </div>
      </div>
    );
  }

  const cartId = `${product.id}-${selectedVariant}`;
  const inCartItem = items.find((i) => i.cartId === cartId);
  const inCartQty = inCartItem?.quantity || 0;

  const handleAddToCart = async () => {
    setAdding(true);
    await new Promise((r) => setTimeout(r, 400));
    for (let i = 0; i < quantity; i++) addToCart(product, selectedVariant);
    setAdding(false);
    toast.success(`${quantity}× ${product.name} ditambahkan ke keranjang!`);
  };

  const handleShare = async () => {
    const shareData = { title: product.name, text: `Coba ${product.name} dari BreadSweet — Rp ${product.price.toLocaleString('id-ID')}`, url: window.location.href };
    if (navigator.share) {
      try { await navigator.share(shareData); } catch { /* user cancelled */ }
    } else {
      await navigator.clipboard.writeText(window.location.href);
      toast.success('Link produk disalin ke clipboard!');
    }
  };

  const discountPct = product.originalPrice
    ? Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100)
    : 0;

  return (
    <div className="min-h-screen bg-white flex flex-col max-w-[390px] mx-auto">
      {/* Image */}
      <div className="relative">
        <img
          src={product.image}
          alt={product.name}
          className="w-full h-64 object-cover"
        />
        <div className="absolute inset-0 bg-gradient-to-b from-black/20 to-transparent" />

        {/* Top bar */}
        <div className="absolute top-0 left-0 right-0 px-4 pt-12 flex justify-between items-center">
          <button
            onClick={() => navigate(-1)}
            className="w-10 h-10 bg-white/90 rounded-2xl flex items-center justify-center shadow-md"
          >
            <ArrowLeft className="w-5 h-5 text-amber-900" />
          </button>
          <div className="flex gap-2">
            <button
              onClick={() => setIsWishlisted(!isWishlisted)}
              className="w-10 h-10 bg-white/90 rounded-2xl flex items-center justify-center shadow-md"
            >
              <Heart
                className={`w-5 h-5 transition-colors ${isWishlisted ? 'fill-red-500 text-red-500' : 'text-amber-900'}`}
              />
            </button>
            <button onClick={handleShare} className="w-10 h-10 bg-white/90 rounded-2xl flex items-center justify-center shadow-md">
              <Share2 className="w-5 h-5 text-amber-900" />
            </button>
          </div>
        </div>

        {/* Badge */}
        {discountPct > 0 && (
          <div className="absolute bottom-3 left-4 bg-red-500 text-white text-xs px-3 py-1 rounded-full">
            Hemat {discountPct}%
          </div>
        )}
        {product.isNew && (
          <div className="absolute bottom-3 left-4 bg-green-500 text-white text-xs px-3 py-1 rounded-full">
            Produk Baru
          </div>
        )}
      </div>

      {/* Content */}
      <div className="flex-1 px-5 pt-5 pb-32">
        <div className="flex items-start justify-between">
          <div className="flex-1">
            <p className="text-amber-500 text-xs uppercase tracking-wide">{product.category}</p>
            <h1 className="text-amber-900 text-2xl mt-0.5 leading-tight">{product.name}</h1>
          </div>
          <div className="text-right">
            <p className="text-amber-700 text-xl">
              Rp {product.price.toLocaleString('id-ID')}
            </p>
            {product.originalPrice && (
              <p className="text-gray-400 text-xs line-through">
                Rp {product.originalPrice.toLocaleString('id-ID')}
              </p>
            )}
          </div>
        </div>

        {/* Rating & Stock */}
        <div className="flex items-center gap-3 mt-3">
          <div className="flex items-center gap-1 bg-amber-50 px-3 py-1.5 rounded-xl">
            <Star className="w-3.5 h-3.5 fill-amber-400 text-amber-400" />
            <span className="text-amber-700 text-sm">{product.rating}</span>
          </div>
          <span className="text-gray-400 text-xs">{product.sold} terjual</span>
          <span className={`text-xs px-2 py-1 rounded-lg ${product.stock > 10 ? 'bg-green-50 text-green-600' : 'bg-red-50 text-red-500'}`}>
            Stok: {product.stock}
          </span>
        </div>

        {/* Description */}
        <div className="mt-4">
          <h3 className="text-amber-900 mb-2">Deskripsi</h3>
          <p className="text-amber-700/70 text-sm leading-relaxed">{product.description}</p>
        </div>

        {/* Ingredients */}
        {product.ingredients && (
          <div className="mt-4">
            <h3 className="text-amber-900 mb-2">Bahan-Bahan</h3>
            <div className="flex flex-wrap gap-2">
              {product.ingredients.map((ing) => (
                <span key={ing} className="bg-amber-50 text-amber-700 text-xs px-3 py-1 rounded-xl border border-amber-100">
                  {ing}
                </span>
              ))}
            </div>
          </div>
        )}

        {/* Variants */}
        <div className="mt-5">
          <h3 className="text-amber-900 mb-2.5">Pilih Varian</h3>
          <div className="flex flex-wrap gap-2">
            {product.variants.map((variant) => (
              <button
                key={variant}
                onClick={() => setSelectedVariant(variant)}
                className={`px-4 py-2 rounded-xl text-sm transition-all border ${
                  selectedVariant === variant
                    ? 'bg-amber-600 text-white border-amber-600 shadow-md shadow-amber-200'
                    : 'bg-white text-amber-700 border-amber-200'
                }`}
              >
                {variant}
              </button>
            ))}
          </div>
        </div>

        {/* Quantity */}
        <div className="mt-5 flex items-center justify-between">
          <h3 className="text-amber-900">Jumlah</h3>
          <div className="flex items-center gap-4">
            <button
              onClick={() => setQuantity(Math.max(1, quantity - 1))}
              className="w-9 h-9 rounded-xl border-2 border-amber-200 flex items-center justify-center hover:border-amber-400 transition-colors"
            >
              <Minus className="w-4 h-4 text-amber-600" />
            </button>
            <span className="text-amber-900 w-6 text-center">{quantity}</span>
            <button
              onClick={() => setQuantity(Math.min(product.stock, quantity + 1))}
              className="w-9 h-9 rounded-xl bg-amber-500 flex items-center justify-center hover:bg-amber-600 transition-colors"
            >
              <Plus className="w-4 h-4 text-white" />
            </button>
          </div>
        </div>

        {inCartQty > 0 && (
          <div className="mt-3 flex items-center gap-2 bg-green-50 rounded-xl px-3 py-2">
            <CheckCircle className="w-4 h-4 text-green-500" />
            <p className="text-green-600 text-xs">{inCartQty} item varian ini sudah di keranjang</p>
          </div>
        )}
      </div>

      {/* Bottom CTA */}
      <div className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-[390px] bg-white border-t border-amber-100 px-5 py-4 flex gap-3">
        <button
          onClick={() => navigate('/app/cart')}
          className="w-12 h-12 border-2 border-amber-200 rounded-xl flex items-center justify-center relative flex-shrink-0"
        >
          <ShoppingCart className="w-5 h-5 text-amber-600" />
        </button>
        <button
          onClick={handleAddToCart}
          disabled={adding || product.stock === 0}
          className="flex-1 py-3 bg-gradient-to-r from-amber-600 to-amber-500 text-white rounded-xl shadow-lg shadow-amber-200 disabled:opacity-60 transition-all flex items-center justify-center gap-2"
        >
          {adding ? (
            <div className="w-5 h-5 border-2 border-white/40 border-t-white rounded-full animate-spin" />
          ) : (
            <>
              <Plus className="w-4 h-4" strokeWidth={2.5} />
              Tambah ke Keranjang · Rp {(product.price * quantity).toLocaleString('id-ID')}
            </>
          )}
        </button>
      </div>
    </div>
  );
}
