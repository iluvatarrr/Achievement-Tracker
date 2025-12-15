import { useState, useEffect } from 'react'
import { apiService } from '../services/api'
import { GroupMemberDto, UserDto } from '../types'
import './MemberCard.css'

interface MemberCardProps {
  member: GroupMemberDto
  canDelete: boolean
  onDelete: () => void
}

export default function MemberCard({ member, canDelete, onDelete }: MemberCardProps) {
  const [userProfile, setUserProfile] = useState<UserDto | null>(null)

  useEffect(() => {
    loadUserProfile()
  }, [member.user.id])

  const loadUserProfile = async () => {
    try {
      const profile = await apiService.getUserById(member.user.id)
      setUserProfile(profile)
    } catch (err) {
      console.error('Ошибка загрузки профиля пользователя')
    }
  }

  return (
    <div className="member-card">
      <div className="member-info">
        <div className="member-main">
          <span className="member-name">{member.user.username}</span>
          {userProfile?.profile && (
            <span className="member-fullname">
              {userProfile.profile.firstName} {userProfile.profile.lastName}
            </span>
          )}
        </div>
        <span className={`member-role ${member.role.toLowerCase()}`}>
          {member.role}
        </span>
      </div>
      {canDelete && (
        <button className="btn-delete-member" onClick={onDelete}>
          Удалить
        </button>
      )}
    </div>
  )
}

