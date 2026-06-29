import { useNavigate } from 'react-router';
import { Trash2, Plus, Minus, ShoppingBag, ArrowRight } from 'lucide-react';
import { useCart } from '../context/CartContext';

export default function CartPage() {
  const { items, removeFromCart, updateQuantity, totalPrice, totalItems } = useCart();
  const navigate = useNavigate();

  const shippingFee = totalPrice >= 100000 ? 0 : 15000;
  const grandTotal = totalPrice + shippingFee;

  return (
    <div className="flex flex-col min-h-full">
      {/* Header */}
      <div className="bg-white px-5 pt-12 pb-4 sticky top-0 z-20 shadow-sm border-b border-amber-50">
        <div className="flex items-center justify-between">
          <h1 className="text-amber-900 text-2xl">Keranjang</h1>
          {totalItems > 0 && (
            <span className="bg-amber-100 text-amber-700 text-xs px-3 py-1 rounded-full">
              {totalItems} item
            </span>
          )}
        </div>
      </div>

      {items.length === 0 ? (
        <div className="flex-1 flex flex-col items-center justify-center px-8 py-16 text-center">
          <div className="w-24 h-24 bg-amber-50 rounded-3xl flex items-center justify-center mb-5">
            <ShoppingBag className="w-12 h-12 text-amber-300" />
          </div>
          <h3 className="text-amber-900 text-xl mb-2">Keranjang Kosong</h3>
          <p className="text-amber-600/70 text-sm leading-relaxed mb-6">
            Yuk tambahkan roti favoritmu ke keranjang
          </p>
          <button
            onClick={() => navigate('/app/home')}
            className="px-6 py-3 bg-amber-600 text-white rounded-2xl shadow-md shadow-amber-200"
          >
            Mulai Belanja
          </button>
        </div>
      ) : (
        <>
          <div className="flex-1 px-5 pt-4 space-y-3 pb-4">
            {/* Free shipping banner */}
            {totalPrice < 100000 && (
              <div className="bg-amber-50 rounded-2xl px-4 py-3 border border-amber-100 flex items-center gap-3">
                <span className="text-2xl">🚚</span>
                <div>
                  <p className="text-amber-800 text-sm">
                    Tambah Rp {(100000 - totalPrice).toLocaleString('id-ID')} lagi untuk gratis ongkir!
                  </p>
                  <div className="mt-1.5 h-1.5 bg-amber-200 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-amber-500 rounded-full transition-all"
                      style={{ width: `${Math.min(100, (totalPrice / 100000) * 100)}%` }}
                    />
                  </div>
                </div>
              </div>
            )}
            {totalPrice >= 100000 && (
              <div className="bg-green-50 rounded-2xl px-4 py-3 border border-green-100 flex items-center gap-3">
                <span className="text-xl">🎉</span>
                <p className="text-green-700 text-sm">Selamat! Kamu mendapatkan gratis ongkir</p>
              </div>
            )}

            {/* Cart items */}
            {items.map((item) => (
              <div key={item.cartId} className="bg-white rounded-2xl p-3 flex gap-3 shadow-sm border border-amber-50">
                <img
                  src={item.product.image}
                  alt={item.product.name}
                  className="w-20 h-20 object-cover rounded-xl flex-shrink-0"
                />
                <div className="flex-1 min-w-0">
                  <div className="flex items-start justify-between gap-2">
                    <div className="min-w-0">
                      <h4 className="text-amber-900 text-sm leading-tight line-clamp-1">{item.product.name}</h4>
                      <span className="text-[11px] text-amber-500 bg-amber-50 px-2 py-0.5 rounded-full inline-block mt-0.5">
                        {item.selectedVariant}
                      </span>
                    </div>
                    <button
                      onClick={() => removeFromCart(item.cartId)}
                      className="w-7 h-7 flex items-center justify-center text-red-400 hover:bg-red-50 rounded-xl flex-shrink-0"
                    >
                      <Trash2 className="w-3.5 h-3.5" />
                    </button>
                  </div>
                  <div className="flex items-center justify-between mt-2">
                    <p className="text-amber-700 text-sm">
                      Rp {(item.product.price * item.quantity).toLocaleString('id-ID')}
                    </p>
                    <div className="flex items-center gap-2">
                      <button
                        onClick={() => updateQuantity(item.cartId, item.quantity - 1)}
                        className="w-7 h-7 border-2 border-amber-200 rounded-lg flex items-center justify-center hover:border-amber-400"
                      >
                        <Minus className="w-3.5 h-3.5 text-amber-600" />
                      </button>
                      <span className="text-amber-900 w-5 text-center text-sm">{item.quantity}</span>
                      <button
                        onClick={() => updateQuantity(item.cartId, item.quantity + 1)}
                        className="w-7 h-7 bg-amber-500 rounded-lg flex items-center justify-center hover:bg-amber-600"
                      >
                        <Plus className="w-3.5 h-3.5 text-white" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* Order summary + CTA */}
          <div className="bg-white rounded-t-3xl px-5 pt-5 pb-6 shadow-[0_-4px_20px_rgba(0,0,0,0.06)] border-t border-amber-50">
            <h3 className="text-amber-900 mb-3">Ringkasan</h3>
            <div className="space-y-2 mb-4">
              <div className="flex justify-between text-sm">
                <span className="text-amber-600/70">Subtotal ({totalItems} item)</span>
                <span className="text-amber-800">Rp {totalPrice.toLocaleString('id-ID')}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-amber-600/70">Ongkos Kirim</span>
                <span className={shippingFee === 0 ? 'text-green-600' : 'text-amber-800'}>
                  {shippingFee === 0 ? 'Gratis' : `Rp ${shippingFee.toLocaleString('id-ID')}`}
                </span>
              </div>
              <div className="border-t border-amber-100 pt-2 flex justify-between">
                <span className="text-amber-900">Total</span>
                <span className="text-amber-700">Rp {grandTotal.toLocaleString('id-ID')}</span>
              </div>
            </div>
            <button
              onClick={() => navigate('/app/checkout')}
              className="w-full py-4 bg-gradient-to-r from-amber-600 to-amber-500 text-white rounded-2xl shadow-lg shadow-amber-200 flex items-center justify-center gap-2"
            >
              Lanjutkan ke Pembayaran
              <ArrowRight className="w-5 h-5" />
            </button>
          </div>
        </>
      )}
    </div>
  );
}
