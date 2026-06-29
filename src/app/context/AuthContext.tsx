import { createContext, useContext, useState, ReactNode } from 'react';

export type UserRole = 'customer' | 'admin';

export interface User {
  id: string;
  email: string;
  name: string;
  phone?: string;
  address?: string;
  role: UserRole;
}

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => { success: boolean; role?: UserRole };
  register: (email: string, password: string, name: string) => boolean;
  logout: () => void;
  updateProfile: (data: Partial<User>) => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const MOCK_ADMIN: User = {
  id: 'admin-001',
  email: 'admin@breadsweet.com',
  name: 'Admin BreadSweet',
  role: 'admin',
};

const MOCK_CUSTOMER: User = {
  id: 'cust-001',
  email: 'pelanggan@email.com',
  name: 'Pelanggan',
  phone: '08123456789',
  role: 'customer',
};

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  const login = (email: string, password: string) => {
    if (email === 'admin@breadsweet.com' && password === 'admin123') {
      setUser(MOCK_ADMIN);
      return { success: true, role: 'admin' as UserRole };
    }
    if (email && password.length >= 6) {
      const customerUser: User = {
        id: `cust-${Date.now()}`,
        email,
        name: email.split('@')[0],
        role: 'customer',
      };
      setUser(customerUser);
      return { success: true, role: 'customer' as UserRole };
    }
    return { success: false };
  };

  const register = (email: string, password: string, name: string) => {
    if (email && password.length >= 6 && name) {
      const newUser: User = {
        id: `cust-${Date.now()}`,
        email,
        name,
        role: 'customer',
      };
      setUser(newUser);
      return true;
    }
    return false;
  };

  const logout = () => setUser(null);

  const updateProfile = (data: Partial<User>) => {
    if (user) setUser({ ...user, ...data });
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, updateProfile, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within AuthProvider');
  return context;
}
