import { Outlet, Link, useNavigate } from 'react-router-dom'
import { useAuthStore } from '../store/authStore'
import './Layout.css'

export default function Layout() {
  const { logout, user } = useAuthStore()
  const navigate = useNavigate()
  const isAdmin = user?.roles?.some((role) => role === 'ROLE_ADMIN')

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="layout">
      <nav className="navbar">
        <div className="nav-brand">
          <Link to="/goals">Achievement Tracker</Link>
        </div>
        <div className="nav-links">
          <Link to="/goals">Цели</Link>
          <Link to="/groups">Группы</Link>
          <Link to="/notifications">Уведомления</Link>
          <Link to="/statistics">Статистика</Link>
          <Link to="/profile">Профиль</Link>
          {isAdmin && <Link to="/admin">Админ</Link>}
          <button onClick={handleLogout} className="logout-btn">
            Выйти
          </button>
        </div>
      </nav>
      <main className="main-content">
        <Outlet />
      </main>
    </div>
  )
}

