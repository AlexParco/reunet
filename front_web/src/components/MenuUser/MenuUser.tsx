import { useAuth } from "@/context/Auth.context"
import {
  Menu, Image, MenuButton, MenuItem, MenuList, Button,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  Text,
  ModalCloseButton,
  useDisclosure,
} from "@chakra-ui/react"
import { useNavigate } from "react-router-dom"

const MenuUser = () => {
  const { logout, user } = useAuth()
  const navegate = useNavigate()
  const word: string[] = ["lol", "csm", "hdp", "crj", "hdpt", "hate you", "wtf"]
  const { isOpen, onOpen, onClose } = useDisclosure()

  const handleLogout = () => {
    logout()
    navegate("/login")
  }

  return (
    <>
      <Menu >
        <MenuButton m='0' p='0' >
          <Image
            border='2px'
            borderColor='#f0f0f0'
            shadow={'md'}
            boxSize={'60px'}
            rounded='50%'
            alt='user'
            src="https://bit.ly/dan-abramov"
          />
          {user.firstname}
        </MenuButton>
        <MenuList>
          <MenuItem>Profile</MenuItem>
          <MenuItem onClick={() => handleLogout()}>Logout</MenuItem>
          <MenuItem onClick={() => onOpen()}>Mensaje Prohibidos</MenuItem>
        </MenuList>
      </Menu>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Menesajes Prohibidos</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            {word.map((e, i) =>
              <Text key={i}>{e}</Text>
            )}
          </ModalBody>
          <ModalFooter>
            <Button colorScheme='blue' mr={3} onClick={onClose}>
              Close
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  )
}

export default MenuUser