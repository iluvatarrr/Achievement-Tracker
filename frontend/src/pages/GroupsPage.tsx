import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { apiService } from '../services/api'
import { useAuthStore } from '../store/authStore'
import { GroupDto } from '../types'
import './GroupsPage.css'

export default function GroupsPage() {
  const [groups, setGroups] = useState<GroupDto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [newGroupTitle, setNewGroupTitle] = useState('')
  const [newGroupDescription, setNewGroupDescription] = useState('')
  const [newGroupOrganisation, setNewGroupOrganisation] = useState('')
  const [newGroupIsPublic, setNewGroupIsPublic] = useState(false)

  const { userId } = useAuthStore()
  const navigate = useNavigate()

  useEffect(() => {
    if (userId) {
      loadGroups()
    }
  }, [userId])

  const loadGroups = async () => {
    if (!userId) return
    
    try {
      setLoading(true)
      const userGroups = await apiService.getUserGroups(userId)
      setGroups(userGroups)
    } catch (err: any) {
      setError('Ошибка загрузки групп')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleCreateGroup = async () => {
    if (!userId || !newGroupTitle) return
    
    try {
      const groupId = await apiService.createGroup(userId, {
        title: newGroupTitle,
        description: newGroupDescription || undefined,
        organisation: newGroupOrganisation || undefined,
        isPublic: newGroupIsPublic,
      })
      setNewGroupTitle('')
      setNewGroupDescription('')
      setNewGroupOrganisation('')
      setNewGroupIsPublic(false)
      setShowCreateForm(false)
      loadGroups()
      navigate(`/groups/${groupId}`)
    } catch (err: any) {
      alert('Ошибка создания группы')
      console.error(err)
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Удалить группу?')) return
    
    try {
      await apiService.deleteGroup(id)
      setGroups(groups.filter((g) => g.id !== id))
    } catch (err) {
      alert('Ошибка удаления группы')
    }
  }

  if (loading) return <div className="loading">Загрузка...</div>
  if (error) return <div className="error">{error}</div>

  return (
    <div className="groups-page">
      <div className="page-header">
        <h1>Мои группы</h1>
        <button
          className="btn-primary"
          onClick={() => setShowCreateForm(!showCreateForm)}
        >
          {showCreateForm ? 'Отмена' : 'Создать группу'}
        </button>
      </div>

      {showCreateForm && (
        <div className="create-group-form">
          <h2>Создать новую группу</h2>
          <div className="form-group">
            <label>Название *</label>
            <input
              type="text"
              value={newGroupTitle}
              onChange={(e) => setNewGroupTitle(e.target.value)}
              placeholder="Название группы"
            />
          </div>
          <div className="form-group">
            <label>Описание</label>
            <textarea
              value={newGroupDescription}
              onChange={(e) => setNewGroupDescription(e.target.value)}
              placeholder="Описание группы"
            />
          </div>
          <div className="form-group">
            <label>Организация</label>
            <input
              type="text"
              value={newGroupOrganisation}
              onChange={(e) => setNewGroupOrganisation(e.target.value)}
              placeholder="Название организации"
            />
          </div>
          <div className="form-group">
            <label>
              <input
                type="checkbox"
                checked={newGroupIsPublic}
                onChange={(e) => setNewGroupIsPublic(e.target.checked)}
              />
              Публичная группа
            </label>
          </div>
          <button className="btn-primary" onClick={handleCreateGroup}>
            Создать
          </button>
        </div>
      )}

      <div className="groups-list">
        {groups.length === 0 ? (
          <div className="empty-state">Нет групп. Создайте первую!</div>
        ) : (
          groups.map((group) => (
            <div key={group.id} className="group-card">
              <div className="group-header">
                <h3 onClick={() => navigate(`/groups/${group.id}`)} className="group-title">
                  {group.title}
                </h3>
                <span className={`group-badge ${group.isPublic ? 'public' : 'private'}`}>
                  {group.isPublic ? 'Публичная' : 'Приватная'}
                </span>
              </div>
              {group.description && <p className="group-description">{group.description}</p>}
              <div className="group-info">
                {group.organisation && (
                  <span className="group-org">{group.organisation}</span>
                )}
                <span className="group-members">
                  Участников: {group.members?.length || 0}
                </span>
                <span className="group-status">{group.groupStatus}</span>
              </div>
              <div className="group-actions">
                <button
                  className="btn-secondary"
                  onClick={() => navigate(`/groups/${group.id}`)}
                >
                  Открыть
                </button>
                <button
                  className="btn-danger"
                  onClick={() => handleDelete(group.id)}
                >
                  Удалить
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  )
}

