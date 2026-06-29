import { useState } from 'react';
import { useNavigate } from 'react-router';
import {
  User, Phone, MapPin, Mail, LogOut, ChevronRight,
  ShoppingBag, Heart, Bell, Shield, HelpCircle, Star,
  Edit3, X, ChevronDown, Package, Clock, CheckCircle,
  BellOff, Lock, Trash2, ChevronUp
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { PRODUCTS } from '../data/products';
import { toast } from 'sonner';

type PanelType = 'orders' | 'favorites' | 'notifications' | 'privacy' | 'faq' | null;

const MOCK_ORDERS = [
  { id: 'BS240010', date: '24 Mei 2026', items: 'Croissant Mentega ×2', total: 50000, status: 'selesai' },
  { id: 'BS240008', date: '20 Mei 2026', items: 'Cinnamon Roll ×1, Pain au Chocolat ×2', total: 88000, status: 'selesai' },
  { id: 'BS240005', date: '15 Mei 2026', items: 'Sourdough Artisan ×1', total: 45000, status: 'selesai' },
];

const MOCK_NOTIFICATIONS = [
  { id: 1, icon: '🎉', title: 'Promo Hari Ini!', body: 'Diskon 20% untuk semua produk Pastri. Berlaku hari ini saja!', time: '08:00', unread: true },
  { id: 2, icon: '✅', title: 'Pesanan Selesai', body: 'Pesanan #BS240010 telah selesai dan siap diambil/dikirim.', time: 'Kemarin', unread: true },
  { id: 3, icon: '🚚', title: 'Dalam Pengiriman', body: 'Pesanan #BS240008 sedang dalam perjalanan ke alamat Anda.', time: '20 Mei', unread: false },
  { id: 4, icon: '🍞', title: 'Produk Baru!', body: 'Roti Keju Tarik varian baru sudah tersedia. Coba sekarang!', time: '18 Mei', unread: false },
];

const FAQ_ITEMS = [
  { q: 'Berapa lama waktu pengiriman?', a: 'Estimasi pengiriman 1–2 jam untuk area dalam kota. Pesanan dilakukan sebelum pukul 16.00.' },
  { q: 'Apakah bisa pesan untuk besok?', a: 'Bisa! Kamu bisa pre-order untuk hari berikutnya melalui menu Checkout dan memilih jadwal pengiriman.' },
  { q: 'Bagaimana cara membatalkan pesanan?', a: 'Pembatalan bisa dilakukan dalam 10 menit setelah pemesanan. Hubungi kami melalui WhatsApp di nomor yang tertera.' },
  { q: 'Metode pembayaran apa saja yang tersedia?', a: 'Kami menerima Transfer Bank (BCA, Mandiri, BNI, BRI), E-Wallet (GoPay, OVO, Dana), dan Bayar di Tempat (COD/Pickup).' },
  { q: 'Apakah produk bisa dikembalikan?', a: 'Karena sifat produk makanan segar, pengembalian hanya bisa dilakukan jika ada kerusakan produk. Hubungi kami dalam 2 jam.' },
];

function BottomSheet({ title, onClose, children }: { title: string; onClose: () => void; children: React.ReactNode }) {
  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center">
      <div className="absolute inset-0 bg-black/40" onClick={onClose} />
      <div className="relative bg-white w-full max-w-[390px] rounded-t-3xl max-h-[85vh] flex flex-col shadow-2xl">
        <div className="flex items-center justify-between px-5 pt-5 pb-4 border-b border-amber-50 flex-shrink-0">
          <h3 className="text-amber-900">{title}</h3>
          <button onClick={onClose} className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center hover:bg-amber-100 transition">
            <X className="w-4 h-4 text-amber-600" />
          </button>
        </div>
        <div className="overflow-y-auto flex-1 pb-6">
          {children}
        </div>
      </div>
    </div>
  );
}

export default function ProfilePage() {
  const { user, logout, updateProfile } = useAuth();
  const navigate = useNavigate();
  const [editing, setEditing] = useState(false);
  const [name, setName] = useState(user?.name || '');
  const [phone, setPhone] = useState(user?.phone || '');
  const [address, setAddress] = useState(user?.address || '');
  const [activePanel, setActivePanel] = useState<PanelType>(null);
  const [favorites, setFavorites] = useState<number[]>([1, 3, 7]);
  const [expandedFaq, setExpandedFaq] = useState<number | null>(null);
  const [notifSettings, setNotifSettings] = useState({ promo: true, order: true, new: false });

  const handleSave = () => {
    updateProfile({ name, phone, address });
    setEditing(false);
    toast.success('Profil berhasil diperbarui');
  };

  const handleCancelEdit = () => {
    setName(user?.name || '');
    setPhone(user?.phone || '');
    setAddress(user?.address || '');
    setEditing(false);
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const removeFavorite = (id: number) => {
    setFavorites((prev) => prev.filter((f) => f !== id));
    toast.success('Dihapus dari favorit');
  };

  const MENU_ITEMS = [
    { icon: ShoppingBag, label: 'Riwayat Pesanan', badge: `${MOCK_ORDERS.length}`, panel: 'orders' as PanelType },
    { icon: Heart, label: 'Favorit Saya', badge: `${favorites.length}`, panel: 'favorites' as PanelType },
    { icon: Bell, label: 'Notifikasi', badge: `${MOCK_NOTIFICATIONS.filter(n => n.unread).length}`, panel: 'notifications' as PanelType },
    { icon: Shield, label: 'Privasi & Keamanan', badge: '', panel: 'privacy' as PanelType },
    { icon: HelpCircle, label: 'Bantuan & FAQ', badge: '', panel: 'faq' as PanelType },
  ];

  const favoriteProducts = PRODUCTS.filter((p) => favorites.includes(p.id));

  return (
    <div className="flex flex-col">
      {/* Header */}
      <div className="bg-gradient-to-br from-amber-700 to-amber-500 px-5 pt-12 pb-8">
        <div className="flex flex-col items-center">
          <div className="w-20 h-20 bg-white/20 rounded-3xl flex items-center justify-center mb-3 shadow-lg border-2 border-white/30">
            <span className="text-4xl">👤</span>
          </div>
          <h2 className="text-white text-xl capitalize">{user?.name}</h2>
          <p className="text-amber-200 text-sm">{user?.email}</p>
          <div className="flex gap-3 mt-4">
            <button onClick={() => setActivePanel('orders')} className="bg-white/20 hover:bg-white/30 rounded-2xl px-5 py-2 text-center transition">
              <p className="text-white text-lg">{MOCK_ORDERS.length}</p>
              <p className="text-amber-200 text-xs">Pesanan</p>
            </button>
            <button onClick={() => setActivePanel('favorites')} className="bg-white/20 hover:bg-white/30 rounded-2xl px-5 py-2 text-center transition">
              <p className="text-white text-lg">{favorites.length}</p>
              <p className="text-amber-200 text-xs">Favorit</p>
            </button>
            <div className="bg-white/20 rounded-2xl px-5 py-2 text-center flex items-center gap-1">
              <Star className="w-4 h-4 text-amber-200 fill-amber-200" />
              <p className="text-white text-lg">4.9</p>
            </div>
          </div>
        </div>
      </div>

      <div className="px-5 -mt-3">
        {/* Profile Card */}
        <div className="bg-white rounded-2xl shadow-sm border border-amber-50 p-4 mb-4">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-amber-900">Informasi Akun</h3>
            <div className="flex gap-2">
              {editing && (
                <button onClick={handleCancelEdit} className="flex items-center gap-1 text-gray-400 text-sm">
                  <X className="w-3.5 h-3.5" />
                  Batal
                </button>
              )}
              <button
                onClick={() => editing ? handleSave() : setEditing(true)}
                className="flex items-center gap-1 text-amber-600 text-sm"
              >
                <Edit3 className="w-3.5 h-3.5" />
                {editing ? 'Simpan' : 'Edit'}
              </button>
            </div>
          </div>

          <div className="space-y-3">
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center flex-shrink-0">
                <User className="w-4 h-4 text-amber-500" />
              </div>
              {editing ? (
                <input
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  className="flex-1 text-sm text-amber-900 border-b-2 border-amber-300 pb-1 focus:outline-none focus:border-amber-500 bg-transparent"
                />
              ) : (
                <p className="text-amber-900 text-sm capitalize">{user?.name || '-'}</p>
              )}
            </div>

            <div className="flex items-center gap-3">
              <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center flex-shrink-0">
                <Mail className="w-4 h-4 text-amber-500" />
              </div>
              <p className="text-amber-700 text-sm">{user?.email}</p>
            </div>

            <div className="flex items-center gap-3">
              <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center flex-shrink-0">
                <Phone className="w-4 h-4 text-amber-500" />
              </div>
              {editing ? (
                <input
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="Tambah nomor HP"
                  className="flex-1 text-sm text-amber-900 border-b-2 border-amber-300 pb-1 focus:outline-none focus:border-amber-500 bg-transparent"
                />
              ) : (
                <p className={`text-sm ${user?.phone ? 'text-amber-900' : 'text-amber-300'}`}>
                  {user?.phone || 'Belum diisi'}
                </p>
              )}
            </div>

            <div className="flex items-center gap-3">
              <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center flex-shrink-0">
                <MapPin className="w-4 h-4 text-amber-500" />
              </div>
              {editing ? (
                <input
                  value={address}
                  onChange={(e) => setAddress(e.target.value)}
                  placeholder="Tambah alamat"
                  className="flex-1 text-sm text-amber-900 border-b-2 border-amber-300 pb-1 focus:outline-none focus:border-amber-500 bg-transparent"
                />
              ) : (
                <p className={`text-sm ${user?.address ? 'text-amber-900' : 'text-amber-300'}`}>
                  {user?.address || 'Belum diisi'}
                </p>
              )}
            </div>
          </div>
        </div>

        {/* Menu list */}
        <div className="bg-white rounded-2xl shadow-sm border border-amber-50 overflow-hidden mb-4">
          {MENU_ITEMS.map(({ icon: Icon, label, badge, panel }, i) => (
            <button
              key={label}
              onClick={() => setActivePanel(panel)}
              className={`w-full flex items-center gap-3 px-4 py-3.5 hover:bg-amber-50 active:bg-amber-100 transition-colors text-left ${i < MENU_ITEMS.length - 1 ? 'border-b border-amber-50' : ''}`}
            >
              <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center">
                <Icon className="w-4 h-4 text-amber-500" />
              </div>
              <span className="flex-1 text-amber-900 text-sm">{label}</span>
              {badge && Number(badge) > 0 && (
                <span className="w-5 h-5 bg-red-500 text-white text-[10px] rounded-full flex items-center justify-center">
                  {badge}
                </span>
              )}
              <ChevronRight className="w-4 h-4 text-amber-300" />
            </button>
          ))}
        </div>

        {/* Logout */}
        <button
          onClick={handleLogout}
          className="w-full flex items-center justify-center gap-2 py-4 bg-red-50 text-red-500 rounded-2xl border border-red-100 hover:bg-red-100 active:bg-red-200 transition-colors mb-6"
        >
          <LogOut className="w-4 h-4" />
          Keluar dari Akun
        </button>
      </div>

      {/* ── Riwayat Pesanan ── */}
      {activePanel === 'orders' && (
        <BottomSheet title="Riwayat Pesanan" onClose={() => setActivePanel(null)}>
          <div className="px-5 pt-4 space-y-3">
            {MOCK_ORDERS.map((order) => (
              <div key={order.id} className="bg-amber-50 rounded-2xl p-4 border border-amber-100">
                <div className="flex items-center justify-between mb-1.5">
                  <div className="flex items-center gap-2">
                    <Package className="w-4 h-4 text-amber-500" />
                    <span className="text-amber-800 text-sm">#{order.id}</span>
                  </div>
                  <span className="flex items-center gap-1 text-xs text-green-600 bg-green-50 px-2 py-0.5 rounded-full border border-green-100">
                    <CheckCircle className="w-3 h-3" />
                    Selesai
                  </span>
                </div>
                <p className="text-amber-700 text-sm mb-1">{order.items}</p>
                <div className="flex items-center justify-between">
                  <p className="text-amber-500 text-xs flex items-center gap-1">
                    <Clock className="w-3 h-3" />{order.date}
                  </p>
                  <p className="text-amber-700 text-sm">Rp {order.total.toLocaleString('id-ID')}</p>
                </div>
                <button
                  onClick={() => { setActivePanel(null); navigate('/app/home'); toast.success('Arahkan ke produk untuk pesan ulang'); }}
                  className="mt-2 w-full py-2 border border-amber-300 text-amber-600 rounded-xl text-xs hover:bg-amber-100 transition"
                >
                  Pesan Ulang
                </button>
              </div>
            ))}
          </div>
        </BottomSheet>
      )}

      {/* ── Favorit Saya ── */}
      {activePanel === 'favorites' && (
        <BottomSheet title="Favorit Saya" onClose={() => setActivePanel(null)}>
          <div className="px-5 pt-4">
            {favoriteProducts.length === 0 ? (
              <div className="text-center py-10">
                <Heart className="w-12 h-12 text-amber-200 mx-auto mb-3" />
                <p className="text-amber-600 text-sm">Belum ada produk favorit</p>
                <button onClick={() => { setActivePanel(null); navigate('/app/home'); }} className="mt-3 text-amber-500 text-sm underline">
                  Jelajahi Produk
                </button>
              </div>
            ) : (
              <div className="space-y-3">
                {favoriteProducts.map((p) => (
                  <div key={p.id} className="flex items-center gap-3 bg-amber-50 rounded-2xl p-3 border border-amber-100">
                    <img src={p.image} alt={p.name} className="w-14 h-14 object-cover rounded-xl flex-shrink-0" />
                    <div className="flex-1 min-w-0">
                      <p className="text-amber-900 text-sm line-clamp-1">{p.name}</p>
                      <p className="text-amber-500 text-xs">{p.category}</p>
                      <p className="text-amber-700 text-sm mt-0.5">Rp {p.price.toLocaleString('id-ID')}</p>
                    </div>
                    <div className="flex flex-col gap-1.5">
                      <button
                        onClick={() => { setActivePanel(null); navigate(`/app/product/${p.id}`); }}
                        className="text-xs text-white bg-amber-500 px-2.5 py-1.5 rounded-xl hover:bg-amber-600 transition"
                      >
                        Beli
                      </button>
                      <button
                        onClick={() => removeFavorite(p.id)}
                        className="text-xs text-red-400 bg-red-50 px-2.5 py-1.5 rounded-xl hover:bg-red-100 transition flex items-center gap-1 justify-center"
                      >
                        <Trash2 className="w-3 h-3" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </BottomSheet>
      )}

      {/* ── Notifikasi ── */}
      {activePanel === 'notifications' && (
        <BottomSheet title="Notifikasi" onClose={() => setActivePanel(null)}>
          <div className="px-5 pt-4 space-y-3">
            {/* Settings */}
            <div className="bg-amber-50 rounded-2xl p-4 border border-amber-100 space-y-3">
              <p className="text-amber-700 text-xs uppercase tracking-wide">Pengaturan Notifikasi</p>
              {[
                { key: 'promo', label: 'Promo & Diskon', icon: '🎉' },
                { key: 'order', label: 'Update Pesanan', icon: '📦' },
                { key: 'new', label: 'Produk Baru', icon: '✨' },
              ].map(({ key, label, icon }) => (
                <div key={key} className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <span>{icon}</span>
                    <span className="text-amber-900 text-sm">{label}</span>
                  </div>
                  <button
                    onClick={() => setNotifSettings(prev => ({ ...prev, [key]: !prev[key as keyof typeof prev] }))}
                    className={`w-11 h-6 rounded-full transition-colors relative ${notifSettings[key as keyof typeof notifSettings] ? 'bg-amber-500' : 'bg-gray-200'}`}
                  >
                    <div className={`absolute top-1 w-4 h-4 bg-white rounded-full shadow transition-all ${notifSettings[key as keyof typeof notifSettings] ? 'left-6' : 'left-1'}`} />
                  </button>
                </div>
              ))}
            </div>
            {/* Notification list */}
            {MOCK_NOTIFICATIONS.map((notif) => (
              <div key={notif.id} className={`rounded-2xl p-4 border flex gap-3 ${notif.unread ? 'bg-amber-50 border-amber-200' : 'bg-white border-amber-50'}`}>
                <span className="text-2xl flex-shrink-0">{notif.icon}</span>
                <div className="flex-1 min-w-0">
                  <div className="flex items-center justify-between mb-0.5">
                    <p className={`text-sm ${notif.unread ? 'text-amber-900' : 'text-gray-700'}`}>{notif.title}</p>
                    <span className="text-xs text-gray-400 flex-shrink-0 ml-2">{notif.time}</span>
                  </div>
                  <p className="text-xs text-gray-500 leading-relaxed">{notif.body}</p>
                </div>
                {notif.unread && <div className="w-2 h-2 bg-amber-500 rounded-full mt-1 flex-shrink-0" />}
              </div>
            ))}
          </div>
        </BottomSheet>
      )}

      {/* ── Privasi & Keamanan ── */}
      {activePanel === 'privacy' && (
        <BottomSheet title="Privasi & Keamanan" onClose={() => setActivePanel(null)}>
          <div className="px-5 pt-4 space-y-3">
            <div className="bg-white rounded-2xl border border-amber-100 overflow-hidden">
              <button
                onClick={() => toast.info('Fitur ubah password akan segera hadir')}
                className="w-full flex items-center gap-3 px-4 py-4 hover:bg-amber-50 transition border-b border-amber-50"
              >
                <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center">
                  <Lock className="w-4 h-4 text-amber-500" />
                </div>
                <span className="flex-1 text-amber-900 text-sm text-left">Ubah Password</span>
                <ChevronRight className="w-4 h-4 text-amber-300" />
              </button>
              <button
                onClick={() => toast.info('Fitur autentikasi 2 faktor akan segera hadir')}
                className="w-full flex items-center gap-3 px-4 py-4 hover:bg-amber-50 transition border-b border-amber-50"
              >
                <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center">
                  <Shield className="w-4 h-4 text-amber-500" />
                </div>
                <span className="flex-1 text-amber-900 text-sm text-left">Autentikasi 2 Faktor</span>
                <ChevronRight className="w-4 h-4 text-amber-300" />
              </button>
              <button
                onClick={() => toast.info('Fitur kelola data akan segera hadir')}
                className="w-full flex items-center gap-3 px-4 py-4 hover:bg-amber-50 transition"
              >
                <div className="w-8 h-8 bg-amber-50 rounded-xl flex items-center justify-center">
                  <BellOff className="w-4 h-4 text-amber-500" />
                </div>
                <span className="flex-1 text-amber-900 text-sm text-left">Kelola Data Pribadi</span>
                <ChevronRight className="w-4 h-4 text-amber-300" />
              </button>
            </div>
            <button
              onClick={() => { toast.error('Akun berhasil dihapus (demo)'); logout(); navigate('/'); }}
              className="w-full py-3.5 bg-red-50 border border-red-200 text-red-500 rounded-2xl text-sm hover:bg-red-100 transition flex items-center justify-center gap-2"
            >
              <Trash2 className="w-4 h-4" />
              Hapus Akun Saya
            </button>
          </div>
        </BottomSheet>
      )}

      {/* ── Bantuan & FAQ ── */}
      {activePanel === 'faq' && (
        <BottomSheet title="Bantuan & FAQ" onClose={() => setActivePanel(null)}>
          <div className="px-5 pt-4 space-y-2">
            <div className="bg-amber-50 rounded-2xl p-4 border border-amber-100 mb-3">
              <p className="text-amber-800 text-sm">Butuh bantuan lebih lanjut?</p>
              <button
                onClick={() => toast.success('Menghubungkan ke WhatsApp Support...')}
                className="mt-2 w-full py-2.5 bg-green-500 text-white rounded-xl text-sm hover:bg-green-600 transition flex items-center justify-center gap-2"
              >
                <span>💬</span> Chat via WhatsApp
              </button>
            </div>
            {FAQ_ITEMS.map((item, i) => (
              <div key={i} className="bg-white rounded-2xl border border-amber-100 overflow-hidden">
                <button
                  onClick={() => setExpandedFaq(expandedFaq === i ? null : i)}
                  className="w-full flex items-center justify-between px-4 py-3.5 text-left hover:bg-amber-50 transition"
                >
                  <span className="text-amber-900 text-sm pr-3">{item.q}</span>
                  {expandedFaq === i
                    ? <ChevronUp className="w-4 h-4 text-amber-400 flex-shrink-0" />
                    : <ChevronDown className="w-4 h-4 text-amber-400 flex-shrink-0" />
                  }
                </button>
                {expandedFaq === i && (
                  <div className="px-4 pb-4 border-t border-amber-50">
                    <p className="text-amber-600/80 text-sm leading-relaxed pt-3">{item.a}</p>
                  </div>
                )}
              </div>
            ))}
          </div>
        </BottomSheet>
      )}
    </div>
  );
}
