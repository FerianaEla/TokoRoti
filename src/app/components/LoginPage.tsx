import { useState } from 'react';
import { useNavigate, Link } from 'react-router';
import { useAuth } from '../context/AuthContext';
import { Mail, Lock, Eye, EyeOff, ChevronRight } from 'lucide-react';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    if (!email || !password) {
      setError('Email dan password wajib diisi');
      return;
    }
    if (password.length < 6) {
      setError('Password minimal 6 karakter');
      return;
    }
    setLoading(true);
    await new Promise((r) => setTimeout(r, 600));
    const result = login(email, password);
    setLoading(false);
    if (result.success) {
      navigate(result.role === 'admin' ? '/admin' : '/app/home');
    } else {
      setError('Email atau password salah');
    }
  };

  const handleAdminLogin = async () => {
    setError('');
    setLoading(true);
    await new Promise((r) => setTimeout(r, 600));
    const result = login('admin@breadsweet.com', 'admin123');
    setLoading(false);
    if (result.success) {
      navigate('/admin');
    }
  };

  return (
    <div className="min-h-screen bg-amber-50 flex items-center justify-center p-4">
      <div className="w-full max-w-[390px] min-h-screen bg-amber-50 flex flex-col">
        {/* Hero */}
        <div className="relative overflow-hidden bg-gradient-to-br from-amber-700 via-amber-600 to-yellow-500 px-8 pt-16 pb-12 flex flex-col items-center">
          <div className="absolute -top-8 -right-8 w-40 h-40 bg-white/10 rounded-full" />
          <div className="absolute -bottom-10 -left-10 w-52 h-52 bg-white/10 rounded-full" />
          <div className="relative z-10 flex flex-col items-center">
            <div className="w-20 h-20 bg-white rounded-3xl flex items-center justify-center shadow-xl mb-5">
              <span className="text-4xl">🍞</span>
            </div>
            <h1 className="text-white text-4xl tracking-tight">BreadSweet</h1>
            <p className="text-amber-100 mt-2 text-center">Roti hangat untuk hari yang sempurna</p>
          </div>
        </div>

        {/* Form */}
        <div className="flex-1 bg-white rounded-t-3xl -mt-6 px-6 pt-8 pb-6 shadow-xl">
          <h2 className="text-amber-900 text-2xl mb-1">Selamat Datang</h2>
          <p className="text-amber-700/70 text-sm mb-7">Masuk ke akun Anda</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs text-amber-800 mb-1.5 uppercase tracking-wide">Email</label>
              <div className="relative">
                <Mail className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-amber-400" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full pl-10 pr-4 py-3.5 bg-amber-50 border border-amber-200 rounded-xl text-amber-900 placeholder:text-amber-300 focus:outline-none focus:ring-2 focus:ring-amber-400 focus:border-transparent transition"
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
                  className="w-full pl-10 pr-12 py-3.5 bg-amber-50 border border-amber-200 rounded-xl text-amber-900 placeholder:text-amber-300 focus:outline-none focus:ring-2 focus:ring-amber-400 focus:border-transparent transition"
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
                <>Masuk <ChevronRight className="w-4 h-4" /></>
              )}
            </button>
          </form>

          <div className="flex items-center gap-3 my-5">
            <div className="flex-1 h-px bg-amber-100" />
            <span className="text-amber-400 text-xs">atau</span>
            <div className="flex-1 h-px bg-amber-100" />
          </div>

          {/* Admin Login */}
          <button
            onClick={handleAdminLogin}
            className="w-full py-3.5 border-2 border-amber-200 text-amber-700 rounded-xl hover:bg-amber-50 transition flex items-center justify-center gap-2 group"
          >
            <span className="text-lg">🛡️</span>
            <span>Login sebagai Admin</span>
            <ChevronRight className="w-4 h-4 opacity-0 group-hover:opacity-100 transition" />
          </button>

          <p className="text-center text-amber-700/60 text-sm mt-6">
            Belum punya akun?{' '}
            <Link to="/register" className="text-amber-600 underline underline-offset-2">
              Daftar sekarang
            </Link>
          </p>

          <div className="mt-6 p-3 bg-amber-50 rounded-xl border border-amber-100">
            <p className="text-xs text-amber-600 text-center">
              Demo admin: admin@breadsweet.com / admin123
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
