import { Group } from "@/types/group.type";
import { State, StateDefault } from "@/types/state.type";
import { createContext, ReactNode, useContext, useState } from "react";

interface IGroupContext {
  groups: Group[];
  state: State;
  keyword: number;
  setKeyword: React.Dispatch<React.SetStateAction<number>>;
  setGroups: React.Dispatch<React.SetStateAction<Group[]>>;
}

const GroupContext = createContext<IGroupContext>({
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

  return {
    groups, state, setKeyword, keyword, setGroups
  }
}