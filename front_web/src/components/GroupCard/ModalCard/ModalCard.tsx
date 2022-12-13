import { Activity } from "@/components/Activity"
import { useAuth } from "@/context/Auth.context"
import { useGroup } from "@/hooks/useGroup"
import { Group } from "@/types/group.type"
import { SettingsIcon } from "@chakra-ui/icons"
import {
  Button, FormControl, IconButton, Input, Modal, ModalBody,
  ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Text, useDisclosure
} from "@chakra-ui/react"
import { userInfo } from "os"
import { useState } from "react"
import { BiPencil } from 'react-icons/bi'

interface Props {
  keyword: number,
  group: Group
}

const ModalCard = ({ keyword, group }: Props) => {
  const [state1, setState1] = useState<Boolean>(false)
  const [state2, setState2] = useState<Boolean>(false)
  const { isOpen, onOpen, onClose } = useDisclosure()
  const { user } = useAuth()
  const { deleteGroup, editGroup } = useGroup()
  const [name, setName] = useState<string>("")
  const [description, setDescription] = useState<string>("")


  const handleDelete = () => {
    deleteGroup(keyword)
    onClose()
  }

  const handleSubmitDsrp = (e: React.FormEvent<HTMLDivElement>) => {
    e.preventDefault()
    editGroup({ ...group, description })
    setState2(false)
    setDescription("")
  }

  const handleSubmitName = (e: React.FormEvent<HTMLDivElement>) => {
    e.preventDefault()
    editGroup({ ...group, name })
    setState1(false)
    setName("")
  }

  return (
    <>
      <IconButton
        onClick={() => onOpen()}
        top='2'
        right='2'
        position='absolute'
        variant='ghost'
        colorScheme='whiteAlpha.900'
        aria-label="delete-group"
        icon={<SettingsIcon />}
        size='' />
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>
            <FormControl as='form' onSubmit={(e) => handleSubmitName(e)}>
              {
                !state1 ? group.name
                  : <Input
                    value={name}
                    size='sm'
                    onChange={(e) => setName(e.target.value)}
                    w='40%' />
              }
              {
                (group.user_id === user.id) &&
                <IconButton
                  size='sm'
                  variant='ghot'
                  ml='2'
                  onClick={() => setState1(!state1)}
                  color='grey'
                  aria-label="edit-group"
                  icon={<BiPencil />}
                />
              }
            </FormControl>
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Text as='b'>Descripcion</Text>
            {

              (group.user_id === user.id) &&
              <IconButton
                size='sm'
                variant='ghot'
                ml='2'
                onClick={() => {
                  setState2(!state2)
                }}
                color='grey'
                aria-label="edit-group"
                icon={<BiPencil />}
              />
            }
            <FormControl as='form' mb='8' onSubmit={handleSubmitDsrp}>
              <Text>
                {
                  !state2 ? group.description
                    : <Input
                      value={description}
                      onChange={(e) => setDescription(e.target.value)}
                      size='sm'
                      w='70%' />
                }
              </Text>
            </FormControl>
            <Activity keyword={keyword} group={group} />
          </ModalBody>

          <ModalFooter>
            <Button colorScheme='blue' mr={3} onClick={onClose}>
              Close
            </Button>
            {
              (group.user_id === user.id) && <Button colorScheme='red' onClick={() => handleDelete()} >Delete Group</Button>
            }
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  )
}

export default ModalCard