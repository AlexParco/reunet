import { Groups, Messages, Participants } from "@/components"
import { useAuth } from "@/context/Auth.context"
import { useGroup } from "@/context/Group.context"
import { useParticipant } from "@/context/Participant.context"
import { createMessage, findAllMessages } from "@/services/message.service"
import { findAllPrtByGrpId } from "@/services/participant.service"
import { findUserById } from "@/services/user.service"
import { Message } from "@/types/message.type"
import { User } from "@/types/user.type"
import { Box, Button, Flex, FormControl, Heading, IconButton, Input, Text } from "@chakra-ui/react"
import React, { useEffect, useState } from "react"
import { TbSend } from "react-icons/tb"

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
        border='1px'
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