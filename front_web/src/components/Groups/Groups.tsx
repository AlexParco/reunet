import { GroupCard } from "@/components"
import { useAuth } from "@/context/Auth.context"
import { useGroup } from "@/hooks/useGroup"
import { Box, Button, Drawer, DrawerBody, DrawerCloseButton, DrawerContent, DrawerFooter, DrawerHeader, DrawerOverlay, Flex, FormLabel, Input, Stack, useDisclosure } from "@chakra-ui/react"
import { useRef, useState } from "react"

const Groups = () => {
  const { groups, addGroup } = useGroup()
  const { user } = useAuth()
  const { isOpen, onOpen, onClose } = useDisclosure()
  const refBtn = useRef<HTMLInputElement>(null)

  const [name, setName] = useState<string>("")
  const [description, setDescription] = useState<string>("")

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    addGroup({ id: 0, user_id: user.id, name, description })
  }

  return (
    <Box >
      <Flex direction='column'>
        {groups.map((group, i) =>
          <GroupCard
            key={i}
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