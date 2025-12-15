import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { apiService } from '../services/api'
import { JwtRequest, JwtResponse, UserDto } from '../types'

interface AuthState {
  user: UserDto | null
  userId: number | null
  isAuthenticated: boolean
  accessToken: string | null
  refreshToken: string | null
  login: (request: JwtRequest) => Promise<void>
  logout: () => void
  setUser: (user: UserDto) => void
  setUserId: (id: number) => void
  setTokens: (tokens: { accessToken: string; refreshToken: string }) => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      userId: null,
      isAuthenticated: false,
      accessToken: localStorage.getItem('accessToken'),
      refreshToken: localStorage.getItem('refreshToken'),

      login: async (request: JwtRequest) => {
        try {
          const response: JwtResponse = await apiService.login(request)
          localStorage.setItem('accessToken', response.accessToken)
          localStorage.setItem('refreshToken', response.refreshToken)
          
          const user = await apiService.getUserById(response.id)
          
          set({
            userId: response.id,
            user,
            isAuthenticated: true,
            accessToken: response.accessToken,
            refreshToken: response.refreshToken,
          })
        } catch (error) {
          console.error('Login error:', error)
          throw error
        }
      },

      logout: () => {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        set({
          user: null,
          userId: null,
          isAuthenticated: false,
          accessToken: null,
          refreshToken: null,
        })
      },

      setUser: (user: UserDto) => {
        set({ user })
      },

      setUserId: (id: number) => {
        set({ userId: id })
      },

      setTokens: (tokens: { accessToken: string; refreshToken: string }) => {
        localStorage.setItem('accessToken', tokens.accessToken)
        localStorage.setItem('refreshToken', tokens.refreshToken)
        set({
          accessToken: tokens.accessToken,
          refreshToken: tokens.refreshToken,
        })
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        userId: state.userId,
        isAuthenticated: state.isAuthenticated,
        user: state.user,
      }),
    }
  )
)

