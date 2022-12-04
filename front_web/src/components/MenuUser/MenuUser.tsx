import { useAuth } from "@/context/Auth.context"
import { ChevronDownIcon } from "@chakra-ui/icons"
import { Menu, Image, MenuButton, MenuItem, MenuList, Button } from "@chakra-ui/react"
import { useNavigate } from "react-router-dom"

const MenuUser = () => {
  const { logout } = useAuth()
  const navegate = useNavigate()

  const handleLogout = () => {
    logout()
    navegate("/login")
  }

  return (
    <Menu>
      <MenuButton>
        <Image
          border='2px'
          borderColor='#f0f0f0'
          shadow={'md'}
          boxSize={'60px'}
          rounded='50%'
          alt='user'
          src="https://bit.ly/dan-abramov"
        />
      </MenuButton>
      <MenuList>
        <MenuItem>Profile</MenuItem>
        <MenuItem onClick={() => handleLogout()}>Logout</MenuItem>
      </MenuList>
    </Menu>
  )
}

export default MenuUser