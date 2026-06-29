import { useState, useRef } from 'react';
import { useNavigate } from 'react-router';
import {
  Package, ClipboardList, TrendingUp, LogOut, Plus, X, Upload,
  ChevronDown, Check, Clock, Loader, AlertCircle, ChevronRight,
  LayoutDashboard, Search, Edit2, Trash2, ImagePlus
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { PRODUCTS as INITIAL_PRODUCTS, CATEGORIES } from '../data/products';
import { Product } from '../context/CartContext';
import { toast } from 'sonner';

type OrderStatus = 'pending' | 'diproses' | 'selesai';

interface Order {
  id: string;
  customer: string;
  items: string;
  total: number;
  status: OrderStatus;
  time: string;
  method: string;
}

const MOCK_ORDERS: Order[] = [
  { id: 'BS20260608-001', customer: 'Rina Maulida', items: 'Croissant Butter Original ×2, Pain au Chocolat ×1', total: 68000, status: 'pending', time: '08:12', method: 'Home Delivery' },
  { id: 'BS20260608-002', customer: 'Budi Santoso', items: 'Roti Sobek Cokelat Keju ×1, Roti Bun Kopi ×2', total: 42000, status: 'diproses', time: '08:45', method: 'Pick-up' },
  { id: 'BS20260608-003', customer: 'Dewi Putri', items: 'Cinnamon Roll Kayu Manis ×3, Roti Donat ×2', total: 61000, status: 'diproses', time: '09:03', method: 'Home Delivery' },
  { id: 'BS20260608-004', customer: 'Arif Rahman', items: 'Pizza Mini Sosis Keju ×3, Roti Sosis ×2', total: 68000, status: 'selesai', time: '07:30', method: 'Pick-up' },
  { id: 'BS20260608-005', customer: 'Siti Rahayu', items: 'Slice Cake Black Forest ×2, Cupcake Vanila ×3', total: 86000, status: 'pending', time: '09:20', method: 'Home Delivery' },
  { id: 'BS20260608-006', customer: 'Ahmad Fauzi', items: 'Croissant Almond Premium ×2, Danish Pastry Blueberry ×1', total: 70000, status: 'selesai', time: '07:15', method: 'Pick-up' },
  { id: 'BS20260608-007', customer: 'Linda Wati', items: 'Tiramisu Espresso ×1, Matcha Crepe ×1', total: 57000, status: 'pending', time: '09:45', method: 'Home Delivery' },
];

const STATUS_CONFIG: Record<OrderStatus, { label: string; color: string; bg: string; icon: typeof Check }> = {
  pending: { label: 'Menunggu', color: 'text-yellow-600', bg: 'bg-yellow-50 border-yellow-200', icon: Clock },
  diproses: { label: 'Diproses', color: 'text-blue-600', bg: 'bg-blue-50 border-blue-200', icon: Loader },
  selesai: { label: 'Selesai', color: 'text-green-600', bg: 'bg-green-50 border-green-200', icon: Check },
};

interface ProductForm {
  name: string;
  category: string;
  variant: string;
  price: string;
  stock: string;
  imagePreview: string;
}

const EMPTY_FORM: ProductForm = {
  name: '', category: CATEGORIES[1], variant: '', price: '', stock: '', imagePreview: '',
};

export default function AdminDashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState<'dashboard' | 'products' | 'orders'>('dashboard');
  const [orders, setOrders] = useState<Order[]>(MOCK_ORDERS);
  const [products, setProducts] = useState<Product[]>(INITIAL_PRODUCTS);
  const [showProductModal, setShowProductModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Product | null>(null);
  const [orderSearch, setOrderSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<OrderStatus | 'semua'>('semua');
  const [form, setForm] = useState<ProductForm>(EMPTY_FORM);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleLogout = () => { logout(); navigate('/'); };

  const handleStatusChange = (orderId: string, status: OrderStatus) => {
    setOrders((prev) => prev.map((o) => o.id === orderId ? { ...o, status } : o));
    toast.success('Status pesanan diperbarui');
  };

  const openAddModal = () => {
    setEditingProduct(null);
    setForm(EMPTY_FORM);
    setShowProductModal(true);
  };

  const openEditModal = (product: Product) => {
    setEditingProduct(product);
    setForm({
      name: product.name,
      category: product.category,
      variant: product.variants.join(', '),
      price: String(product.price),
      stock: String(product.stock),
      imagePreview: product.image,
    });
    setShowProductModal(true);
  };

  const handleSaveProduct = (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.name || !form.price || !form.stock) {
      toast.error('Lengkapi semua field yang diperlukan');
      return;
    }
    if (editingProduct) {
      setProducts((prev) => prev.map((p) =>
        p.id === editingProduct.id
          ? { ...p, name: form.name, category: form.category, variants: form.variant.split(',').map(v => v.trim()).filter(Boolean), price: Number(form.price), stock: Number(form.stock), image: form.imagePreview || p.image }
          : p
      ));
      toast.success(`Produk "${form.name}" berhasil diperbarui`);
    } else {
      const newProduct: Product = {
        id: Date.now(),
        name: form.name,
        category: form.category,
        variants: form.variant.split(',').map(v => v.trim()).filter(Boolean).length > 0
          ? form.variant.split(',').map(v => v.trim()).filter(Boolean)
          : ['Original'],
        price: Number(form.price),
        image: form.imagePreview || 'https://images.unsplash.com/photo-1608198093002-ad4e005484ec?w=400&q=80',
        description: `${form.name} dari BreadSweet`,
        rating: 5.0,
        sold: 0,
        stock: Number(form.stock),
        isNew: true,
      };
      setProducts((prev) => [newProduct, ...prev]);
      toast.success(`Produk "${form.name}" berhasil ditambahkan!`);
    }
    setShowProductModal(false);
    setForm(EMPTY_FORM);
    setEditingProduct(null);
  };

  const confirmDelete = () => {
    if (!deleteTarget) return;
    setProducts((prev) => prev.filter((p) => p.id !== deleteTarget.id));
    toast.success(`Produk "${deleteTarget.name}" dihapus`);
    setDeleteTarget(null);
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (ev) => {
      setForm((prev) => ({ ...prev, imagePreview: ev.target?.result as string }));
    };
    reader.readAsDataURL(file);
  };

  const filteredOrders = orders.filter((o) => {
    const matchSearch = orderSearch === '' || o.customer.toLowerCase().includes(orderSearch.toLowerCase()) || o.id.includes(orderSearch);
    const matchStatus = statusFilter === 'semua' || o.status === statusFilter;
    return matchSearch && matchStatus;
  });

  const pendingCount = orders.filter((o) => o.status === 'pending').length;
  const diprosesCount = orders.filter((o) => o.status === 'diproses').length;

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="w-full max-w-[390px] min-h-screen bg-gray-50 flex flex-col">
        {/* Header */}
        <div className="bg-gradient-to-br from-amber-800 to-amber-600 px-5 pt-12 pb-5">
          <div className="flex justify-between items-start mb-4">
            <div>
              <div className="flex items-center gap-2 mb-1">
                <span className="text-lg">🛡️</span>
                <span className="text-amber-200 text-xs uppercase tracking-widest">Admin Panel</span>
              </div>
              <h1 className="text-white text-xl">BreadSweet</h1>
              <p className="text-amber-300 text-xs mt-0.5">{user?.name}</p>
            </div>
            <button
              onClick={handleLogout}
              className="flex items-center gap-1.5 bg-white/15 hover:bg-white/25 text-white text-xs px-3 py-2 rounded-xl transition"
            >
              <LogOut className="w-3.5 h-3.5" />
              Keluar
            </button>
          </div>
          <div className="flex bg-white/15 rounded-2xl p-1 gap-1">
            {[
              { id: 'dashboard', icon: LayoutDashboard, label: 'Dashboard' },
              { id: 'products', icon: Package, label: 'Produk' },
              { id: 'orders', icon: ClipboardList, label: 'Pesanan' },
            ].map(({ id, icon: Icon, label }) => (
              <button
                key={id}
                onClick={() => setActiveTab(id as typeof activeTab)}
                className={`flex-1 flex items-center justify-center gap-1.5 py-2 rounded-xl text-xs transition-all ${activeTab === id ? 'bg-white text-amber-700 shadow-sm' : 'text-white/80'}`}
              >
                <Icon className="w-3.5 h-3.5" />
                {label}
              </button>
            ))}
          </div>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto">
          {/* ── Dashboard Tab ── */}
          {activeTab === 'dashboard' && (
            <div className="px-5 pt-5 space-y-4 pb-6">
              <h2 className="text-gray-800">Ringkasan Hari Ini</h2>
              <div className="grid grid-cols-2 gap-3">
                <div className="bg-white rounded-2xl p-4 shadow-sm border border-gray-100">
                  <TrendingUp className="w-5 h-5 text-green-500 mb-2" />
                  <p className="text-gray-500 text-xs">Pendapatan</p>
                  <p className="text-gray-800 text-lg">Rp 499K</p>
                </div>
                <div className="bg-white rounded-2xl p-4 shadow-sm border border-gray-100">
                  <ClipboardList className="w-5 h-5 text-blue-500 mb-2" />
                  <p className="text-gray-500 text-xs">Total Pesanan</p>
                  <p className="text-gray-800 text-lg">{orders.length} pesanan</p>
                </div>
                <div className="bg-yellow-50 rounded-2xl p-4 shadow-sm border border-yellow-100">
                  <AlertCircle className="w-5 h-5 text-yellow-500 mb-2" />
                  <p className="text-gray-500 text-xs">Menunggu</p>
                  <p className="text-yellow-700 text-lg">{pendingCount} pesanan</p>
                </div>
                <div className="bg-blue-50 rounded-2xl p-4 shadow-sm border border-blue-100">
                  <Loader className="w-5 h-5 text-blue-500 mb-2" />
                  <p className="text-gray-500 text-xs">Diproses</p>
                  <p className="text-blue-700 text-lg">{diprosesCount} pesanan</p>
                </div>
              </div>
              <h3 className="text-gray-700 text-sm">Aksi Cepat</h3>
              <div className="space-y-2">
                <button
                  onClick={() => { setActiveTab('products'); openAddModal(); }}
                  className="w-full bg-amber-600 text-white rounded-2xl px-4 py-3.5 flex items-center gap-3 shadow-md shadow-amber-200 hover:bg-amber-700 transition"
                >
                  <Plus className="w-5 h-5" />
                  <span>Tambah Produk Baru</span>
                  <ChevronRight className="w-4 h-4 ml-auto" />
                </button>
                <button
                  onClick={() => setActiveTab('orders')}
                  className="w-full bg-white border border-gray-200 text-gray-700 rounded-2xl px-4 py-3.5 flex items-center gap-3 hover:bg-gray-50 transition"
                >
                  <ClipboardList className="w-5 h-5 text-gray-400" />
                  <span>Lihat Pesanan Aktif</span>
                  {pendingCount > 0 && (
                    <span className="ml-auto bg-yellow-400 text-white text-xs px-2 py-0.5 rounded-full">{pendingCount} baru</span>
                  )}
                </button>
              </div>
              <h3 className="text-gray-700 text-sm">Produk Terlaris</h3>
              <div className="space-y-2">
                {products.slice(0, 3).map((p, i) => (
                  <div key={p.id} className="bg-white rounded-xl p-3 flex items-center gap-3 shadow-sm border border-gray-50">
                    <span className="text-gray-400 w-5 text-center text-sm">#{i + 1}</span>
                    <img src={p.image} alt={p.name} className="w-10 h-10 object-cover rounded-xl" />
                    <div className="flex-1 min-w-0">
                      <p className="text-gray-800 text-sm line-clamp-1">{p.name}</p>
                      <p className="text-gray-400 text-xs">{p.sold} terjual</p>
                    </div>
                    <p className="text-amber-600 text-sm flex-shrink-0">Rp {p.price.toLocaleString('id-ID')}</p>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* ── Products Tab ── */}
          {activeTab === 'products' && (
            <div className="px-5 pt-5 pb-6">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-gray-800">Manajemen Produk <span className="text-gray-400 text-sm">({products.length})</span></h2>
                <button
                  onClick={openAddModal}
                  className="flex items-center gap-1 bg-amber-600 text-white text-xs px-3 py-2 rounded-xl hover:bg-amber-700 transition"
                >
                  <Plus className="w-3.5 h-3.5" />
                  Tambah
                </button>
              </div>
              <div className="space-y-3">
                {products.map((product) => (
                  <div key={product.id} className="bg-white rounded-2xl p-3 flex gap-3 shadow-sm border border-gray-100">
                    <img src={product.image} alt={product.name} className="w-16 h-16 object-cover rounded-xl flex-shrink-0" />
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-1">
                        <p className="text-gray-800 text-sm line-clamp-1">{product.name}</p>
                        <span className={`text-[10px] px-2 py-0.5 rounded-full flex-shrink-0 ${product.stock > 10 ? 'bg-green-50 text-green-600' : 'bg-red-50 text-red-500'}`}>
                          Stok: {product.stock}
                        </span>
                      </div>
                      <p className="text-gray-400 text-xs mt-0.5">{product.category} · {product.variants.join(', ')}</p>
                      <div className="flex items-center justify-between mt-1.5">
                        <p className="text-amber-600 text-sm">Rp {product.price.toLocaleString('id-ID')}</p>
                        <div className="flex gap-1.5">
                          <button
                            onClick={() => openEditModal(product)}
                            className="text-xs text-amber-600 border border-amber-200 px-2.5 py-1 rounded-lg hover:bg-amber-50 transition flex items-center gap-1"
                          >
                            <Edit2 className="w-3 h-3" />Edit
                          </button>
                          <button
                            onClick={() => setDeleteTarget(product)}
                            className="text-xs text-red-400 border border-red-100 px-2.5 py-1 rounded-lg hover:bg-red-50 transition flex items-center gap-1"
                          >
                            <Trash2 className="w-3 h-3" />Hapus
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* ── Orders Tab ── */}
          {activeTab === 'orders' && (
            <div className="px-5 pt-5 pb-6">
              <h2 className="text-gray-800 mb-4">Pesanan Aktif</h2>
              <div className="relative mb-3">
                <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <input
                  value={orderSearch}
                  onChange={(e) => setOrderSearch(e.target.value)}
                  placeholder="Cari nama atau ID pesanan..."
                  className="w-full pl-10 pr-4 py-3 bg-white border border-gray-200 rounded-xl text-sm text-gray-700 focus:outline-none focus:ring-2 focus:ring-amber-300"
                />
              </div>
              <div className="flex gap-2 mb-4 overflow-x-auto pb-1">
                {(['semua', 'pending', 'diproses', 'selesai'] as const).map((s) => (
                  <button
                    key={s}
                    onClick={() => setStatusFilter(s)}
                    className={`flex-shrink-0 text-xs px-3 py-1.5 rounded-xl transition-all ${statusFilter === s ? 'bg-amber-600 text-white' : 'bg-white text-gray-600 border border-gray-200'}`}
                  >
                    {s === 'semua' ? 'Semua' : s === 'pending' ? 'Menunggu' : s === 'diproses' ? 'Diproses' : 'Selesai'}
                    {s !== 'semua' && <span className="ml-1">({orders.filter(o => o.status === s).length})</span>}
                  </button>
                ))}
              </div>
              <div className="space-y-3">
                {filteredOrders.length === 0 ? (
                  <div className="text-center py-10">
                    <p className="text-gray-400 text-sm">Tidak ada pesanan ditemukan</p>
                  </div>
                ) : (
                  filteredOrders.map((order) => {
                    const status = STATUS_CONFIG[order.status];
                    const StatusIcon = status.icon;
                    return (
                      <div key={order.id} className="bg-white rounded-2xl p-4 shadow-sm border border-gray-100">
                        <div className="flex items-start justify-between mb-2">
                          <div>
                            <p className="text-gray-800 text-sm">{order.customer}</p>
                            <p className="text-gray-400 text-xs">#{order.id} · {order.time} · {order.method}</p>
                          </div>
                          <span className={`flex items-center gap-1 text-xs px-2.5 py-1 rounded-xl border ${status.bg} ${status.color}`}>
                            <StatusIcon className="w-3 h-3" />
                            {status.label}
                          </span>
                        </div>
                        <p className="text-gray-500 text-xs mb-3 line-clamp-2">{order.items}</p>
                        <div className="flex items-center justify-between">
                          <p className="text-amber-700 text-sm">Rp {order.total.toLocaleString('id-ID')}</p>
                          <div className="flex gap-1.5">
                            {order.status === 'pending' && (
                              <button onClick={() => handleStatusChange(order.id, 'diproses')} className="text-xs text-white bg-blue-500 hover:bg-blue-600 px-3 py-1.5 rounded-xl transition">
                                Proses
                              </button>
                            )}
                            {order.status === 'diproses' && (
                              <button onClick={() => handleStatusChange(order.id, 'selesai')} className="text-xs text-white bg-green-500 hover:bg-green-600 px-3 py-1.5 rounded-xl transition">
                                Selesai
                              </button>
                            )}
                            {order.status === 'selesai' && (
                              <span className="text-xs text-green-500 px-3 py-1.5 flex items-center gap-1"><Check className="w-3 h-3" />Selesai</span>
                            )}
                          </div>
                        </div>
                      </div>
                    );
                  })
                )}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* ── Add/Edit Product Modal ── */}
      {showProductModal && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-end justify-center">
          <div className="bg-white w-full max-w-[390px] rounded-t-3xl p-6 max-h-[92vh] overflow-y-auto">
            <div className="flex items-center justify-between mb-5">
              <h3 className="text-gray-800 text-lg">{editingProduct ? 'Edit Produk' : 'Tambah Produk Baru'}</h3>
              <button onClick={() => { setShowProductModal(false); setEditingProduct(null); setForm(EMPTY_FORM); }}>
                <X className="w-5 h-5 text-gray-400" />
              </button>
            </div>
            <form onSubmit={handleSaveProduct} className="space-y-4">
              {/* Photo upload */}
              <div>
                <input type="file" accept="image/*" ref={fileInputRef} onChange={handleImageUpload} className="hidden" />
                {form.imagePreview ? (
                  <div className="relative rounded-2xl overflow-hidden h-36">
                    <img src={form.imagePreview} alt="preview" className="w-full h-full object-cover" />
                    <button
                      type="button"
                      onClick={() => setForm((p) => ({ ...p, imagePreview: '' }))}
                      className="absolute top-2 right-2 w-7 h-7 bg-black/50 rounded-full flex items-center justify-center"
                    >
                      <X className="w-4 h-4 text-white" />
                    </button>
                    <button
                      type="button"
                      onClick={() => fileInputRef.current?.click()}
                      className="absolute bottom-2 right-2 flex items-center gap-1 bg-black/50 text-white text-xs px-2.5 py-1.5 rounded-xl"
                    >
                      <ImagePlus className="w-3.5 h-3.5" />Ganti
                    </button>
                  </div>
                ) : (
                  <button
                    type="button"
                    onClick={() => fileInputRef.current?.click()}
                    className="w-full border-2 border-dashed border-amber-200 rounded-2xl p-6 flex flex-col items-center gap-2 bg-amber-50 hover:bg-amber-100 transition"
                  >
                    <Upload className="w-8 h-8 text-amber-400" />
                    <p className="text-amber-600 text-sm">Unggah Foto Produk</p>
                    <p className="text-amber-400 text-xs">JPG, PNG, max 5MB</p>
                  </button>
                )}
              </div>

              <div>
                <label className="text-xs text-gray-600 uppercase tracking-wide">Nama Roti *</label>
                <input
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  className="w-full mt-1.5 px-3 py-3 bg-gray-50 border border-gray-200 rounded-xl text-sm text-gray-800 focus:outline-none focus:ring-2 focus:ring-amber-300"
                  placeholder="cth. Croissant Mentega Premium"
                  required
                />
              </div>

              <div>
                <label className="text-xs text-gray-600 uppercase tracking-wide">Kategori *</label>
                <div className="relative mt-1.5">
                  <select
                    value={form.category}
                    onChange={(e) => setForm({ ...form, category: e.target.value })}
                    className="w-full px-3 py-3 bg-gray-50 border border-gray-200 rounded-xl text-sm text-gray-800 focus:outline-none focus:ring-2 focus:ring-amber-300 appearance-none"
                  >
                    {CATEGORIES.filter((c) => c !== 'Semua').map((c) => (
                      <option key={c} value={c}>{c}</option>
                    ))}
                  </select>
                  <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none" />
                </div>
              </div>

              <div>
                <label className="text-xs text-gray-600 uppercase tracking-wide">Varian Rasa</label>
                <input
                  value={form.variant}
                  onChange={(e) => setForm({ ...form, variant: e.target.value })}
                  className="w-full mt-1.5 px-3 py-3 bg-gray-50 border border-gray-200 rounded-xl text-sm text-gray-800 focus:outline-none focus:ring-2 focus:ring-amber-300"
                  placeholder="cth. Original, Cokelat, Keju (pisah koma)"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="text-xs text-gray-600 uppercase tracking-wide">Harga (Rp) *</label>
                  <input
                    type="number"
                    value={form.price}
                    onChange={(e) => setForm({ ...form, price: e.target.value })}
                    className="w-full mt-1.5 px-3 py-3 bg-gray-50 border border-gray-200 rounded-xl text-sm text-gray-800 focus:outline-none focus:ring-2 focus:ring-amber-300"
                    placeholder="25000"
                    required
                  />
                </div>
                <div>
                  <label className="text-xs text-gray-600 uppercase tracking-wide">Stok *</label>
                  <input
                    type="number"
                    value={form.stock}
                    onChange={(e) => setForm({ ...form, stock: e.target.value })}
                    className="w-full mt-1.5 px-3 py-3 bg-gray-50 border border-gray-200 rounded-xl text-sm text-gray-800 focus:outline-none focus:ring-2 focus:ring-amber-300"
                    placeholder="20"
                    required
                  />
                </div>
              </div>

              <button type="submit" className="w-full py-4 bg-gradient-to-r from-amber-600 to-amber-500 text-white rounded-xl shadow-md shadow-amber-200 hover:from-amber-700 hover:to-amber-600 transition">
                {editingProduct ? 'Simpan Perubahan' : 'Tambah Produk'}
              </button>
            </form>
          </div>
        </div>
      )}

      {/* ── Delete Confirmation ── */}
      {deleteTarget && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center px-6">
          <div className="bg-white rounded-3xl p-6 w-full max-w-[320px] shadow-2xl">
            <div className="w-14 h-14 bg-red-50 rounded-2xl flex items-center justify-center mx-auto mb-4">
              <Trash2 className="w-7 h-7 text-red-500" />
            </div>
            <h3 className="text-gray-800 text-center mb-1">Hapus Produk?</h3>
            <p className="text-gray-500 text-sm text-center mb-5">
              "{deleteTarget.name}" akan dihapus secara permanen dan tidak dapat dipulihkan.
            </p>
            <div className="flex gap-3">
              <button onClick={() => setDeleteTarget(null)} className="flex-1 py-3 border border-gray-200 text-gray-600 rounded-xl hover:bg-gray-50 transition text-sm">
                Batal
              </button>
              <button onClick={confirmDelete} className="flex-1 py-3 bg-red-500 text-white rounded-xl hover:bg-red-600 transition text-sm">
                Hapus
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
