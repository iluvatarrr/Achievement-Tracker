import { useState, useEffect } from 'react'
import { apiService } from '../services/api'
import { useAuthStore } from '../store/authStore'
import { UserDto, UserUpdateDto, ChangePasswordRequest } from '../types'
import './ProfilePage.css'

export default function ProfilePage() {
  const { userId, user, setUser } = useAuthStore()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [email, setEmail] = useState('')
  
  const [oldPassword, setOldPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPasswordForm, setShowPasswordForm] = useState(false)

  useEffect(() => {
    if (userId) {
      loadUser()
    }
  }, [userId])

  const loadUser = async () => {
    if (!userId) return
    
    try {
      const userData = await apiService.getUserById(userId)
      setUser(userData)
      setFirstName(userData.profile?.firstName || '')
      setLastName(userData.profile?.lastName || '')
      setEmail(userData.email || '')
    } catch (err) {
      console.error('Ошибка загрузки пользователя')
    }
  }

  const handleUpdateProfile = async () => {
    if (!userId) return
    
    try {
      setLoading(true)
      setError('')
      
      const updateDto: UserUpdateDto = {
        email,
        profile: {
          firstName,
          lastName,
        },
      }
      
      const updated = await apiService.updateUser(userId, updateDto)
      setUser(updated)
      setSuccess('Профиль обновлен')
      setTimeout(() => setSuccess(''), 3000)
    } catch (err: any) {
      setError('Ошибка обновления профиля')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleChangePassword = async () => {
    if (!userId || !oldPassword || !newPassword) return
    
    if (newPassword !== confirmPassword) {
      setError('Пароли не совпадают')
      return
    }
    
    if (newPassword.length < 6) {
      setError('Пароль должен быть не менее 6 символов')
      return
    }

    try {
      setLoading(true)
      setError('')
      
      const request: ChangePasswordRequest = {
        currentPassword: oldPassword,
        newPassword,
      }
      
      await apiService.changePassword(request)
      setOldPassword('')
      setNewPassword('')
      setConfirmPassword('')
      setShowPasswordForm(false)
      setSuccess('Пароль изменен')
      setTimeout(() => setSuccess(''), 3000)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Ошибка изменения пароля')
    } finally {
      setLoading(false)
    }
  }

  if (!user) return <div className="loading">Загрузка...</div>

  return (
    <div className="profile-page">
      <h1>Мой профиль</h1>

      <div className="profile-card">
        <h2>Информация профиля</h2>
        
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}

        <div className="form-group">
          <label>Имя</label>
          <input
            type="text"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            placeholder="Имя"
          />
        </div>

        <div className="form-group">
          <label>Фамилия</label>
          <input
            type="text"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            placeholder="Фамилия"
          />
        </div>

        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
          />
        </div>

        <div className="profile-info">
          <div className="info-item">
            <span className="info-label">Статус:</span>
            <span>{user.userStatus}</span>
          </div>
          <div className="info-item">
            <span className="info-label">Роли:</span>
            <span>{user.roles?.join(', ') || 'Нет ролей'}</span>
          </div>
          <div className="info-item">
            <span className="info-label">Дата регистрации:</span>
            <span>{new Date(user.createdAt).toLocaleDateString('ru-RU')}</span>
          </div>
        </div>

        <button
          className="btn-primary"
          onClick={handleUpdateProfile}
          disabled={loading}
        >
          {loading ? 'Сохранение...' : 'Сохранить изменения'}
        </button>
      </div>

      <div className="profile-card">
        <div className="password-header">
          <h2>Изменение пароля</h2>
          <button
            className="btn-secondary"
            onClick={() => setShowPasswordForm(!showPasswordForm)}
          >
            {showPasswordForm ? 'Отмена' : 'Изменить пароль'}
          </button>
        </div>

        {showPasswordForm && (
          <div className="password-form">
            <div className="form-group">
              <label>Текущий пароль</label>
              <input
                type="password"
                value={oldPassword}
                onChange={(e) => setOldPassword(e.target.value)}
                placeholder="Текущий пароль"
              />
            </div>

            <div className="form-group">
              <label>Новый пароль</label>
              <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                placeholder="Новый пароль (минимум 6 символов)"
                minLength={6}
              />
            </div>

            <div className="form-group">
              <label>Подтвердите новый пароль</label>
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Подтвердите новый пароль"
                minLength={6}
              />
            </div>

            <button
              className="btn-primary"
              onClick={handleChangePassword}
              disabled={loading}
            >
              {loading ? 'Изменение...' : 'Изменить пароль'}
            </button>
          </div>
        )}
      </div>
    </div>
  )
}

