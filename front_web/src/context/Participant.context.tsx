import { addParticipanat, deleteParticipant, findAllPrtByGrpId } from "@/services/participant.service";
import { findUserById } from "@/services/user.service";
import { Participant } from "@/types/participant.type";
import { User } from "@/types/user.type";
import { createContext, ReactNode, useContext, useEffect, useState } from "react";
import { useAuth } from "./Auth.context";
import { useGroup } from "./Group.context";

interface IParticipantContext {
  participants: User[];
  addUser: (participant: Participant) => void;
  deleteUser: (participantId: number) => void;
}

const ParticipantContext = createContext<IParticipantContext>({
  participants: [],
  addUser: () => null,
  deleteUser: () => null,
})

export const ParticipantProvider = ({ children }: { children: ReactNode }) => {
  const [participants, setParticipants] = useState<User[]>([])
  const { token } = useAuth()
  const { keyword } = useGroup()

  useEffect(() => {
    const fetchData = async () => {
      try {
        let usersTemp: User[] = []
        const users = await findAllPrtByGrpId(keyword, token)
        await Promise.all(users.data.map(async (user) => {
          const data = await findUserById(user.user_id as number, token)
          usersTemp.push(data.data)
        }))
        setParticipants(usersTemp)

      } catch (error) {
        console.log(error)
      }
    }
    fetchData()
  }, [keyword])

  const deleteUser = (participantId: number) => {
    async function fetchData() {
      try {
        const users = await findAllPrtByGrpId(keyword, token)
        const user = users.data.find(e => e.user_id == participantId)
        await deleteParticipant(user?.participant_id as number, token)

        setParticipants((prev) => prev.filter(e => e.id != participantId))

      } catch (error) {

        console.log(error)
      }
    }
    fetchData()
  }

  const addUser = (participant: Participant) => {
    addParticipanat(participant, token)
      .then(async (resp) => {
        const user = await findUserById(resp.data.user_id as number, token)
        setParticipants([...participants, user.data])
      })
      .catch(error => console.log(error))
  }


  return <ParticipantContext.Provider value={{ participants, addUser, deleteUser }}>
    {children}
  </ParticipantContext.Provider>
}

export const useParticipant = () => {
  const { participants, addUser, deleteUser } = useContext(ParticipantContext)
  return {
    participants, addUser, deleteUser
  }
} 