// Enums
export enum GoalStatus {
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE',
  EXPIRED = 'EXPIRED',
  REJECTED = 'REJECTED',
}

export enum GoalCategory {
  SPORT = 'SPORT',
  HEALTH = 'HEALTH',
  CAREER = 'CAREER',
  EDUCATIONAL = 'EDUCATIONAL',
}

export enum GoalType {
  GROUP = 'GROUP',
  SELF = 'SELF',
}

export enum GroupStatus {
  ACTIVE = 'ACTIVE',
  ARCHIVED = 'ARCHIVED',
}

export enum GroupRole {
  OWNER = 'OWNER',
  MODERATOR = 'MODERATOR',
  MEMBER = 'MEMBER',
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  ARCHIVED = 'ARCHIVED',
}

export enum Role {
  ROLE_USER = 'ROLE_USER',
  ROLE_ADMIN = 'ROLE_ADMIN',
}

export enum GroupInvitationStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  DECLINED = 'DECLINED',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED',
}

// DTOs
export interface UserProfileDto {
  firstName: string
  lastName: string
}

export interface UserDto {
  id?: number
  email: string
  password?: string
  createdAt: string
  roles: Role[]
  profile: UserProfileDto
  userStatus: UserStatus
}

export interface UserRegisterDto {
  email: string
  password: string
}

export interface UserUpdateDto {
  email?: string
  profile?: UserProfileDto
}

export interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
}

export interface JwtRequest {
  email: string
  password: string
}

export interface JwtResponse {
  id: number
  username: string
  accessToken: string
  refreshToken: string
}

export interface SubGoalDto {
  id: number
  title: string
  description: string
  goalStatus: GoalStatus
  createdAt: string
  completedAt: string | null
  deadline: string
}

export interface CreateSubGoalDto {
  title: string
  description?: string
  deadline: string
}

export interface UpdateSubGoalDto {
  title: string
  description?: string
  goalStatus: GoalStatus
  deadline: string
}

export interface GoalDto {
  id: number
  title: string
  description: string
  goalStatus: GoalStatus
  goalType: GoalType
  goalCategory: GoalCategory
  progressInPercent: number
  createdAt: string
  completedAt: string | null
  deadline: string
  subGoalList: SubGoalDto[]
}

export interface CreateGoalDto {
  title: string
  description: string
  goalStatus: GoalStatus
  goalType: GoalType
  goalCategory: GoalCategory
  deadline: string
  subGoalList?: CreateSubGoalDto[]
}

export interface UpdateGoalDto {
  title: string
  description?: string
  goalStatus: GoalStatus
  goalCategory: GoalCategory
  deadline: string
  subGoalList?: UpdateSubGoalDto[]
}

export interface UserInfo {
  id: number
  username: string
}

export interface GroupMemberDto {
  id: number
  user: UserInfo
  role: GroupRole
}

export interface GroupDto {
  id: number
  title: string
  description: string
  organisation: string
  isPublic: boolean
  groupStatus: GroupStatus
  createdBy: UserInfo
  createdAt: string
  members: GroupMemberDto[]
}

export interface CreateGroupDto {
  title: string
  description?: string
  organisation?: string
  isPublic: boolean
}

export interface GroupInvocationDto {
  id: number
  groupName: string
  username: string
  invitedByName: string
  status: GroupInvitationStatus
  createdAt: string
  expiresAt: string
}

export interface CreateGroupInvocationDto {
  username: string
  expiresAt: string
}

export interface OverallStatisticsDto {
  totalGoals: number
  doneGoals: number
  averageProgress: number
}

export interface CategoryStatisticsDto {
  category: GoalCategory
  totalGoals: number
  doneGoals: number
  averageProgress: number
}

