import { Groups, Messages, Participants } from "@/components"
import { useGroup } from "@/context/Group.context"
import { useParticipant } from "@/context/Participant.context"
import { Flex } from "@chakra-ui/react"

const Home = () => {
  const { keyword } = useGroup()
  const { participants } = useParticipant()

  return (
    <Flex
      align='center'
      justify='center'
      h='100%'
      mt='20'
    >
      <Flex
        margin='0'
        wrap='wrap'
      >
        <Groups />
        {keyword !== 0 &&
          <Messages />
        }
        {keyword !== 0 &&
          <Participants participants={participants} />
        }
      </Flex >
    </Flex>
  )
}

export default Home 