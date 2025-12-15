import { useState, useEffect } from 'react'
import { apiService } from '../services/api'
import { UserDto, GroupDto, Role, UserStatus, GroupStatus } from '../types'
import './AdminPage.css'

interface Metric {
  name: string
  description?: string
  measurements?: Array<{
    statistic: string
    value: number
  }>
}

interface HealthInfo {
  status: string
  components?: Record<string, any>
}

export default function AdminPage() {
  const [users, setUsers] = useState<UserDto[]>([])
  const [groups, setGroups] = useState<GroupDto[]>([])
  const [activeTab, setActiveTab] = useState<'users' | 'groups' | 'metrics'>('users')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [health, setHealth] = useState<HealthInfo | null>(null)
  const [metrics, setMetrics] = useState<Metric[]>([])
  const [selectedMetric, setSelectedMetric] = useState<string>('')
  const [metricDetails, setMetricDetails] = useState<any>(null)
  const [loadingMetrics, setLoadingMetrics] = useState(false)

  useEffect(() => {
    loadUsers()
    loadGroups()
  }, [])

  const loadUsers = async () => {
    try {
      const data = await apiService.getAllUsers()
      setUsers(data)
    } catch (err: any) {
      setError('Ошибка загрузки пользователей')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const loadGroups = async () => {
    try {
      const data = await apiService.getAllGroups()
      setGroups(data)
    } catch (err: any) {
      console.error('Ошибка загрузки групп')
    }
  }

  const handleSetUserRole = async (userId: number, role: Role) => {
    try {
      await apiService.addRole(userId, role)
      // Перезагружаем список пользователей после изменения
      await loadUsers()
      alert('Роль успешно изменена')
    } catch (err: any) {
      const errorMsg = err.response?.data?.message || err.message || 'Ошибка изменения роли'
      alert(errorMsg)
      console.error('Ошибка изменения роли:', err)
    }
  }

  const handleSetUserStatus = async (userId: number, status: UserStatus) => {
    try {
      await apiService.setUserStatus(userId, status)
      // Перезагружаем список пользователей после изменения
      await loadUsers()
      alert('Статус пользователя успешно изменен')
    } catch (err: any) {
      const errorMsg = err.response?.data?.message || err.message || 'Ошибка изменения статуса'
      alert(errorMsg)
      console.error('Ошибка изменения статуса:', err)
    }
  }

  const handleSetGroupStatus = async (groupId: number, status: GroupStatus) => {
    try {
      await apiService.setGroupStatus(groupId, status)
      // Перезагружаем список групп после изменения
      await loadGroups()
      alert('Статус группы успешно изменен')
    } catch (err: any) {
      const errorMsg = err.response?.data?.message || err.message || 'Ошибка изменения статуса группы'
      alert(errorMsg)
      console.error('Ошибка изменения статуса группы:', err)
    }
  }

  const loadHealth = async () => {
    try {
      const healthData = await apiService.getHealth()
      setHealth(healthData)
    } catch (err: any) {
      console.error('Ошибка загрузки health:', err)
      setHealth({ status: 'UNKNOWN' })
    }
  }

  const loadMetrics = async () => {
    try {
      setLoadingMetrics(true)
      const metricsData = await apiService.getMetrics()
      // Spring Actuator возвращает объект с полем names как массив
      if (metricsData && Array.isArray(metricsData.names)) {
        setMetrics(metricsData.names.map((name: string) => ({ name })))
      } else if (Array.isArray(metricsData)) {
        // Если приходит массив напрямую
        setMetrics(metricsData.map((name: string) => ({ name })))
      } else {
        console.warn('Неожиданный формат ответа метрик:', metricsData)
        setMetrics([])
      }
    } catch (err: any) {
      console.error('Ошибка загрузки метрик:', err)
      const errorMsg = err.response?.data?.message || err.message || 'Ошибка загрузки метрик'
      alert(`Ошибка загрузки метрик: ${errorMsg}. Убедитесь, что вы авторизованы как ADMIN и actuator endpoints доступны.`)
      setMetrics([])
    } finally {
      setLoadingMetrics(false)
    }
  }

  const loadMetricDetails = async (metricName: string) => {
    try {
      setLoadingMetrics(true)
      const details = await apiService.getMetric(metricName)
      setMetricDetails(details)
    } catch (err: any) {
      console.error('Ошибка загрузки деталей метрики:', err)
      setMetricDetails(null)
    } finally {
      setLoadingMetrics(false)
    }
  }

  useEffect(() => {
    if (activeTab === 'metrics') {
      loadHealth()
      loadMetrics()
    }
  }, [activeTab])

  if (loading) return <div className="loading">Загрузка...</div>
  if (error) return <div className="error">{error}</div>

  return (
    <div className="admin-page">
      <h1>Панель администратора</h1>

      <div className="admin-tabs">
        <button
          className={`tab-button ${activeTab === 'users' ? 'active' : ''}`}
          onClick={() => setActiveTab('users')}
        >
          Пользователи
        </button>
        <button
          className={`tab-button ${activeTab === 'groups' ? 'active' : ''}`}
          onClick={() => setActiveTab('groups')}
        >
          Группы
        </button>
        <button
          className={`tab-button ${activeTab === 'metrics' ? 'active' : ''}`}
          onClick={() => setActiveTab('metrics')}
        >
          Метрики
        </button>
      </div>

      {activeTab === 'users' && (
        <div className="admin-section">
          <h2>Управление пользователями</h2>
          <div className="admin-table">
            <table>
              <thead>
                <tr>
                  <th>Email</th>
                  <th>Имя</th>
                  <th>Фамилия</th>
                  <th>Роли</th>
                  <th>Статус</th>
                  <th>Действия</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.id || user.email}>
                    <td>{user.email}</td>
                    <td>{user.profile?.firstName || '-'}</td>
                    <td>{user.profile?.lastName || '-'}</td>
                    <td>{user.roles?.join(', ') || '-'}</td>
                    <td>{user.userStatus}</td>
                    <td>
                      <div className="action-buttons">
                        <select
                          defaultValue=""
                          onChange={(e) => {
                            const select = e.target
                            if (select.value && user.id) {
                              handleSetUserRole(user.id, select.value as Role)
                              setTimeout(() => { select.value = '' }, 100)
                            }
                          }}
                          className="action-select"
                        >
                          <option value="">Изменить роль</option>
                          <option value={Role.ROLE_USER}>USER</option>
                          <option value={Role.ROLE_ADMIN}>ADMIN</option>
                        </select>
                        <select
                          defaultValue=""
                          onChange={(e) => {
                            const select = e.target
                            if (select.value && user.id) {
                              handleSetUserStatus(user.id, select.value as UserStatus)
                              setTimeout(() => { select.value = '' }, 100)
                            }
                          }}
                          className="action-select"
                        >
                          <option value="">Изменить статус</option>
                          <option value={UserStatus.ACTIVE}>ACTIVE</option>
                          <option value={UserStatus.ARCHIVED}>ARCHIVED</option>
                        </select>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {activeTab === 'groups' && (
        <div className="admin-section">
          <h2>Управление группами</h2>
          <div className="groups-list">
            {groups.map((group) => (
              <div key={group.id} className="admin-group-card">
                <div className="group-header">
                  <h3>{group.title}</h3>
                  <span className="group-status-badge">{group.groupStatus}</span>
                </div>
                <p>{group.description}</p>
                <div className="group-actions">
                  <select
                    defaultValue=""
                    onChange={(e) => {
                      const select = e.target
                      if (select.value) {
                        handleSetGroupStatus(group.id, select.value as GroupStatus)
                        setTimeout(() => { select.value = '' }, 100)
                      }
                    }}
                    className="action-select"
                  >
                    <option value="">Изменить статус</option>
                    <option value={GroupStatus.ACTIVE}>ACTIVE</option>
                    <option value={GroupStatus.ARCHIVED}>ARCHIVED</option>
                  </select>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {activeTab === 'metrics' && (
        <div className="admin-section">
          <h2>Метрики системы</h2>
          
          <div className="health-section">
            <h3>Состояние системы</h3>
            {health && (
              <div className={`health-status ${health.status?.toLowerCase()}`}>
                <strong>Status:</strong> {health.status}
              </div>
            )}
            <button className="btn-secondary" onClick={loadHealth}>
              Обновить
            </button>
          </div>

          <div className="metrics-section">
            <h3>Доступные метрики</h3>
            <div className="metrics-controls">
              <select
                value={selectedMetric}
                onChange={(e) => {
                  setSelectedMetric(e.target.value)
                  if (e.target.value) {
                    loadMetricDetails(e.target.value)
                  } else {
                    setMetricDetails(null)
                  }
                }}
                className="metric-select"
              >
                <option value="">Выберите метрику</option>
                {metrics.map(metric => (
                  <option key={metric.name} value={metric.name}>
                    {metric.name}
                  </option>
                ))}
              </select>
              <button className="btn-secondary" onClick={loadMetrics} disabled={loadingMetrics}>
                {loadingMetrics ? 'Загрузка...' : 'Обновить список'}
              </button>
            </div>

            {metricDetails && (
              <div className="metric-details">
                <h4>{metricDetails.name}</h4>
                <p className="metric-description">{metricDetails.description || 'Нет описания'}</p>
                {metricDetails.measurements && (
                  <div className="metric-measurements">
                    <h5>Измерения:</h5>
                    <table className="metric-table">
                      <thead>
                        <tr>
                          <th>Статистика</th>
                          <th>Значение</th>
                        </tr>
                      </thead>
                      <tbody>
                        {metricDetails.measurements.map((m: any, idx: number) => (
                          <tr key={idx}>
                            <td>{m.statistic}</td>
                            <td>{typeof m.value === 'number' ? m.value.toFixed(2) : m.value}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

