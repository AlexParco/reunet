import { useAuth } from '@/context/Auth.context'
import { createActivity, deleteActivity, findAllActivities } from '@/services/acitivity.service'
import { Activity as TypeActivity } from "@/types/activity.type"
import { Group } from '@/types/group.type'
import {
  AddIcon,
  SmallCloseIcon,
} from '@chakra-ui/icons'
import {
  Box,
  Button, ButtonGroup, Drawer,
  DrawerBody, DrawerCloseButton, DrawerContent, DrawerFooter,
  DrawerHeader,
  DrawerOverlay, FormControl, FormLabel, IconButton, Input, Radio, RadioGroup, Stack, Tag, Text, useDisclosure
} from '@chakra-ui/react'
import { useEffect, useState } from 'react'

const Activity = ({ keyword, group }: { keyword: number, group: Group }) => {
  const { isOpen, onOpen, onClose } = useDisclosure()
  const [activities, setActivities] = useState<TypeActivity[]>([])
  const { token, user } = useAuth()
  const [type, setType] = useState<string>("tarea")
  const [name, setName] = useState<string>("")
  const [closedAt, setClosedAt] = useState<string>("")

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    createActivity({
      name,
      group_id: keyword,
      type,
      closed_at: closedAt,
    }, token)
      .then(resp => {
        setActivities([...activities, resp.data])
      })
      .catch(error => {
        console.log(error)
      })
    setName("")
    setClosedAt("")
  }

  const handleDelete = (id: number) => {
    deleteActivity(id, token)
      .then(_ => {
        setActivities(prev => prev.filter(e => e.id !== id))
      })
      .catch(error => {
        console.log(error)
      })
  }

  useEffect(() => {
    findAllActivities(keyword, token)
      .then(resp => {
        setActivities(resp.data)
      })
  }, [keyword])


  return (
    <>
      <Text as='b'>Activities</Text>
      {
        (group.user_id === user.id) &&
        <IconButton
          size='sm'
          variant='ghot'
          ml='2'
          onClick={() => onOpen()}
          color='grey'
          aria-label="edit-group"
          icon={<AddIcon />}
        />
      }
      <Stack gap='4' mt='2' >
        {!(activities.length === 0) ?
          activities.map((e, i) =>
            <Stack key={i} direction='row' align='center'>
              <Text key={i}>{e.name} </Text>
              <Tag>{new Date(e.closed_at as string).toLocaleDateString()}</Tag>
              <Tag>{e.type}</Tag>
              {
                (group.user_id === user.id) &&
                <ButtonGroup isAttached variant='outline' size='sm' >
                  <IconButton
                    onClick={() => handleDelete(e.id as number)}
                    aria-label='delete-activity'
                    icon={<SmallCloseIcon />} />
                </ButtonGroup>
              }
            </Stack>
          )
          : "No hay actividades"
        }
      </Stack>
      <Drawer
        isOpen={isOpen}
        placement='right'
        onClose={onClose}
      >
        <DrawerOverlay />
        <form onSubmit={handleSubmit}>
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader>Crear Nueva Actividad</DrawerHeader>

            <DrawerBody>
              <FormControl>
                <FormLabel>Nombre: </FormLabel>
                <Input
                  value={name}
                  required
                  onChange={(e) => setName(e.target.value)}
                />
              </FormControl>
              <FormControl mt='6'>
                <FormLabel>
                  <FormLabel>Tipo: </FormLabel>
                  <RadioGroup defaultValue={type} onChange={setType}>
                    <Stack direction='row'>
                      <Radio value='tarea'>Tarea</Radio>
                      <Radio value='evento'>Evento</Radio>
                    </Stack>
                  </RadioGroup>
                </FormLabel>
              </FormControl>
              <FormControl mt='6'>
                <FormLabel>Fecha de cierre: </FormLabel>
                <Input type='date'
                  value={closedAt}
                  required
                  onChange={(e) => setClosedAt(e.target.value)}
                />
              </FormControl>
            </DrawerBody>

            <DrawerFooter>
              <Button variant='outline' mr={3} onClick={onClose}>
                Cancel
              </Button>
              <Button type='submit' colorScheme='blue'>Save</Button>
            </DrawerFooter>
          </DrawerContent>
        </form>
      </Drawer>
    </>
  )
}

export default Activity