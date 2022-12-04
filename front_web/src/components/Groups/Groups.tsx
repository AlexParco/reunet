import { GroupCard } from "@/components"
import { useAuth } from "@/context/Auth.context"
import { useGroup } from "@/context/Group.context"
import { useParticipant } from "@/context/Participant.context"
import { createGroup, findAllGroups } from "@/services/group.service"
import { findAllPrtByGrpId } from "@/services/participant.service"
import { Group } from "@/types/group.type"
import { sortGroups } from "@/util/sortArray"
import { Box, Button, Drawer, DrawerBody, DrawerCloseButton, DrawerContent, DrawerFooter, DrawerHeader, DrawerOverlay, Flex, FormLabel, Input, Stack, useDisclosure } from "@chakra-ui/react"
import { useEffect, useRef, useState } from "react"

const Groups = () => {
  const { groups, setGroups } = useGroup()
  const { user, token } = useAuth()
  const { isOpen, onOpen, onClose } = useDisclosure()
  const refBtn = useRef<HTMLInputElement>(null)
  const { addUser } = useParticipant()

  const [name, setName] = useState<string>("")
  const [description, setDescription] = useState<string>("")

  useEffect(() => {
    if (!token) return setGroups([])
    const getAllGroups = async () => {
      try {
        const respgrp = await findAllGroups(token)

        let tempGroups: Group[] = []
        await Promise.all(respgrp.data.map(async (group) => {
          const data = await findAllPrtByGrpId(group.id as number, token)
          const inGroup = data.data.some(e => e.user_id == user.id)
          if (inGroup) {
            tempGroups.push(group)
          }
        }))
        tempGroups = sortGroups(tempGroups)
        setGroups(tempGroups)
      } catch (error) {
        console.log(error)
      }
    }
    getAllGroups()
  }, [setGroups])

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    async function fetchData() {
      try {
        const resp = await createGroup({ id: 0, user_id: user.id, name, description }, token)
        setGroups([...groups, resp.data])
        addUser({ group_id: resp.data.id, user_id: user.id })
      } catch (error) {
        console.log(error)
      }
    }
    fetchData()

  }

  return (
    <Box >
      <Flex direction='column'>
        {groups.map(group =>
          <GroupCard
            key={group.id}
            {...group}
          />
        )}
      </Flex>
      <Button m='4' variant='outline' shadow='sm' onClick={onOpen}>+ Group</Button>
      <Drawer
        isOpen={isOpen}
        placement='right'
        initialFocusRef={refBtn}
        onClose={onClose}
      >
        <DrawerOverlay />
        <form onSubmit={handleSubmit}>
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader borderBottomWidth='1px'>
              Create a new Group
            </DrawerHeader>

            <DrawerBody>
              <Stack spacing='24px'>
                <Box>
                  <FormLabel htmlFor='name'>Group name</FormLabel>
                  <Input
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    id='name'
                    placeholder='Please enter group name'
                  />
                </Box>
                <Box>
                  <FormLabel htmlFor='description'>Group description</FormLabel>
                  <Input
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    type='text'
                    id='description'
                    placeholder='Please enter description of group'
                  />
                </Box>
              </Stack>
            </DrawerBody>

            <DrawerFooter borderTopWidth='1px'>
              <Button variant='outline' mr={3} onClick={onClose}>
                Cancel
              </Button>
              <Button type="submit" colorScheme='blue'>Submit</Button>
            </DrawerFooter>

          </DrawerContent>
        </form>
      </Drawer>
    </Box>
  )
}

export default Groups