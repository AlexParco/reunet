import { deleteGruop, findAllGroups } from "@/services/group.service";
import { findAllPrtByGrpId } from "@/services/participant.service";
import { Group } from "@/types/group.type";
import { State, StateDefault } from "@/types/state.type";
import { sortGroups } from "@/util/sortArray";
import { createContext, ReactNode, useContext, useEffect, useState } from "react";
import { useAuth } from "./Auth.context";

interface IGroupContext {
  groups: Group[];
  state: State;
  keyword: number;
  setKeyword: React.Dispatch<React.SetStateAction<number>>;
  setGroups: React.Dispatch<React.SetStateAction<Group[]>>;
}

export const GroupContext = createContext<IGroupContext>({
  groups: [],
  state: StateDefault,
  keyword: 0,
  setKeyword: () => null,
  setGroups: () => null
})

export const GroupProvider = ({ children }: { children: ReactNode }) => {
  const [groups, setGroups] = useState<Group[]>([])
  const [state, setState] = useState<State>(StateDefault)
  const [keyword, setKeyword] = useState<number>(0)


  return (
    <GroupContext.Provider value={{ groups, state, setKeyword, keyword, setGroups }}>
      {children}
    </GroupContext.Provider>
  )
}

export const useGroup = () => {
  const { groups, state, setKeyword, keyword, setGroups } = useContext(GroupContext)
  const { token, user } = useAuth()

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
        setGroups((prev) => prev.filter(e => e.id == id))
      })
      .catch(error => {
        console.log(error)
      })
  }


  return {
    groups, state, setKeyword, keyword, setGroups, deleteGroup
  }
}
