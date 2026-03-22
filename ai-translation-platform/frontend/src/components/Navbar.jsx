import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export default function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-brand">
          <span className="nav-logo">🌐</span>
          <span className="nav-title">AI Translator</span>
        </Link>
        <div className="nav-links">
          <Link to="/" className="nav-link">Translate</Link>
          {isAuthenticated && (
            <Link to="/history" className="nav-link">History</Link>
          )}
          {isAuthenticated ? (
            <div className="nav-user">
              <span className="nav-username">{user?.username}</span>
              <button onClick={handleLogout} className="btn btn-outline btn-sm">
                Logout
              </button>
            </div>
          ) : (
            <div className="nav-auth">
              <Link to="/login" className="btn btn-outline btn-sm">Login</Link>
              <Link to="/register" className="btn btn-primary btn-sm">Sign Up</Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
