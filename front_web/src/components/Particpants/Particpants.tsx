import { useAuth } from "@/context/Auth.context"
import { useParticipant } from "@/context/Participant.context"
import { findAllUsers } from "@/services/user.service"
import { User } from "@/types/user.type"
import { SmallCloseIcon } from "@chakra-ui/icons"
import { Box, Flex, Heading, IconButton, Stack, Text } from "@chakra-ui/react"
import { useEffect, useState } from "react"
import { PModal } from "./PModal"

const Particpants = ({ participants }: { participants: User[] }) => {
  const [users, setUsers] = useState<User[]>([])
  const { deleteUser } = useParticipant()
  const { token, user } = useAuth()

  useEffect(() => {
    findAllUsers(token)
      .then(resp => {
        setUsers(
          resp.data.filter(
            user => !participants.some(prt => prt.email == user.email
            ))
        )
      })
      .catch(error => console.log(error))
  }, [participants])

  return (
    <Box m='4' borderLeft='1px' px='8' >
      <Heading size='md' pb='6'>Participantes</Heading>
      <Stack gap='2'>
        {participants.map((userp, i) =>
          <Flex justify='space-between' align='center' key={i}>
            <Text >{userp.firstname}</Text>
            {
              user.id !== userp.id &&
              <IconButton
                aria-label="delete-user"
                colorScheme='red'
                variant='ghost'
                onClick={() => deleteUser(user.id as number)}
                size='sm'
                icon={<SmallCloseIcon />} />
            }
          </Flex>
        )}
      </Stack>
      <PModal users={users} />
    </Box>
  )
}
export default Particpants