import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios'
import { 
  JwtRequest, 
  JwtResponse, 
  UserRegisterDto,
  UserDto,
  UserUpdateDto,
  ChangePasswordRequest,
  GoalDto,
  CreateGoalDto,
  UpdateGoalDto,
  CreateSubGoalDto,
  UpdateSubGoalDto,
  SubGoalDto,
  GroupDto,
  CreateGroupDto,
  GroupMemberDto,
  GroupInvocationDto,
  CreateGroupInvocationDto,
  OverallStatisticsDto,
  CategoryStatisticsDto,
  GoalStatus,
  GoalCategory,
  GroupStatus,
  GroupRole,
  GroupInvitationStatus,
  Role,
  UserStatus,
} from '../types'

const API_BASE_URL = '/api/v1'

class ApiService {
  private api: AxiosInstance

  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    })

    // Request interceptor to add auth token
    this.api.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('accessToken')
        if (token && config.headers) {
          config.headers.Authorization = `Bearer ${token}`
        }
        return config
      },
      (error) => Promise.reject(error)
    )

    // Response interceptor for token refresh
    this.api.interceptors.response.use(
      (response) => response,
      async (error) => {
        const originalRequest = error.config

        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true

          try {
            const refreshToken = localStorage.getItem('refreshToken')
            if (refreshToken) {
              const response = await axios.post<JwtResponse>(
                `${API_BASE_URL}/auth/refresh`,
                refreshToken
              )
              const { accessToken, refreshToken: newRefreshToken } = response.data
              localStorage.setItem('accessToken', accessToken)
              localStorage.setItem('refreshToken', newRefreshToken)
              
              if (originalRequest.headers) {
                originalRequest.headers.Authorization = `Bearer ${accessToken}`
              }
              return this.api(originalRequest)
            }
          } catch (refreshError) {
            localStorage.removeItem('accessToken')
            localStorage.removeItem('refreshToken')
            window.location.href = '/login'
            return Promise.reject(refreshError)
          }
        }

        return Promise.reject(error)
      }
    )
  }

  // Auth
  async login(request: JwtRequest): Promise<JwtResponse> {
    const response = await this.api.post<JwtResponse>('/auth/login', request)
    return response.data
  }

  async register(request: UserRegisterDto): Promise<UserRegisterDto> {
    const response = await this.api.post<UserRegisterDto>('/auth/register', request)
    return response.data
  }

  async refresh(refreshToken: string): Promise<JwtResponse> {
    // Backend expects string in body - axios will JSON.stringify it automatically
    const response = await this.api.post<JwtResponse>('/auth/refresh', refreshToken)
    return response.data
  }

  // Users
  async getUserById(id: number): Promise<UserDto> {
    const response = await this.api.get<UserDto>(`/user/${id}`)
    return response.data
  }

  async getAllUsers(): Promise<UserDto[]> {
    const response = await this.api.get<UserDto[]>('/user/all')
    return response.data
  }

  async updateUser(id: number, dto: UserUpdateDto): Promise<UserDto> {
    const response = await this.api.patch<UserDto>(`/user/${id}`, dto)
    return response.data
  }

  async deleteUser(id: number): Promise<void> {
    await this.api.delete(`/user/${id}`)
  }

  async changePassword(request: ChangePasswordRequest): Promise<void> {
    await this.api.post('/user/change/password', request)
  }

  async addRole(id: number, role: Role): Promise<UserDto> {
    const response = await this.api.patch<UserDto>(`/user/set-role/${id}`, null, {
      params: { role },
    })
    return response.data
  }

  async setUserStatus(id: number, status: UserStatus): Promise<UserDto> {
    const response = await this.api.patch<UserDto>(`/user/set-user-status/${id}`, null, {
      params: { userStatus: status },
    })
    return response.data
  }

  // Actuator endpoints
  async getHealth(): Promise<any> {
    const response = await this.api.get('/actuator/health')
    return response.data
  }

  async getMetrics(): Promise<any> {
    const response = await this.api.get('/actuator/metrics')
    return response.data
  }

  async getMetric(metricName: string): Promise<any> {
    const response = await this.api.get(`/actuator/metrics/${metricName}`)
    return response.data
  }

  async getInfo(): Promise<any> {
    const response = await this.api.get('/actuator/info')
    return response.data
  }

  // Goals
  async getGoalById(id: number): Promise<GoalDto> {
    const response = await this.api.get<GoalDto>(`/goal/${id}`)
    return response.data
  }

  async getAllGoalsByGroupId(groupId: number): Promise<GoalDto[]> {
    const response = await this.api.get<GoalDto[]>(`/goal/all/group/${groupId}`)
    return response.data
  }

  async getFilteredGoals(
    userId: number,
    status?: GoalStatus,
    category?: GoalCategory,
    deadline?: string
  ): Promise<GoalDto[]> {
    const params: Record<string, string> = {}
    if (status) params.status = status
    if (category) params.category = category
    if (deadline) params.deadline = deadline

    const response = await this.api.get<GoalDto[]>(`/goal/filtered/${userId}`, { params })
    return response.data
  }

  async createGoal(userId: number, dto: CreateGoalDto): Promise<number> {
    const response = await this.api.post<number>(`/goal?userId=${userId}`, dto)
    return response.data
  }

  async createGroupGoal(userId: number, groupId: number, dto: CreateGoalDto): Promise<number> {
    const response = await this.api.post<number>(
      `/goal/create/group?userId=${userId}&groupId=${groupId}`,
      dto
    )
    return response.data
  }

  async updateGoal(id: number, dto: UpdateGoalDto): Promise<UpdateGoalDto> {
    const response = await this.api.patch<UpdateGoalDto>(`/goal/${id}`, dto)
    return response.data
  }

  async deleteGoal(id: number): Promise<void> {
    await this.api.delete(`/goal/${id}`)
  }

  async updateGoalStatus(id: number, status: GoalStatus): Promise<GoalDto> {
    const response = await this.api.patch<GoalDto>(`/goal/status/${id}`, null, {
      params: { status },
    })
    return response.data
  }

  async addSubGoal(goalId: number, dto: CreateSubGoalDto): Promise<GoalDto> {
    const response = await this.api.patch<GoalDto>(`/goal/${goalId}/sub-goal/add`, dto)
    return response.data
  }

  async removeSubGoal(goalId: number, subGoalId: number): Promise<GoalDto> {
    const response = await this.api.patch<GoalDto>(`/goal/${goalId}/sub-goal/remove/${subGoalId}`)
    return response.data
  }

  // SubGoals
  async getSubGoalsByGoalId(goalId: number): Promise<SubGoalDto[]> {
    const response = await this.api.get<SubGoalDto[]>(`/sub-goals/by-goal-id/${goalId}`)
    return response.data
  }

  async updateSubGoal(id: number, dto: UpdateSubGoalDto): Promise<UpdateSubGoalDto> {
    const response = await this.api.patch<UpdateSubGoalDto>(`/sub-goals/${id}`, dto)
    return response.data
  }

  async updateSubGoalStatus(id: number, status: GoalStatus): Promise<SubGoalDto> {
    const response = await this.api.patch<SubGoalDto>(`/sub-goals/${id}/status`, null, {
      params: { status },
    })
    return response.data
  }

  async deleteSubGoal(id: number): Promise<void> {
    await this.api.delete(`/sub-goals/${id}`)
  }

  // Groups
  async getGroupById(id: number): Promise<GroupDto> {
    const response = await this.api.get<GroupDto>(`/group/${id}`)
    return response.data
  }

  async getAllGroups(): Promise<GroupDto[]> {
    const response = await this.api.get<GroupDto[]>('/group/all')
    return response.data
  }

  async getFilteredGroups(
    status?: string,
    category?: string,
    deadline?: string
  ): Promise<GroupDto[]> {
    const params: Record<string, string> = {}
    if (status) params.status = status
    if (category) params.category = category
    if (deadline) params.deadline = deadline

    const response = await this.api.get<GroupDto[]>('/group/filtered', { params })
    return response.data
  }

  async getUserGroups(userId: number): Promise<GroupDto[]> {
    const response = await this.api.get<GroupDto[]>(`/group/user-id/${userId}`)
    return response.data
  }

  async createGroup(userId: number, dto: CreateGroupDto): Promise<number> {
    const response = await this.api.post<number>(`/group/owner/${userId}`, dto)
    return response.data
  }

  async deleteGroup(id: number): Promise<void> {
    await this.api.delete(`/group/${id}`)
  }

  async setGroupStatus(id: number, status: GroupStatus): Promise<void> {
    // Бэкенд возвращает некорректный ответ из-за бага в mapper, поэтому просто проверяем статус
    await this.api.patch(`/group/set-group-status/${id}`, null, {
      params: { groupStatus: status },
    })
  }

  async addMember(groupId: number, userId: number): Promise<void> {
    await this.api.patch(`/group/${groupId}/user-id/${userId}`)
  }

  async deleteMember(groupId: number, userId: number): Promise<void> {
    await this.api.delete(`/group/${groupId}/user-id/${userId}`)
  }

  async setMemberRole(
    groupId: number,
    userId: number,
    role: GroupRole
  ): Promise<GroupMemberDto> {
    const response = await this.api.patch<GroupMemberDto>(
      `/group/${groupId}/user-id/${userId}/role`,
      null,
      { params: { groupRole: role } }
    )
    return response.data
  }

  // Notifications
  async getGroupInvocations(username: string): Promise<GroupInvocationDto[]> {
    const response = await this.api.get<GroupInvocationDto[]>('/notify/all/invocation', {
      params: { username },
    })
    return response.data
  }

// Notifications
  async inviteToGroup(
      groupId: number,
      usernameToInvite: string,
      expiresAt: string,
      currentUserEmail: string
  ): Promise<number> {
    if (!currentUserEmail) {
      throw new Error('Текущий пользователь не авторизован')
    }

    // DTO для запроса
    const body = { username: usernameToInvite, expiresAt }

    const response = await this.api.post<number>(
        `/notify/invoke/${groupId}`,
        body,
        { params: { invokeByUsername: currentUserEmail } }
    )
    return response.data
  }


  async updateInvocationStatus(
    id: number,
    status: GroupInvitationStatus
  ): Promise<void> {
    await this.api.patch(`/notify/change/${id}/status`, null, { params: { status } })
  }

  // Statistics
  async getUserOverallStatistics(userId: number): Promise<OverallStatisticsDto> {
    const response = await this.api.get<OverallStatisticsDto>(
      `/statistics/user/${userId}/overall`
    )
    return response.data
  }

  async getUserStatisticsByCategory(userId: number): Promise<CategoryStatisticsDto[]> {
    const response = await this.api.get<CategoryStatisticsDto[]>(
      `/statistics/user/${userId}/categories`
    )
    return response.data
  }

  async getGroupStatistics(groupId: number): Promise<OverallStatisticsDto> {
    const response = await this.api.get<OverallStatisticsDto>(
      `/statistics/group/${groupId}/overall`
    )
    return response.data
  }
}

export const apiService = new ApiService()

