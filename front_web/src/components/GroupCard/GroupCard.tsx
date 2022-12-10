import { useAuth } from "@/context/Auth.context"
import { useGroup } from "@/context/Group.context"
import { deleteGruop } from "@/services/group.service"
import { Group } from "@/types/group.type"
import { Flex, Heading, Tag, Text } from "@chakra-ui/react"
import { ModalCard } from "./ModalCard"

const GroupCard = ({ id, name, description, created_at, user_id }: Group) => {
  const { user, token } = useAuth()
  const { setKeyword, setGroups, keyword, groups } = useGroup()
  const date = new Date(created_at as string).toLocaleDateString()

  const handleDelete = () => {
    deleteGruop(token, id)
      .then(_ => {
        const temp = groups.filter(e => e.id == id)
        setGroups(temp)
      })
      .catch(error => {
        console.log(error)
      })
  }


  return (
    <Flex
      px={20}
      py={4}
      transition='ease.1s'
      cursor='pointer'
      boxShadow="sm" m="4"
      borderRadius="12"
      onClick={() => setKeyword(id as number)}
      border='1px'
      borderColor='gray.200'
      align='center'
      direction='column'
      gap='15'
      _hover={{
        borderColor: '#CCCCCC',
      }}
      position='relative'
    >
      {
        user_id === user.id &&
        <>
          <Tag
            top='1'
            left='1'
            size='sm'
            position='absolute'
          >
            Admin
          </Tag>
        </>
      }
      <ModalCard keyword={id} group={{ id, name, description, created_at, user_id }} />
      <Heading size='10px'>{name}
      </Heading>
      <Text>{description || 'no hay descripcion'}</Text>
      <Text fontSize='sm'>Creation Date: {date}</Text>
    </Flex >
  )
}

export default GroupCard
