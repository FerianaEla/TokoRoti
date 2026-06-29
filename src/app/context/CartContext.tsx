import { createContext, useContext, useState, ReactNode } from 'react';

export interface Product {
  id: number;
  name: string;
  category: string;
  variants: string[];
  price: number;
  originalPrice?: number;
  image: string;
  description: string;
  ingredients?: string[];
  rating: number;
  sold: number;
  stock: number;
  isNew?: boolean;
  isPromo?: boolean;
}

export interface CartItem {
  cartId: string;
  product: Product;
  quantity: number;
  selectedVariant: string;
}

interface CartContextType {
  items: CartItem[];
  addToCart: (product: Product, variant?: string) => void;
  removeFromCart: (cartId: string) => void;
  updateQuantity: (cartId: string, quantity: number) => void;
  clearCart: () => void;
  totalItems: number;
  totalPrice: number;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export function CartProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<CartItem[]>([]);

  const addToCart = (product: Product, variant?: string) => {
    const selectedVariant = variant || product.variants[0] || 'Original';
    const cartId = `${product.id}-${selectedVariant}`;

    setItems((prev) => {
      const existing = prev.find((i) => i.cartId === cartId);
      if (existing) {
        return prev.map((i) =>
          i.cartId === cartId ? { ...i, quantity: i.quantity + 1 } : i
        );
      }
      return [...prev, { cartId, product, quantity: 1, selectedVariant }];
    });
  };

  const removeFromCart = (cartId: string) => {
    setItems((prev) => prev.filter((i) => i.cartId !== cartId));
  };

  const updateQuantity = (cartId: string, quantity: number) => {
    if (quantity <= 0) {
      removeFromCart(cartId);
      return;
    }
    setItems((prev) =>
      prev.map((i) => (i.cartId === cartId ? { ...i, quantity } : i))
    );
  };

  const clearCart = () => setItems([]);

  const totalItems = items.reduce((sum, i) => sum + i.quantity, 0);
  const totalPrice = items.reduce((sum, i) => sum + i.product.price * i.quantity, 0);

  return (
    <CartContext.Provider value={{ items, addToCart, removeFromCart, updateQuantity, clearCart, totalItems, totalPrice }}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (!context) throw new Error('useCart must be used within CartProvider');
  return context;
}
