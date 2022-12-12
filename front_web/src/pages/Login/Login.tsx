import { Loading } from "@/components"
import { useAuth } from "@/context/Auth.context"
import {
  Box, Button, Flex, FormControl,
  FormLabel,
  Heading,
  Input, Link,
  useToast
} from "@chakra-ui/react"
import React, { useEffect, useState } from "react"
import { Link as ReachLink, useNavigate } from 'react-router-dom'

const Login = () => {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const { login, isLogged, state } = useAuth()
  const navegate = useNavigate()
  const toast = useToast()

  useEffect(() => {
    if (isLogged) {
      navegate("/")
    }

    if (state.error) {
      toast({
        title: 'Error',
        description: 'incorrect credentials',
        status: 'error',
        duration: 3000,
        isClosable: true,
      })
    }

  }, [isLogged, state])

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    login({ email, password })
    setEmail("")
    setPassword("")
  }

  return (
    <>
      {state.loading && <Loading />}
      {
        !state.loading &&
        <Flex
          align='center'
          justify='center'
          h='100%'
          mt='20'
        >
          <form onSubmit={handleSubmit}>
            <Heading>Login</Heading>
            <FormControl mb={10}>
              <Box mt={10}>
                <FormLabel>Email</FormLabel>
                <Input
                  type='text'
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </Box>
            </FormControl>
            <FormControl>
              <Box my={10}>
                <FormLabel mt='8'>Password</FormLabel>
                <Input
                  type='password'
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
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
      }
    </>
  )
}

export default Login