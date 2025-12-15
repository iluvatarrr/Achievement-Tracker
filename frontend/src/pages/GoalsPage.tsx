import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { apiService } from '../services/api'
import { useAuthStore } from '../store/authStore'
import { GoalDto, GoalStatus, GoalCategory, GoalType, CreateGoalDto } from '../types'
import './GoalsPage.css'

export default function GoalsPage() {
  const [goals, setGoals] = useState<GoalDto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [statusFilter, setStatusFilter] = useState<GoalStatus | ''>('')
  const [categoryFilter, setCategoryFilter] = useState<GoalCategory | ''>('')
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [newGoalTitle, setNewGoalTitle] = useState('')
  const [newGoalDescription, setNewGoalDescription] = useState('')
  const [newGoalCategory, setNewGoalCategory] = useState<GoalCategory>(GoalCategory.SPORT)
  const [newGoalDeadline, setNewGoalDeadline] = useState('')
  
  const { userId } = useAuthStore()
  const navigate = useNavigate()

  useEffect(() => {
    if (userId) {
      loadGoals()
    }
  }, [userId, statusFilter, categoryFilter])

  const loadGoals = async () => {
    if (!userId) return
    
    try {
      setLoading(true)
      const filteredGoals = await apiService.getFilteredGoals(
        userId,
        statusFilter || undefined,
        categoryFilter || undefined
      )
      setGoals(filteredGoals)
    } catch (err: any) {
      setError('Ошибка загрузки целей')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id: number) => {
    if (!confirm('Удалить цель?')) return
    
    try {
      await apiService.deleteGoal(id)
      setGoals(goals.filter((g) => g.id !== id))
    } catch (err) {
      alert('Ошибка удаления цели')
    }
  }

  const handleCreateGoal = async () => {
    if (!userId || !newGoalTitle || !newGoalDescription || !newGoalDeadline) return
    
    try {
      const goal: CreateGoalDto = {
        title: newGoalTitle,
        description: newGoalDescription,
        goalStatus: GoalStatus.IN_PROGRESS,
        goalType: GoalType.SELF,
        goalCategory: newGoalCategory,
        deadline: newGoalDeadline,
      }
      
      const goalId = await apiService.createGoal(userId, goal)
      setNewGoalTitle('')
      setNewGoalDescription('')
      setNewGoalDeadline('')
      setShowCreateForm(false)
      loadGoals()
      navigate(`/goals/${goalId}`)
    } catch (err) {
      alert('Ошибка создания цели')
    }
  }

  const getStatusColor = (status: GoalStatus) => {
    switch (status) {
      case GoalStatus.DONE:
        return '#27ae60'
      case GoalStatus.EXPIRED:
        return '#e74c3c'
      case GoalStatus.REJECTED:
        return '#95a5a6'
      default:
        return '#3498db'
    }
  }

  if (loading) return <div className="loading">Загрузка...</div>
  if (error) return <div className="error">{error}</div>

  return (
    <div className="goals-page">
      <div className="page-header">
        <h1>Мои цели</h1>
        <button className="btn-primary" onClick={() => setShowCreateForm(!showCreateForm)}>
          {showCreateForm ? 'Отмена' : 'Создать цель'}
        </button>
      </div>

      {showCreateForm && (
        <div className="create-goal-form">
          <h2>Создать новую цель</h2>
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

      <div className="filters">
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value as GoalStatus | '')}
          className="filter-select"
        >
          <option value="">Все статусы</option>
          <option value={GoalStatus.IN_PROGRESS}>В процессе</option>
          <option value={GoalStatus.DONE}>Выполнено</option>
          <option value={GoalStatus.EXPIRED}>Просрочено</option>
          <option value={GoalStatus.REJECTED}>Отклонено</option>
        </select>

        <select
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value as GoalCategory | '')}
          className="filter-select"
        >
          <option value="">Все категории</option>
          <option value={GoalCategory.SPORT}>Спорт</option>
          <option value={GoalCategory.HEALTH}>Здоровье</option>
          <option value={GoalCategory.CAREER}>Карьера</option>
          <option value={GoalCategory.EDUCATIONAL}>Образование</option>
        </select>
      </div>

      <div className="goals-list">
        {goals.length === 0 ? (
          <div className="empty-state">Нет целей. Создайте первую!</div>
        ) : (
          goals.map((goal) => (
            <div key={goal.id} className="goal-card">
              <div className="goal-header">
                <h3 onClick={() => navigate(`/goals/${goal.id}`)} className="goal-title">
                  {goal.title}
                </h3>
                <span
                  className="goal-status"
                  style={{ backgroundColor: getStatusColor(goal.goalStatus) }}
                >
                  {goal.goalStatus}
                </span>
              </div>
              <p className="goal-description">{goal.description}</p>
              <div className="goal-info">
                <span className="goal-category">{goal.goalCategory}</span>
                <span className="goal-deadline">
                  До: {new Date(goal.deadline).toLocaleDateString('ru-RU')}
                </span>
                <span className="goal-progress">Прогресс: {goal.progressInPercent}%</span>
              </div>
              <div className="goal-actions">
                <button
                  className="btn-secondary"
                  onClick={() => navigate(`/goals/${goal.id}`)}
                >
                  Подробнее
                </button>
                <button
                  className="btn-danger"
                  onClick={() => handleDelete(goal.id)}
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

