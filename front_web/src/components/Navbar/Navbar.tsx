import { useAuth } from "@/context/Auth.context"
import { Box, Button, Flex, Heading, HStack, Link, Stack } from "@chakra-ui/react"
import { Link as ReachLink, useNavigate } from 'react-router-dom'
import { MenuUser } from "../MenuUser"

const Navbar = () => {
  const { isLogged, logout } = useAuth()
  const navegate = useNavigate()

  const handleLogout = () => {
    logout()
    navegate("/login")
  }

  return (
    <Flex
      p={3}
      w="100%"
      margin='0'
      align='center'
      justify='center'
    >
      <HStack
        p='3'
        align='center'
        justify='space-between'
        w="100%"
        maxW='960px'
      >
        <Heading size='lg' >
          Reune T
        </Heading>
        {
          isLogged ?
            <MenuUser />
            : <Stack direction='row'>
              <Link as={ReachLink} to='/register' size='sm'>Registrarse</Link>
              <Link as={ReachLink} to='/login' size='sm'>Iniciar Sesíon</Link>
            </Stack>
        }
      </HStack>

    </Flex >
  )
}

export default Navbar