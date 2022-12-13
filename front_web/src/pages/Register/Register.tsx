import { useAuth } from "@/context/Auth.context"
import { Flex, FormControl, Link, Heading, Box, Input, FormLabel, Button } from "@chakra-ui/react"
import { useEffect, useState } from 'react'
import { Link as ReachLink, useNavigate } from 'react-router-dom'

const Register = () => {
  const [firstname, setFirstname] = useState<string>("")
  const [lastname, setLastName] = useState<string>("")
  const [role, setRole] = useState<string>("")
  const [email, setEmail] = useState<string>("")
  const [password, setPassword] = useState<string>("")
  const { isLogged, register } = useAuth()
  const navegate = useNavigate()

  useEffect(() => {
    console.log(isLogged)
    if (isLogged) {
      navegate("/")
    }

  }, [isLogged])


  const handleSubmit = () => {
    register({
      lastname,
      firstname,
      email,
      password,
      role: "ROLE_USER",
      avatar: ""
    })
    navegate("/login")
  }


  return (
    <Flex
      align='center'
      justify='center'
      h='100%'
      mt='20'
    >
      <form onSubmit={handleSubmit}>
        <Heading>Register</Heading>
        <FormControl mb={10}>
          <Box mt={10}>
            <FormLabel>Firstname</FormLabel>
            <Input
              type='text'
              value={firstname}
              required
              onChange={(e) => setFirstname(e.target.value)}
            />
          </Box>
        </FormControl>
        <FormControl mb={10}>
          <Box mt={10}>
            <FormLabel>Lastname</FormLabel>
            <Input
              type='text'
              value={lastname}
              required
              onChange={(e) => setLastName(e.target.value)}
            />
          </Box>
        </FormControl>
        <FormControl mb={10}>
          <Box mt={10}>
            <FormLabel>Email</FormLabel>
            <Input
              type='text'
              value={email}
              required
              onChange={(e) => setEmail(e.target.value)}
            />
          </Box>
        </FormControl>
        <FormControl>
          <Box my={10}>
            <FormLabel mt='8'>Password</FormLabel>
            <Input
              type='password'
              value={password}
              required
              onChange={(e) => setPassword(e.target.value)}
            />
          </Box>
        </FormControl>
        <Button type="submit" >Enviar</Button>
        <Box w='77%' textAlign='right' color='blue'>
          <Link as={ReachLink} to="/register" position='absolute'>
            register
          </Link>
        </Box>
      </form >
    </Flex >
  )

}

export default Register