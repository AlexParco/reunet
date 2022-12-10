import { useAuth } from "@/context/Auth.context"
import { useGroup } from "@/hooks/useGroup"
import { findAllActivities } from "@/services/acitivity.service"
import { Activity } from "@/types/activity.type"
import { Group } from "@/types/group.type"
import { SettingsIcon } from "@chakra-ui/icons"
import {
  Button, FormControl, IconButton, Input, Modal, ModalBody,
  ModalCloseButton, ModalContent, ModalFooter, ModalHeader, ModalOverlay, Text, useDisclosure
} from "@chakra-ui/react"
import { useEffect, useState } from "react"
import { BiPencil } from 'react-icons/bi'

interface Props {
  keyword: number,
  group: Group
}

const ModalCard = ({ keyword, group }: Props) => {
  const [state1, setState1] = useState<Boolean>(false)
  const [state2, setState2] = useState<Boolean>(false)
  const { isOpen, onOpen, onClose } = useDisclosure()
  const { deleteGroup, editGroup } = useGroup()
  const { token } = useAuth()
  const [activities, setActivities] = useState<Activity[]>([])
  const [name, setName] = useState<string>("")
  const [description, setDescription] = useState<string>("")

  useEffect(() => {
    findAllActivities(group.id, token)
      .then(_ => {
        console.log(activities)
      })
  }, [keyword])

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
              <IconButton
                size='sm'
                variant='ghot'
                ml='2'
                onClick={() => setState1(!state1)}
                color='grey'
                aria-label="edit-group"
                icon={<BiPencil />}
              />
            </FormControl>
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Text as='b'>Descripcion</Text>
            <FormControl as='form' onSubmit={handleSubmitDsrp}>
              <Text>
                {
                  !state2 ? group.description
                    : <Input
                      value={description}
                      onChange={(e) => setDescription(e.target.value)}
                      size='sm'
                      w='70%' />
                }
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
              </Text>
            </FormControl>
            <Text as='b' mt='6'>Activities</Text>
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
            <Text>

              {!(activities.length === 0) ?
                activities.map(e =>
                  <li>{e.name}</li>
                )
                : "No hay actividades"
              }
            </Text>
          </ModalBody>

          <ModalFooter>
            <Button colorScheme='blue' mr={3} onClick={onClose}>
              Close
            </Button>
            <Button colorScheme='red' onClick={() => handleDelete()} >Delete Group</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  )
}

export default ModalCard