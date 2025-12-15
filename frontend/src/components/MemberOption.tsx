import { useState, useEffect } from 'react'
import { apiService } from '../services/api'
import { GroupMemberDto, UserDto } from '../types'

interface MemberOptionProps {
  member: GroupMemberDto
}

export default function MemberOption({ member }: MemberOptionProps) {
  const [userProfile, setUserProfile] = useState<UserDto | null>(null)

  useEffect(() => {
    loadUserProfile()
  }, [member.user.id])

  const loadUserProfile = async () => {
    try {
      const profile = await apiService.getUserById(member.user.id)
      setUserProfile(profile)
    } catch (err) {
      // Игнорируем ошибку, используем только username
    }
  }

  const displayName = userProfile?.profile
    ? `${userProfile.profile.firstName} ${userProfile.profile.lastName} (${member.user.username})`
    : member.user.username

  return <option value={member.user.id}>{displayName}</option>
}

