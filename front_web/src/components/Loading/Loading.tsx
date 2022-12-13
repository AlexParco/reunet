import { Flex, Heading } from "@chakra-ui/react"

const Loading = () => {
  return (
    <Flex
      w="100%"
      margin='0'
      align='center'
      justify='center'
      minH='100vh'
    >
      <Heading size="4xl">cargando...</Heading>
    </Flex>
  )
}

export default Loading