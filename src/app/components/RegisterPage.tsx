import { useState } from 'react';
import { useNavigate, Link } from 'react-router';
import { useAuth } from '../context/AuthContext';
import { Mail, Lock, User, Eye, EyeOff, ArrowLeft, ChevronRight } from 'lucide-react';

export default function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    if (!name || !email || !password || !confirmPassword) {
      setError('Semua kolom wajib diisi');
      return;
    }
    if (password.length < 6) {
      setError('Password minimal 6 karakter');
      return;
    }
    if (password !== confirmPassword) {
      setError('Password tidak cocok');
      return;
    }
    setLoading(true);
    await new Promise((r) => setTimeout(r, 700));
    const success = register(email, password, name);
    setLoading(false);
    if (success) {
      navigate('/app/home');
    } else {
      setError('Pendaftaran gagal, coba lagi');
    }
  };

  return (
    <div className="min-h-screen bg-amber-50 flex items-center justify-center p-4">
      <div className="w-full max-w-[390px] min-h-screen bg-white flex flex-col">
        {/* Header */}
        <div className="bg-gradient-to-br from-amber-700 to-amber-500 px-6 pt-12 pb-10 relative overflow-hidden">
          <div className="absolute -top-6 -right-6 w-32 h-32 bg-white/10 rounded-full" />
          <div className="absolute -bottom-8 -left-8 w-44 h-44 bg-white/10 rounded-full" />
          <button
            onClick={() => navigate('/')}
            className="relative z-10 mb-6 flex items-center gap-1 text-amber-100 hover:text-white transition"
          >
            <ArrowLeft className="w-5 h-5" />
            <span className="text-sm">Kembali</span>
          </button>
          <div className="relative z-10">
            <div className="w-14 h-14 bg-white rounded-2xl flex items-center justify-center mb-4 shadow-lg">
              <span className="text-2xl">🍞</span>
            </div>
            <h1 className="text-white text-2xl">Buat Akun Baru</h1>
            <p className="text-amber-100 text-sm mt-1">Daftar gratis, nikmati kemudahan belanja</p>
          </div>
        </div>

        {/* Form */}
        <div className="flex-1 px-6 pt-7 pb-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs text-amber-800 mb-1.5 uppercase tracking-wide">Nama Lengkap</label>
              <div className="relative">
                <User className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-amber-400" />
                <input
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  className="w-full pl-10 pr-4 py-3.5 bg-amber-50 border border-amber-200 rounded-xl text-amber-900 placeholder:text-amber-300 focus:outline-none focus:ring-2 focus:ring-amber-400 transition"
                  placeholder="Nama Anda"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs text-amber-800 mb-1.5 uppercase tracking-wide">Email</label>
              <div className="relative">
                <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-amber-400" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full pl-10 pr-4 py-3.5 bg-amber-50 border border-amber-200 rounded-xl text-amber-900 placeholder:text-amber-300 focus:outline-none focus:ring-2 focus:ring-amber-400 transition"
                  placeholder="nama@email.com"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs text-amber-800 mb-1.5 uppercase tracking-wide">Password</label>
              <div className="relative">
                <Lock className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-amber-400" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full pl-10 pr-12 py-3.5 bg-amber-50 border border-amber-200 rounded-xl text-amber-900 placeholder:text-amber-300 focus:outline-none focus:ring-2 focus:ring-amber-400 transition"
                  placeholder="Min. 6 karakter"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3.5 top-1/2 -translate-y-1/2 text-amber-400 hover:text-amber-600"
                >
                  {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
            </div>

            <div>
              <label className="block text-xs text-amber-800 mb-1.5 uppercase tracking-wide">Konfirmasi Password</label>
              <div className="relative">
                <Lock className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-amber-400" />
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className="w-full pl-10 pr-4 py-3.5 bg-amber-50 border border-amber-200 rounded-xl text-amber-900 placeholder:text-amber-300 focus:outline-none focus:ring-2 focus:ring-amber-400 transition"
                  placeholder="Ulangi password"
                />
              </div>
            </div>

            {error && (
              <div className="bg-red-50 border border-red-200 rounded-xl px-4 py-3 text-red-600 text-sm">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full py-4 bg-gradient-to-r from-amber-600 to-amber-500 hover:from-amber-700 hover:to-amber-600 text-white rounded-xl transition-all shadow-lg shadow-amber-200 disabled:opacity-60 flex items-center justify-center gap-2"
            >
              {loading ? (
                <div className="w-5 h-5 border-2 border-white/40 border-t-white rounded-full animate-spin" />
              ) : (
                <>Daftar Sekarang <ChevronRight className="w-4 h-4" /></>
              )}
            </button>
          </form>

          <p className="text-center text-amber-700/60 text-sm mt-6">
            Sudah punya akun?{' '}
            <Link to="/" className="text-amber-600 underline underline-offset-2">
              Masuk di sini
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
