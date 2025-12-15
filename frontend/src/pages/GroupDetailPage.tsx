import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { apiService } from '../services/api'
import { useAuthStore } from '../store/authStore'
import {
  GroupDto,
  GoalDto,
  GroupRole,
  CreateGoalDto,
  GoalStatus,
  GoalCategory,
  GoalType,
  UserDto,
  GroupInvocationDto, GroupInvitationStatus
} from '../types'
import MemberCard from '../components/MemberCard'
import MemberOption from '../components/MemberOption'
import './GroupDetailPage.css'

export default function GroupDetailPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [group, setGroup] = useState<GroupDto | null>(null)
  const [goals, setGoals] = useState<GoalDto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showCreateGoalForm, setShowCreateGoalForm] = useState(false)
  const [newGoalTitle, setNewGoalTitle] = useState('')
  const [newGoalDescription, setNewGoalDescription] = useState('')
  const [newGoalCategory, setNewGoalCategory] = useState<GoalCategory>(GoalCategory.SPORT)
  const [newGoalDeadline, setNewGoalDeadline] = useState('')
  const [selectedMemberId, setSelectedMemberId] = useState<number | null>(null)
  const [showInviteForm, setShowInviteForm] = useState(false)
  const [inviteUsername, setInviteUsername] = useState('')
  const [allUsers, setAllUsers] = useState<UserDto[]>([])
  const [acceptedInvitations, setAcceptedInvitations] = useState<GroupInvocationDto[]>([])

  const { userId, user } = useAuthStore()

  useEffect(() => {
    if (id) {
      loadGroup()
      loadGoals()
      loadAllUsers()
    }
  }, [id])

  useEffect(() => {
    if (group && id && isOwnerOrModerator()) {
      loadAcceptedInvitations()
    }
  }, [group, id])

  const loadAllUsers = async () => {
    try {
      // Пытаемся загрузить всех пользователей (требует роль ADMIN)
      // Если нет прав, просто игнорируем - список будет пустым
      const users = await apiService.getAllUsers()
      setAllUsers(users)
    } catch (err) {
      // Игнорируем ошибку, если нет прав доступа
      console.warn('Не удалось загрузить список пользователей (требуется роль ADMIN)')
      setAllUsers([])
    }
  }

  const loadAcceptedInvitations = async () => {
    if (!group) return
    try {
      // Получаем всех пользователей (если есть права) и проверяем их приглашения для этой группы
      // Это не идеально, но без специального API метода по groupId - единственный способ
      const users = await apiService.getAllUsers()
      const invitations: GroupInvocationDto[] = []
      
      for (const u of users) {
        try {
          const userInvitations = await apiService.getGroupInvocations(u.email)
          const groupInvitations = userInvitations.filter(
            inv => inv.groupName === group.title && inv.status === GroupInvitationStatus.ACCEPTED
          )
          invitations.push(...groupInvitations)
        } catch {
          // Игнорируем ошибки для отдельных пользователей
        }
      }
      
      setAcceptedInvitations(invitations)
    } catch (err) {
      console.warn('Не удалось загрузить принятые приглашения')
      setAcceptedInvitations([])
    }
  }

  const handleAddAcceptedUser = async (invitation: GroupInvocationDto) => {
    if (!id || !confirm(`Добавить пользователя ${invitation.username} в группу?`)) return
    
    try {
      // Находим ID пользователя по email/username
      let targetUser = allUsers.find(u => u.email === invitation.username)
      
      // Если не нашли в списке всех пользователей, пробуем найти через поиск
      if (!targetUser && allUsers.length > 0) {
        // Пробуем найти по точному совпадению email
        targetUser = allUsers.find(u => 
          u.email.toLowerCase() === invitation.username.toLowerCase()
        )
      }
      
      // Если все еще не нашли, пробуем получить пользователя через API (если есть такой метод)
      if (!targetUser) {
        // Показываем ошибку, но предлагаем попробовать обновить список
        if (confirm('Пользователь не найден в списке. Обновить список пользователей?')) {
          await loadAllUsers()
          targetUser = allUsers.find(u => u.email === invitation.username)
        }
      }
      
      if (!targetUser) {
        alert(`Пользователь с email ${invitation.username} не найден. Убедитесь, что пользователь зарегистрирован в системе.`)
        return
      }
      
      await apiService.addMember(Number(id), targetUser.id)
      alert(`Пользователь ${invitation.username} успешно добавлен в группу`)
      loadGroup()
      loadAcceptedInvitations()
    } catch (err: any) {
      const errorMsg = err.response?.data?.message || err.message || 'Ошибка добавления пользователя'
      alert(errorMsg)
      console.error('Ошибка добавления пользователя:', err)
    }
  }

  const isOwnerOrModerator = () => {
    if (!group || !userId) return false
    const currentMember = group.members?.find(m => m.user.id === userId)
    return currentMember?.role === GroupRole.OWNER || currentMember?.role === GroupRole.MODERATOR
  }

  const isOwner = () => {
    if (!group || !userId) return false
    return group.createdBy.id === userId
  }

  const isCurrentUserMember = () => {
    if (!group || !userId) return false
    return group.members?.some(m => m.user.id === userId) || false
  }

  const loadGroup = async () => {
    if (!id) return
    
    try {
      const groupData = await apiService.getGroupById(Number(id))
      setGroup(groupData)
    } catch (err: any) {
      setError('Ошибка загрузки группы')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const loadGoals = async () => {
    if (!id) return
    
    try {
      const goalsData = await apiService.getAllGoalsByGroupId(Number(id))
      setGoals(goalsData)
    } catch (err) {
      console.error('Ошибка загрузки целей группы')
    }
  }

  const handleCreateGoal = async () => {
    if (!id || !newGoalTitle || !newGoalDescription || !newGoalDeadline || !selectedMemberId) {
      alert('Выберите участника для назначения цели')
      return
    }
    
    try {
      const goal: CreateGoalDto = {
        title: newGoalTitle,
        description: newGoalDescription,
        goalStatus: GoalStatus.IN_PROGRESS,
        goalType: GoalType.GROUP,
        goalCategory: newGoalCategory,
        deadline: newGoalDeadline,
      }
      
      await apiService.createGroupGoal(selectedMemberId, Number(id), goal)
      setNewGoalTitle('')
      setNewGoalDescription('')
      setNewGoalDeadline('')
      setSelectedMemberId(null)
      setShowCreateGoalForm(false)
      loadGoals()
    } catch (err) {
      alert('Ошибка создания цели')
    }
  }

  const handleInviteUser = async () => {
    if (!id || !inviteUsername || !user?.email) {
      alert('Введите email пользователя')
      return
    }

    try {
      // Вычисляем expiresAt - через 7 дней
      const expiresAt = new Date()
      expiresAt.setDate(expiresAt.getDate() + 7)
      const expiresAtString = expiresAt.toISOString()

      // Передаем email текущего пользователя в метод
      await apiService.inviteToGroup(Number(id), inviteUsername, expiresAtString, user.email)

      setInviteUsername('')
      setShowInviteForm(false)
      alert('Приглашение отправлено пользователю ' + inviteUsername)
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || err.message || 'Ошибка отправки приглашения'
      alert(errorMessage)
      console.error('Ошибка отправки приглашения:', err)
    }
  }


  const handleDeleteMember = async (memberUserId: number) => {
    if (!id || !confirm('Удалить участника из группы?')) return
    
    try {
      await apiService.deleteMember(Number(id), memberUserId)
      loadGroup()
    } catch (err) {
      alert('Ошибка удаления участника')
    }
  }

  const handleLeaveGroup = async () => {
    if (!id || !userId || !confirm('Покинуть группу?')) return
    
    try {
      await apiService.deleteMember(Number(id), userId)
      navigate('/groups')
    } catch (err) {
      alert('Ошибка выхода из группы')
    }
  }

  const getUserProfile = async (userId: number): Promise<UserDto | null> => {
    try {
      return await apiService.getUserById(userId)
    } catch {
      return null
    }
  }

  if (loading) return <div className="loading">Загрузка...</div>
  if (error || !group) return <div className="error">{error || 'Группа не найдена'}</div>

  return (
    <div className="group-detail-page">
      <button className="back-btn" onClick={() => navigate('/groups')}>
        ← Назад к группам
      </button>

      <div className="group-detail-card">
        <div className="group-detail-header">
          <div>
            <h1>{group.title}</h1>
            <span className={`group-badge ${group.isPublic ? 'public' : 'private'}`}>
              {group.isPublic ? 'Публичная' : 'Приватная'}
            </span>
          </div>
        </div>

        {group.description && <p className="group-detail-description">{group.description}</p>}
        
        {group.organisation && (
          <div className="group-org">Организация: {group.organisation}</div>
        )}

        <div className="members-section">
          <div className="members-header">
            <h2>Участники ({group.members?.length || 0})</h2>
            {isOwnerOrModerator() && (
              <button
                className="btn-primary"
                onClick={() => setShowInviteForm(!showInviteForm)}
              >
                {showInviteForm ? 'Отмена' : 'Пригласить пользователя'}
              </button>
            )}
            {isCurrentUserMember() && !isOwner() && (
              <button className="btn-danger" onClick={handleLeaveGroup}>
                Покинуть группу
              </button>
            )}
          </div>

          {showInviteForm && (
            <div className="invite-form">
              <div className="form-group">
                <label>Email пользователя *</label>
                <input
                  type="email"
                  value={inviteUsername}
                  onChange={(e) => setInviteUsername(e.target.value)}
                  placeholder="email@example.com"
                  list="users-list"
                />
                <datalist id="users-list">
                  {allUsers.map(u => (
                    <option key={u.id} value={u.email} />
                  ))}
                </datalist>
              </div>
              <button className="btn-primary" onClick={handleInviteUser}>
                Отправить приглашение
              </button>
            </div>
          )}

          {acceptedInvitations.length > 0 && isOwnerOrModerator() && (
            <div className="accepted-invitations-section">
              <h3>Принятые приглашения (требуют добавления в группу)</h3>
              <div className="accepted-invitations-list">
                {acceptedInvitations.map((invitation) => {
                  // Проверяем, не добавлен ли уже этот пользователь в группу
                  const isAlreadyMember = group.members?.some(m => m.user.username === invitation.username)
                  if (isAlreadyMember) return null
                  
                  return (
                    <div key={invitation.id} className="accepted-invitation-card">
                      <div className="invitation-info">
                        <span className="invitation-username">{invitation.username}</span>
                        <span className="invitation-date">
                          Принято: {new Date(invitation.createdAt).toLocaleString('ru-RU')}
                        </span>
                      </div>
                      <button
                        className="btn-primary"
                        onClick={() => handleAddAcceptedUser(invitation)}
                      >
                        Добавить в группу
                      </button>
                    </div>
                  )
                })}
              </div>
            </div>
          )}

          <div className="members-list">
            {group.members && group.members.length > 0 ? (
              group.members.map((member) => (
                  <MemberCard
                  key={member.id}
                  member={member}
                  canDelete={isOwnerOrModerator() && member.user.id !== userId}
                  onDelete={() => handleDeleteMember(member.user.id)}
                />
              ))
            ) : (
              <div className="empty-state">Нет участников</div>
            )}
          </div>
        </div>

        <div className="goals-section">
          <div className="goals-header">
            <h2>Цели группы</h2>
            <button
              className="btn-primary"
              onClick={() => setShowCreateGoalForm(!showCreateGoalForm)}
            >
              {showCreateGoalForm ? 'Отмена' : 'Создать цель'}
            </button>
          </div>

          {showCreateGoalForm && (
            <div className="create-goal-form">
              <div className="form-group">
                <label>Название *</label>
                <input
                  type="text"
                  value={newGoalTitle}
                  onChange={(e) => setNewGoalTitle(e.target.value)}
                  placeholder="Название цели"
                />
              </div>
              <div className="form-group">
                <label>Описание *</label>
                <textarea
                  value={newGoalDescription}
                  onChange={(e) => setNewGoalDescription(e.target.value)}
                  placeholder="Описание цели"
                />
              </div>
              <div className="form-group">
                <label>Категория</label>
                <select
                  value={newGoalCategory}
                  onChange={(e) => setNewGoalCategory(e.target.value as GoalCategory)}
                >
                  <option value={GoalCategory.SPORT}>Спорт</option>
                  <option value={GoalCategory.HEALTH}>Здоровье</option>
                  <option value={GoalCategory.CAREER}>Карьера</option>
                  <option value={GoalCategory.EDUCATIONAL}>Образование</option>
                </select>
              </div>
              <div className="form-group">
                <label>Участник *</label>
                <select
                  value={selectedMemberId || ''}
                  onChange={(e) => setSelectedMemberId(Number(e.target.value) || null)}
                  required
                >
                  <option value="">Выберите участника</option>
                  {group.members?.map(member => {
                    // Используем MemberOption для отображения имени и фамилии
                    return (
                      <MemberOption key={member.user.id} member={member} />
                    )
                  })}
                </select>
              </div>
              <div className="form-group">
                <label>Дедлайн *</label>
                <input
                  type="datetime-local"
                  value={newGoalDeadline}
                  onChange={(e) => setNewGoalDeadline(e.target.value)}
                  required
                />
              </div>
              <button className="btn-primary" onClick={handleCreateGoal}>
                Создать
              </button>
            </div>
          )}

          <div className="goals-list">
            {goals.length === 0 ? (
              <div className="empty-state">Нет целей</div>
            ) : (
              goals.map((goal) => (
                <div
                  key={goal.id}
                  className="goal-item"
                  onClick={() => navigate(`/goals/${goal.id}`)}
                >
                  <div className="goal-item-header">
                    <h3>{goal.title}</h3>
                    <span className="goal-status">{goal.goalStatus}</span>
                  </div>
                  <p>{goal.description}</p>
                  <div className="goal-item-info">
                    <span>{goal.goalCategory}</span>
                    <span>Прогресс: {goal.progressInPercent}%</span>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

