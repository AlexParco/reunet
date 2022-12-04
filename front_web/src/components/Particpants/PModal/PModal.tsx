import { useGroup } from '@/context/Group.context'
import { useParticipant } from '@/context/Participant.context'
import { User } from '@/types/user.type'
import {
  Modal,
  Text,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  Button,
  useDisclosure,
  Input,
  Box,
} from '@chakra-ui/react'
import { useState } from 'react'

const PModal = ({ users }: { users: User[] }) => {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const [query, setQuery] = useState<string>("")
  const { keyword } = useGroup()
  const { addUser } = useParticipant()

  const handleClick = () => {
    const userTemp: User = users.filter(e => e.email == query)[0]

    addUser({ user_id: userTemp.id, group_id: keyword })
    setQuery("")
    onClose()
  }

  return (
    <>
      <Button variant='outline' shadow='sm' onClick={onOpen} mt='6' >+ Participante</Button>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Search User</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Input type="text"
              value={query}
              placeholder='Enter a firstname '
              onChange={(e) => setQuery(e.target.value)}
              mb='6'
              w='100%' />
            {users.filter(user => {
              if (query === '') {
                return user
              } else if (
                user.email?.toLocaleLowerCase().includes(query.toLocaleLowerCase())
              ) {
                return user
              }

            }).map((user, i) =>
              <Box key={i} p='1'>
                <Text
                  cursor='pointer'
                  onClick={() => setQuery(user.email)}
                >{user.email}</Text>
              </Box>
            )}
          </ModalBody>
          <ModalFooter>
            <Button variant='outline' colorScheme='red' mr={3} onClick={onClose}>
              Close
            </Button>
            <Button variant='outline' colorScheme='blue' onClick={() => handleClick()} >Add User</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  )
}

export default PModal