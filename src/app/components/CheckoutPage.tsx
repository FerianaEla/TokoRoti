import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { ArrowLeft, MapPin, Store, CheckCircle, Banknote, CreditCard, Wallet } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { toast } from 'sonner';

type DeliveryMethod = 'delivery' | 'pickup';
type PaymentMethod = 'transfer' | 'cod' | 'ewallet';

const PAYMENT_OPTIONS = [
  { value: 'transfer' as PaymentMethod, label: 'Transfer Bank', icon: Banknote, sub: 'BCA, Mandiri, BNI, BRI' },
  { value: 'ewallet' as PaymentMethod, label: 'E-Wallet', icon: Wallet, sub: 'GoPay, OVO, Dana, ShopeePay' },
  { value: 'cod' as PaymentMethod, label: 'Bayar di Tempat', icon: CreditCard, sub: 'Tunai saat diterima/pickup' },
];

export default function CheckoutPage() {
  const { items, totalPrice, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [deliveryMethod, setDeliveryMethod] = useState<DeliveryMethod>('delivery');
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>('transfer');
  const [name, setName] = useState(user?.name || '');
  const [phone, setPhone] = useState(user?.phone || '');
  const [address, setAddress] = useState(user?.address || '');
  const [note, setNote] = useState('');
  const [processing, setProcessing] = useState(false);
  const [success, setSuccess] = useState(false);

  const shippingFee = deliveryMethod === 'pickup' ? 0 : (totalPrice >= 100000 ? 0 : 15000);
  const grandTotal = totalPrice + shippingFee;

  useEffect(() => {
    if (items.length === 0 && !success) {
      navigate('/app/cart');
    }
  }, [items.length, success, navigate]);

  const handleOrder = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name || !phone) {
      toast.error('Nama dan nomor telepon wajib diisi');
      return;
    }
    if (deliveryMethod === 'delivery' && !address) {
      toast.error('Alamat pengiriman wajib diisi');
      return;
    }
    setProcessing(true);
    await new Promise((r) => setTimeout(r, 2000));
    setProcessing(false);
    setSuccess(true);
    clearCart();
  };

  if (success) {
    return (
      <div className="min-h-screen bg-amber-50 flex items-center justify-center p-6 max-w-[390px] mx-auto">
        <div className="bg-white rounded-3xl shadow-xl p-8 text-center w-full">
          <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-5">
            <CheckCircle className="w-10 h-10 text-green-500" />
          </div>
          <h2 className="text-amber-900 text-2xl mb-2">Pesanan Diterima!</h2>
          <p className="text-amber-600/70 text-sm mb-1">
            No. Pesanan: <span className="text-amber-700">#BS{Date.now().toString().slice(-6)}</span>
          </p>
          <p className="text-amber-600/70 text-sm mb-6">
            {deliveryMethod === 'pickup'
              ? 'Pesanan Anda siap diambil dalam 30–45 menit'
              : 'Pesanan Anda akan dikirim dalam 1–2 jam'}
          </p>
          <div className="bg-amber-50 rounded-2xl px-4 py-3 mb-6 text-left">
            <p className="text-amber-700 text-sm">Total pembayaran</p>
            <p className="text-amber-900 text-xl mt-0.5">Rp {grandTotal.toLocaleString('id-ID')}</p>
            <p className="text-amber-500 text-xs mt-0.5">via {PAYMENT_OPTIONS.find(p => p.value === paymentMethod)?.label}</p>
          </div>
          <button
            onClick={() => navigate('/app/home')}
            className="w-full py-4 bg-gradient-to-r from-amber-600 to-amber-500 text-white rounded-2xl shadow-lg shadow-amber-200"
          >
            Kembali ke Beranda
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-amber-50 max-w-[390px] mx-auto flex flex-col">
      {/* Header */}
      <div className="bg-white px-5 pt-12 pb-4 shadow-sm">
        <div className="flex items-center gap-3">
          <button onClick={() => navigate('/app/cart')} className="w-9 h-9 bg-amber-50 rounded-xl flex items-center justify-center">
            <ArrowLeft className="w-5 h-5 text-amber-700" />
          </button>
          <h1 className="text-amber-900 text-xl">Checkout</h1>
        </div>
      </div>

      <form onSubmit={handleOrder} className="flex-1 overflow-y-auto">
        <div className="px-5 py-4 space-y-4">
          {/* Delivery Method */}
          <div className="bg-white rounded-2xl p-4 shadow-sm">
            <h3 className="text-amber-900 mb-3">Metode Penerimaan</h3>
            <div className="grid grid-cols-2 gap-2">
              <button
                type="button"
                onClick={() => setDeliveryMethod('delivery')}
                className={`p-3 rounded-xl border-2 flex flex-col items-center gap-2 transition-all ${
                  deliveryMethod === 'delivery'
                    ? 'border-amber-500 bg-amber-50'
                    : 'border-amber-100 bg-white'
                }`}
              >
                <span className="text-2xl">🚚</span>
                <span className={`text-sm ${deliveryMethod === 'delivery' ? 'text-amber-700' : 'text-gray-500'}`}>
                  Kirim ke Rumah
                </span>
              </button>
              <button
                type="button"
                onClick={() => setDeliveryMethod('pickup')}
                className={`p-3 rounded-xl border-2 flex flex-col items-center gap-2 transition-all ${
                  deliveryMethod === 'pickup'
                    ? 'border-amber-500 bg-amber-50'
                    : 'border-amber-100 bg-white'
                }`}
              >
                <Store className={`w-6 h-6 ${deliveryMethod === 'pickup' ? 'text-amber-600' : 'text-gray-400'}`} />
                <span className={`text-sm ${deliveryMethod === 'pickup' ? 'text-amber-700' : 'text-gray-500'}`}>
                  Ambil di Toko
                </span>
              </button>
            </div>
            {deliveryMethod === 'pickup' && (
              <div className="mt-3 bg-amber-50 rounded-xl p-3 flex items-start gap-2">
                <MapPin className="w-4 h-4 text-amber-500 mt-0.5 flex-shrink-0" />
                <div>
                  <p className="text-amber-800 text-sm">BreadSweet Bakery</p>
                  <p className="text-amber-600 text-xs">Jl. Roti Hangat No.1, Kota Bandung</p>
                  <p className="text-amber-500 text-xs mt-1">Buka: 06.00–21.00 setiap hari</p>
                </div>
              </div>
            )}
          </div>

          {/* Contact info */}
          <div className="bg-white rounded-2xl p-4 shadow-sm space-y-3">
            <h3 className="text-amber-900">Data Pemesan</h3>
            <div>
              <label className="text-xs text-amber-600 uppercase tracking-wide">Nama *</label>
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full mt-1.5 px-3 py-3 bg-amber-50 border border-amber-100 rounded-xl text-sm text-amber-900 focus:outline-none focus:ring-2 focus:ring-amber-300"
                placeholder="Nama lengkap"
                required
              />
            </div>
            <div>
              <label className="text-xs text-amber-600 uppercase tracking-wide">No. Telepon *</label>
              <input
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                className="w-full mt-1.5 px-3 py-3 bg-amber-50 border border-amber-100 rounded-xl text-sm text-amber-900 focus:outline-none focus:ring-2 focus:ring-amber-300"
                placeholder="08xxxxxxxxxx"
                required
              />
            </div>
            {deliveryMethod === 'delivery' && (
              <div>
                <label className="text-xs text-amber-600 uppercase tracking-wide">Alamat Pengiriman *</label>
                <textarea
                  value={address}
                  onChange={(e) => setAddress(e.target.value)}
                  rows={2}
                  className="w-full mt-1.5 px-3 py-3 bg-amber-50 border border-amber-100 rounded-xl text-sm text-amber-900 focus:outline-none focus:ring-2 focus:ring-amber-300 resize-none"
                  placeholder="Alamat lengkap termasuk kota & kode pos"
                  required
                />
              </div>
            )}
            <div>
              <label className="text-xs text-amber-600 uppercase tracking-wide">Catatan (opsional)</label>
              <input
                value={note}
                onChange={(e) => setNote(e.target.value)}
                className="w-full mt-1.5 px-3 py-3 bg-amber-50 border border-amber-100 rounded-xl text-sm text-amber-900 focus:outline-none focus:ring-2 focus:ring-amber-300"
                placeholder="Contoh: tanpa gula, pisahkan per item..."
              />
            </div>
          </div>

          {/* Payment method */}
          <div className="bg-white rounded-2xl p-4 shadow-sm">
            <h3 className="text-amber-900 mb-3">Metode Pembayaran</h3>
            <div className="space-y-2">
              {PAYMENT_OPTIONS.map(({ value, label, icon: Icon, sub }) => (
                <button
                  key={value}
                  type="button"
                  onClick={() => setPaymentMethod(value)}
                  className={`w-full flex items-center gap-3 p-3 rounded-xl border-2 transition-all ${
                    paymentMethod === value
                      ? 'border-amber-400 bg-amber-50'
                      : 'border-amber-100 bg-white'
                  }`}
                >
                  <div className={`w-9 h-9 rounded-xl flex items-center justify-center ${paymentMethod === value ? 'bg-amber-500' : 'bg-amber-100'}`}>
                    <Icon className={`w-5 h-5 ${paymentMethod === value ? 'text-white' : 'text-amber-500'}`} />
                  </div>
                  <div className="text-left flex-1">
                    <p className={`text-sm ${paymentMethod === value ? 'text-amber-800' : 'text-gray-700'}`}>{label}</p>
                    <p className="text-xs text-gray-400">{sub}</p>
                  </div>
                  <div className={`w-4 h-4 rounded-full border-2 flex-shrink-0 flex items-center justify-center ${paymentMethod === value ? 'border-amber-500 bg-amber-500' : 'border-gray-300'}`}>
                    {paymentMethod === value && <div className="w-2 h-2 rounded-full bg-white" />}
                  </div>
                </button>
              ))}
            </div>
          </div>

          {/* Order summary */}
          <div className="bg-white rounded-2xl p-4 shadow-sm">
            <h3 className="text-amber-900 mb-3">Ringkasan Pesanan</h3>
            <div className="space-y-2 mb-3">
              {items.map((item) => (
                <div key={item.cartId} className="flex justify-between text-sm">
                  <span className="text-amber-600/80 flex-1 line-clamp-1">
                    {item.product.name} ({item.selectedVariant}) ×{item.quantity}
                  </span>
                  <span className="text-amber-800 ml-2 flex-shrink-0">
                    Rp {(item.product.price * item.quantity).toLocaleString('id-ID')}
                  </span>
                </div>
              ))}
            </div>
            <div className="border-t border-amber-100 pt-3 space-y-1.5">
              <div className="flex justify-between text-sm">
                <span className="text-amber-600/70">Subtotal</span>
                <span className="text-amber-800">Rp {totalPrice.toLocaleString('id-ID')}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-amber-600/70">
                  {deliveryMethod === 'pickup' ? 'Pickup' : 'Ongkir'}
                </span>
                <span className={shippingFee === 0 ? 'text-green-600' : 'text-amber-800'}>
                  {shippingFee === 0 ? 'Gratis' : `Rp ${shippingFee.toLocaleString('id-ID')}`}
                </span>
              </div>
              <div className="flex justify-between pt-1 border-t border-amber-100">
                <span className="text-amber-900">Total Bayar</span>
                <span className="text-amber-700">Rp {grandTotal.toLocaleString('id-ID')}</span>
              </div>
            </div>
          </div>
        </div>

        {/* Submit */}
        <div className="px-5 pb-8 pt-2">
          <button
            type="submit"
            disabled={processing}
            className="w-full py-4 bg-gradient-to-r from-amber-600 to-amber-500 text-white rounded-2xl shadow-lg shadow-amber-200 disabled:opacity-60 flex items-center justify-center gap-2 text-base"
          >
            {processing ? (
              <>
                <div className="w-5 h-5 border-2 border-white/40 border-t-white rounded-full animate-spin" />
                Memproses Pesanan...
              </>
            ) : (
              <>🍞 Buat Pesanan Sekarang</>
            )}
          </button>
        </div>
      </form>
    </div>
  );
}
