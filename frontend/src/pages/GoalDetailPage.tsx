import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { apiService } from '../services/api'
import { GoalDto, GoalStatus, GoalCategory, CreateSubGoalDto, UpdateGoalDto, UpdateSubGoalDto } from '../types'
import './GoalDetailPage.css'

export default function GoalDetailPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [goal, setGoal] = useState<GoalDto | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [isEditing, setIsEditing] = useState(false)
  const [editTitle, setEditTitle] = useState('')
  const [editDescription, setEditDescription] = useState('')
  const [editCategory, setEditCategory] = useState<GoalCategory>(GoalCategory.SPORT)
  const [editDeadline, setEditDeadline] = useState('')
  const [showAddSubGoal, setShowAddSubGoal] = useState(false)
  const [subGoalTitle, setSubGoalTitle] = useState('')
  const [subGoalDescription, setSubGoalDescription] = useState('')
  const [subGoalDeadline, setSubGoalDeadline] = useState('')
  const [editingSubGoalId, setEditingSubGoalId] = useState<number | null>(null)
  const [editSubGoalTitle, setEditSubGoalTitle] = useState('')
  const [editSubGoalDescription, setEditSubGoalDescription] = useState('')
  const [editSubGoalDeadline, setEditSubGoalDeadline] = useState('')
  const [editSubGoalStatus, setEditSubGoalStatus] = useState<GoalStatus>(GoalStatus.IN_PROGRESS)

  useEffect(() => {
    if (id) {
      loadGoal()
    }
  }, [id])

  useEffect(() => {
    if (goal && !isEditing) {
      setEditTitle(goal.title)
      setEditDescription(goal.description)
      setEditCategory(goal.goalCategory)
      setEditDeadline(new Date(goal.deadline).toISOString().slice(0, 16))
    }
  }, [goal, isEditing])

  const loadGoal = async () => {
    if (!id) return
    
    try {
      setLoading(true)
      const goalData = await apiService.getGoalById(Number(id))
      setGoal(goalData)
    } catch (err: any) {
      setError('Ошибка загрузки цели')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleUpdateGoal = async () => {
    if (!id || !goal) return
    
    try {
      const updateDto: UpdateGoalDto = {
        title: editTitle,
        description: editDescription,
        goalStatus: goal.goalStatus,
        goalCategory: editCategory,
        deadline: editDeadline,
      }
      await apiService.updateGoal(Number(id), updateDto)
      setIsEditing(false)
      loadGoal()
    } catch (err) {
      alert('Ошибка обновления цели')
    }
  }

  const handleStatusChange = async (status: GoalStatus) => {
    if (!id) return
    
    try {
      const updated = await apiService.updateGoalStatus(Number(id), status)
      setGoal(updated)
    } catch (err) {
      alert('Ошибка обновления статуса')
    }
  }

  const startEditSubGoal = (subGoal: GoalDto['subGoalList'][0]) => {
    setEditingSubGoalId(subGoal.id)
    setEditSubGoalTitle(subGoal.title)
    setEditSubGoalDescription(subGoal.description || '')
    setEditSubGoalDeadline(new Date(subGoal.deadline).toISOString().slice(0, 16))
    setEditSubGoalStatus(subGoal.goalStatus)
  }

  const handleUpdateSubGoal = async () => {
    if (!editingSubGoalId) return
    
    try {
      const updateDto: UpdateSubGoalDto = {
        title: editSubGoalTitle,
        description: editSubGoalDescription || undefined,
        goalStatus: editSubGoalStatus,
        deadline: editSubGoalDeadline,
      }
      await apiService.updateSubGoal(editingSubGoalId, updateDto)
      setEditingSubGoalId(null)
      loadGoal()
    } catch (err) {
      alert('Ошибка обновления подцели')
    }
  }

  const handleAddSubGoal = async () => {
    if (!id || !subGoalTitle || !subGoalDeadline) return
    
    try {
      const subGoal: CreateSubGoalDto = {
        title: subGoalTitle,
        description: subGoalDescription || undefined,
        deadline: subGoalDeadline,
      }
      const updated = await apiService.addSubGoal(Number(id), subGoal)
      setGoal(updated)
      setSubGoalTitle('')
      setSubGoalDescription('')
      setSubGoalDeadline('')
      setShowAddSubGoal(false)
    } catch (err) {
      alert('Ошибка добавления подцели')
    }
  }

  const handleDeleteSubGoal = async (subGoalId: number) => {
    if (!id || !confirm('Удалить подцель?')) return
    
    try {
      const updated = await apiService.removeSubGoal(Number(id), subGoalId)
      setGoal(updated)
    } catch (err) {
      alert('Ошибка удаления подцели')
    }
  }

  const handleUpdateSubGoalStatus = async (subGoalId: number, status: GoalStatus) => {
    try {
      await apiService.updateSubGoalStatus(subGoalId, status)
      loadGoal()
    } catch (err) {
      alert('Ошибка обновления статуса подцели')
    }
  }

  if (loading) return <div className="loading">Загрузка...</div>
  if (error || !goal) return <div className="error">{error || 'Цель не найдена'}</div>

  return (
    <div className="goal-detail-page">
      <button className="back-btn" onClick={() => navigate('/goals')}>
        ← Назад к целям
      </button>

      <div className="goal-detail-card">
        <div className="goal-detail-header">
          {isEditing ? (
            <>
              <input
                type="text"
                value={editTitle}
                onChange={(e) => setEditTitle(e.target.value)}
                className="edit-input"
              />
              <div className="edit-actions">
                <button className="btn-primary" onClick={handleUpdateGoal}>
                  Сохранить
                </button>
                <button className="btn-secondary" onClick={() => setIsEditing(false)}>
                  Отмена
                </button>
              </div>
            </>
          ) : (
            <>
              <h1>{goal.title}</h1>
              <div className="header-actions">
                <select
                  value={goal.goalStatus}
                  onChange={(e) => handleStatusChange(e.target.value as GoalStatus)}
                  className="status-select"
                >
                  <option value={GoalStatus.IN_PROGRESS}>В процессе</option>
                  <option value={GoalStatus.DONE}>Выполнено</option>
                  <option value={GoalStatus.EXPIRED}>Просрочено</option>
                  <option value={GoalStatus.REJECTED}>Отклонено</option>
                </select>
                <button className="btn-secondary" onClick={() => setIsEditing(true)}>
                  Редактировать
                </button>
              </div>
            </>
          )}
        </div>

        {isEditing ? (
          <>
            <textarea
              value={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
              className="edit-textarea"
              placeholder="Описание"
            />
            <div className="edit-fields">
              <div className="form-group">
                <label>Категория:</label>
                <select
                  value={editCategory}
                  onChange={(e) => setEditCategory(e.target.value as GoalCategory)}
                >
                  <option value={GoalCategory.SPORT}>Спорт</option>
                  <option value={GoalCategory.HEALTH}>Здоровье</option>
                  <option value={GoalCategory.CAREER}>Карьера</option>
                  <option value={GoalCategory.EDUCATIONAL}>Образование</option>
                </select>
              </div>
              <div className="form-group">
                <label>Дедлайн:</label>
                <input
                  type="datetime-local"
                  value={editDeadline}
                  onChange={(e) => setEditDeadline(e.target.value)}
                />
              </div>
            </div>
          </>
        ) : (
          <>
            <p className="goal-detail-description">{goal.description}</p>

            <div className="goal-detail-info">
              <div className="info-item">
                <span className="info-label">Категория:</span>
                <span>{goal.goalCategory}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Тип:</span>
                <span>{goal.goalType}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Дедлайн:</span>
                <span>{new Date(goal.deadline).toLocaleString('ru-RU')}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Прогресс:</span>
                <span>{goal.progressInPercent}%</span>
              </div>
            </div>
          </>
        )}

        <div className="progress-bar">
          <div
            className="progress-fill"
            style={{ width: `${goal.progressInPercent}%` }}
          />
        </div>

        <div className="sub-goals-section">
          <div className="sub-goals-header">
            <h2>Подцели</h2>
            <button
              className="btn-primary"
              onClick={() => setShowAddSubGoal(!showAddSubGoal)}
            >
              {showAddSubGoal ? 'Отмена' : 'Добавить подцель'}
            </button>
          </div>

          {showAddSubGoal && (
            <div className="add-sub-goal-form">
              <input
                type="text"
                placeholder="Название подцели"
                value={subGoalTitle}
                onChange={(e) => setSubGoalTitle(e.target.value)}
                className="form-input"
              />
              <textarea
                placeholder="Описание (необязательно)"
                value={subGoalDescription}
                onChange={(e) => setSubGoalDescription(e.target.value)}
                className="form-input"
              />
              <input
                type="datetime-local"
                value={subGoalDeadline}
                onChange={(e) => setSubGoalDeadline(e.target.value)}
                className="form-input"
                required
              />
              <button className="btn-primary" onClick={handleAddSubGoal}>
                Добавить
              </button>
            </div>
          )}

          <div className="sub-goals-list">
            {goal.subGoalList && goal.subGoalList.length > 0 ? (
              goal.subGoalList.map((subGoal) => (
                <div key={subGoal.id} className="sub-goal-card">
                  {editingSubGoalId === subGoal.id ? (
                    <div className="edit-sub-goal-form">
                      <input
                        type="text"
                        value={editSubGoalTitle}
                        onChange={(e) => setEditSubGoalTitle(e.target.value)}
                        className="form-input"
                        placeholder="Название"
                      />
                      <textarea
                        value={editSubGoalDescription}
                        onChange={(e) => setEditSubGoalDescription(e.target.value)}
                        className="form-input"
                        placeholder="Описание"
                      />
                      <div className="edit-sub-goal-fields">
                        <select
                          value={editSubGoalStatus}
                          onChange={(e) => setEditSubGoalStatus(e.target.value as GoalStatus)}
                          className="form-input"
                        >
                          <option value={GoalStatus.IN_PROGRESS}>В процессе</option>
                          <option value={GoalStatus.DONE}>Выполнено</option>
                          <option value={GoalStatus.EXPIRED}>Просрочено</option>
                        </select>
                        <input
                          type="datetime-local"
                          value={editSubGoalDeadline}
                          onChange={(e) => setEditSubGoalDeadline(e.target.value)}
                          className="form-input"
                        />
                      </div>
                      <div className="edit-sub-goal-actions">
                        <button className="btn-primary" onClick={handleUpdateSubGoal}>
                          Сохранить
                        </button>
                        <button
                          className="btn-secondary"
                          onClick={() => setEditingSubGoalId(null)}
                        >
                          Отмена
                        </button>
                      </div>
                    </div>
                  ) : (
                    <>
                      <div className="sub-goal-header">
                        <h3>{subGoal.title}</h3>
                        <select
                          value={subGoal.goalStatus}
                          onChange={(e) =>
                            handleUpdateSubGoalStatus(subGoal.id, e.target.value as GoalStatus)
                          }
                          className="status-select-small"
                        >
                          <option value={GoalStatus.IN_PROGRESS}>В процессе</option>
                          <option value={GoalStatus.DONE}>Выполнено</option>
                          <option value={GoalStatus.EXPIRED}>Просрочено</option>
                        </select>
                      </div>
                      {subGoal.description && <p>{subGoal.description}</p>}
                      <div className="sub-goal-info">
                        <span>
                          Дедлайн: {new Date(subGoal.deadline).toLocaleString('ru-RU')}
                        </span>
                        <div className="sub-goal-actions">
                          <button
                            className="btn-secondary-small"
                            onClick={() => startEditSubGoal(subGoal)}
                          >
                            Редактировать
                          </button>
                          <button
                            className="btn-danger-small"
                            onClick={() => handleDeleteSubGoal(subGoal.id)}
                          >
                            Удалить
                          </button>
                        </div>
                      </div>
                    </>
                  )}
                </div>
              ))
            ) : (
              <div className="empty-state">Нет подцелей</div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

