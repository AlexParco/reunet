import { useAuth } from '@/context/Auth.context'
import { findAllActivities } from '@/services/acitivity.service'
import { Activity } from '@/types/activity.type'
import { useEffect, useState } from 'react'

export const useActivity = (keyword: number) => {
  const [activities, setActivities] = useState<Activity[]>([])
  const { token } = useAuth()

  useEffect(() => {
    findAllActivities(keyword, token)
      .then(resp => {
        setActivities(resp.data)
      })
      .catch(error => {
        console.log(error)
      })
  }, [keyword, setActivities])

  return {
    activities
  }
} 
