import { createBrowserRouter } from 'react-router';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import MobileLayout from './components/MobileLayout';
import HomePage from './components/HomePage';
import CategoriesPage from './components/CategoriesPage';
import CartPage from './components/CartPage';
import ProfilePage from './components/ProfilePage';
import ProductDetailPage from './components/ProductDetailPage';
import CheckoutPage from './components/CheckoutPage';
import AdminDashboard from './components/AdminDashboard';

export const router = createBrowserRouter([
  {
    path: '/',
    Component: LoginPage,
  },
  {
    path: '/register',
    Component: RegisterPage,
  },
  {
    path: '/admin',
    Component: AdminDashboard,
  },
  {
    path: '/app',
    Component: MobileLayout,
    children: [
      {
        path: 'home',
        Component: HomePage,
      },
      {
        path: 'categories',
        Component: CategoriesPage,
      },
      {
        path: 'cart',
        Component: CartPage,
      },
      {
        path: 'profile',
        Component: ProfilePage,
      },
    ],
  },
  {
    path: '/app/product/:id',
    Component: ProductDetailPage,
  },
  {
    path: '/app/checkout',
    Component: CheckoutPage,
  },
  {
    path: '*',
    element: (
      <div className="min-h-screen flex flex-col items-center justify-center bg-amber-50">
        <p className="text-6xl mb-4">🍞</p>
        <h1 className="text-2xl text-amber-900 mb-2">404</h1>
        <p className="text-amber-600">Halaman tidak ditemukan</p>
      </div>
    ),
  },
]);
