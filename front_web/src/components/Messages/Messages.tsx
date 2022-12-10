import { useAuth } from "@/context/Auth.context"
import { useGroup } from "@/context/Group.context"
import { createMessage, deleteMessage, findAllMessages } from "@/services/message.service"
import { findUserById } from "@/services/user.service"
import { Message } from "@/types/message.type"
import { DeleteIcon, WarningIcon } from "@chakra-ui/icons"
import { Box, Button, Text, Flex, FormControl, IconButton, Input, Stack, useToast } from "@chakra-ui/react"
import { useEffect, useState } from "react"
import { TbSend } from "react-icons/tb"

const Messages = () => {
  const [body, setBody] = useState<string>("")
  const [messages, setMessages] = useState<Message[]>([])
  const { keyword } = useGroup()
  const { token, user } = useAuth()
  const toast = useToast()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const messages = await findAllMessages(keyword, token)
        await Promise.all(messages.data.map(async (message) => {
          const data = await findUserById(message.user_id as number, token)
          message.user = data.data
        }))
        setMessages(messages.data.reverse())

      } catch (error) {
        console.log(error)
      }
    }
    fetchData()
  }, [keyword])

  const handleDelete = (id: number) => {
    deleteMessage(id, token)
      .then(_ => {
        setMessages((prev) => prev.filter(e => e.message_id !== id))
      })
      .catch(error => console.log(error))
  }

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    const fetchData = async () => {
      try {
        if (body.length === 0) {
          return toast({
            title: 'Error',
            description: 'Mensaje Empty',
            status: 'warning',
            duration: 2000,
            isClosable: true,
          })
        }
        const resp = await createMessage({ group_id: keyword, body: body }, token)
        const user = await findUserById(resp.data.user_id as number, token)
        resp.data.user = user.data

        setMessages([resp.data, ...messages])
      } catch (error) {
        console.log(error)
      }
    }
    fetchData()
    setBody("")
  }

  return (
    <Box minW='500px' m='4' borderLeft='1px' px='10'>
      <form onSubmit={handleSubmit}>
        <Flex
          justify='center'
          align='center'
          gap='2'
        >
          <IconButton
            type="submit"
            aria-label='send mensaje'
            variant='outline'
            icon={<TbSend />}
          />
          <FormControl>
            <Input type="text"
              value={body}
              onChange={(e) => setBody(e.target.value)}
              w='100%' />
          </FormControl>
        </Flex>
      </form>
      <Box mt='6'>
        {messages.map((message, i) =>
          <Flex
            key={i}
            p='2'
            justify='space-between'
          >
            <Text>
              <strong>{message.user?.firstname} </strong>: {message.body}
            </Text>
            {user.id == message.user?.id &&
              <IconButton
                onClick={() => handleDelete(message.message_id as number)}
                size='sm' variant='ghost' aria-label="delete" icon={<DeleteIcon color='red' />} />}
          </Flex>
        )}
      </Box>

    </Box>
  )
}

export default Messages