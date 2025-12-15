import { useState, useEffect } from 'react'
import { apiService } from '../services/api'
import { useAuthStore } from '../store/authStore'
import { OverallStatisticsDto, CategoryStatisticsDto } from '../types'
import './StatisticsPage.css'

export default function StatisticsPage() {
  const [overallStats, setOverallStats] = useState<OverallStatisticsDto | null>(null)
  const [categoryStats, setCategoryStats] = useState<CategoryStatisticsDto[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  
  const { userId } = useAuthStore()

  useEffect(() => {
    if (userId) {
      loadStatistics()
    }
  }, [userId])

  const loadStatistics = async () => {
    if (!userId) return
    
    try {
      setLoading(true)
      const [overall, categories] = await Promise.all([
        apiService.getUserOverallStatistics(userId),
        apiService.getUserStatisticsByCategory(userId),
      ])
      setOverallStats(overall)
      setCategoryStats(categories)
    } catch (err: any) {
      setError('Ошибка загрузки статистики')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const getCategoryName = (category: string) => {
    const names: Record<string, string> = {
      SPORT: 'Спорт',
      HEALTH: 'Здоровье',
      CAREER: 'Карьера',
      EDUCATIONAL: 'Образование',
    }
    return names[category] || category
  }

  if (loading) return <div className="loading">Загрузка...</div>
  if (error) return <div className="error">{error}</div>
  if (!overallStats) return <div className="empty-state">Нет данных</div>

  return (
    <div className="statistics-page">
      <h1>Статистика</h1>

      <div className="statistics-card">
        <h2>Общая статистика</h2>
        <div className="stats-grid">
          <div className="stat-item">
            <div className="stat-value">{overallStats.totalGoals}</div>
            <div className="stat-label">Всего целей</div>
          </div>
          <div className="stat-item completed">
            <div className="stat-value">{overallStats.doneGoals}</div>
            <div className="stat-label">Выполнено</div>
          </div>
          <div className="stat-item in-progress">
            <div className="stat-value">{overallStats.totalGoals - overallStats.doneGoals}</div>
            <div className="stat-label">В процессе</div>
          </div>
          <div className="stat-item">
            <div className="stat-value">{overallStats.averageProgress.toFixed(1)}%</div>
            <div className="stat-label">Средний прогресс</div>
          </div>
        </div>

        <div className="progress-bar-large">
          <div
            className="progress-fill-large"
            style={{
              width: `${overallStats.averageProgress}%`,
              backgroundColor: overallStats.averageProgress >= 80 ? '#27ae60' :
                              overallStats.averageProgress >= 50 ? '#f39c12' : '#e74c3c',
            }}
          />
        </div>
      </div>

      <div className="statistics-card">
        <h2>Статистика по категориям</h2>
        <div className="category-stats">
          {categoryStats.length > 0 ? (
            categoryStats.map((catStat) => (
              <div key={catStat.category} className="category-stat-item">
                <h3>{getCategoryName(catStat.category)}</h3>
                <div className="category-stat-grid">
                  <div className="category-stat-value">
                    <span className="value">{catStat.totalGoals}</span>
                    <span className="label">Всего</span>
                  </div>
                  <div className="category-stat-value">
                    <span className="value">{catStat.doneGoals}</span>
                    <span className="label">Выполнено</span>
                  </div>
                  <div className="category-stat-value">
                    <span className="value">{catStat.totalGoals - catStat.doneGoals}</span>
                    <span className="label">В процессе</span>
                  </div>
                  <div className="category-stat-value">
                    <span className="value">{catStat.averageProgress.toFixed(1)}%</span>
                    <span className="label">Прогресс</span>
                  </div>
                </div>
                <div className="category-progress-bar">
                  <div
                    className="category-progress-fill"
                    style={{ width: `${catStat.averageProgress}%` }}
                  />
                </div>
              </div>
            ))
          ) : (
            <div className="empty-state">Нет статистики по категориям</div>
          )}
        </div>
      </div>
    </div>
  )
}

