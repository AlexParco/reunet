import { useAuth } from '@/context/Auth.context'
import { GroupContext } from '@/context/Group.context'
import { useParticipant } from '@/context/Participant.context'
import { createGroup, deleteGruop, findAllGroups, editGroup as eGroup } from '@/services/group.service'
import { findAllPrtByGrpId } from '@/services/participant.service'
import { Group } from '@/types/group.type'
import { sortGroups } from '@/util/sortArray'
import { useContext, useEffect } from 'react'

export const useGroup = () => {
  const { groups, state, setKeyword, keyword, setGroups } = useContext(GroupContext)
  const { token, user } = useAuth()
  const { addUser } = useParticipant()

  useEffect(() => {
    if (!token) { return setGroups([]) }
    (async () => {
      try {
        const groups = await findAllGroups(token)
        let tempGroups: Group[] = []
        await Promise.all(groups.data.map(async (group) => {
          const data = await findAllPrtByGrpId(group.id as number, token)
          if (data.data.some(e => e.user_id == user.id)) {
            tempGroups.push(group)
          }
        }))
        setGroups(sortGroups(tempGroups))
      } catch (error) {
        console.log(error)
      }
    })()

  }, [setGroups])

  const deleteGroup = (id: number) => {
    deleteGruop(token, id)
      .then(_ => {
        setGroups((prev) => prev.filter(e => e.id != id))
      })
      .catch(error => {
        console.log(error)
      })
  }

  const addGroup = (group: Group) => {
    (async () => {
      try {
        const resp = await createGroup(group, token)
        setGroups([...groups, resp.data])
        addUser({ group_id: resp.data.id, user_id: user.id })
      } catch (error) {
        console.log(error)
      }
    })()
  }

  // NOTE: slow reload all groups 
  const editGroup = (group: Group) => {
    (async () => {
      try {
        const data = await eGroup(group, token)
        setGroups([...groups, data.data])
      } catch (error) {
        console.log(error)
      }
    })()
  }

  return {
    groups, state, setKeyword, keyword, setGroups, deleteGroup, addGroup, editGroup
  }
}
