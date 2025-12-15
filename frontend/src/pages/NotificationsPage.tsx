import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { apiService } from '../services/api'
import { useAuthStore } from '../store/authStore'
import { GroupInvocationDto, GroupInvitationStatus } from '../types'
import './NotificationsPage.css'

export default function NotificationsPage() {
  const [invitations, setInvitations] = useState<GroupInvocationDto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const navigate = useNavigate()
  
  const { user } = useAuthStore()

  useEffect(() => {
    if (user?.email) {
      loadInvitations()
    }
  }, [user])

  const loadInvitations = async () => {
    if (!user?.email) return
    
    try {
      setLoading(true)
      const data = await apiService.getGroupInvocations(user.email)
      setInvitations(data)
    } catch (err: any) {
      setError('Ошибка загрузки уведомлений')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleStatusChange = async (invitation: GroupInvocationDto, status: GroupInvitationStatus) => {
    try {
      await apiService.updateInvocationStatus(invitation.id, status)
      if (status === GroupInvitationStatus.ACCEPTED) {
        alert('Приглашение принято. Владелец группы добавит вас в группу после рассмотрения.')
      } else if (status === GroupInvitationStatus.DECLINED) {
        alert('Приглашение отклонено.')
      }
      loadInvitations()
    } catch (err: any) {
      alert(err.response?.data?.message || 'Ошибка обновления статуса приглашения')
    }
  }

  const getStatusColor = (status: GroupInvitationStatus) => {
    switch (status) {
      case GroupInvitationStatus.PENDING:
        return '#f39c12'
      case GroupInvitationStatus.ACCEPTED:
        return '#27ae60'
      case GroupInvitationStatus.DECLINED:
        return '#e74c3c'
      case GroupInvitationStatus.EXPIRED:
        return '#95a5a6'
      case GroupInvitationStatus.CANCELLED:
        return '#95a5a6'
      default:
        return '#3498db'
    }
  }

  if (loading) return <div className="loading">Загрузка...</div>
  if (error) return <div className="error">{error}</div>

  const pendingInvitations = invitations.filter(
    (inv) => inv.status === GroupInvitationStatus.PENDING
  )
  const otherInvitations = invitations.filter(
    (inv) => inv.status !== GroupInvitationStatus.PENDING
  )

  return (
    <div className="notifications-page">
      <h1>Приглашения в группы</h1>

      {pendingInvitations.length > 0 && (
        <div className="invitations-section">
          <h2>Новые приглашения</h2>
          <div className="invitations-list">
            {pendingInvitations.map((invitation) => (
              <div key={invitation.id} className="invitation-card pending">
                <div className="invitation-header">
                  <h3>{invitation.groupName}</h3>
                  <span
                    className="invitation-status"
                    style={{ backgroundColor: getStatusColor(invitation.status) }}
                  >
                    {invitation.status}
                  </span>
                </div>
                <p className="invitation-info">
                  Пригласил: <strong>{invitation.invitedByName}</strong>
                </p>
                <p className="invitation-date">
                  Создано: {new Date(invitation.createdAt).toLocaleString('ru-RU')}
                </p>
                <p className="invitation-date">
                  Истекает: {new Date(invitation.expiresAt).toLocaleString('ru-RU')}
                </p>
                <div className="invitation-actions">
                  <button
                    className="btn-accept"
                    onClick={() => handleStatusChange(invitation, GroupInvitationStatus.ACCEPTED)}
                  >
                    Принять
                  </button>
                  <button
                    className="btn-decline"
                    onClick={() => handleStatusChange(invitation, GroupInvitationStatus.DECLINED)}
                  >
                    Отклонить
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {otherInvitations.length > 0 && (
        <div className="invitations-section">
          <h2>История приглашений</h2>
          <div className="invitations-list">
            {otherInvitations.map((invitation) => (
              <div key={invitation.id} className="invitation-card">
                <div className="invitation-header">
                  <h3>{invitation.groupName}</h3>
                  <span
                    className="invitation-status"
                    style={{ backgroundColor: getStatusColor(invitation.status) }}
                  >
                    {invitation.status}
                  </span>
                </div>
                <p className="invitation-info">
                  Пригласил: <strong>{invitation.invitedByName}</strong>
                </p>
                <p className="invitation-date">
                  {new Date(invitation.createdAt).toLocaleString('ru-RU')}
                </p>
              </div>
            ))}
          </div>
        </div>
      )}

      {invitations.length === 0 && (
        <div className="empty-state">Нет приглашений</div>
      )}
    </div>
  )
}

