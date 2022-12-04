import { Box } from '@chakra-ui/react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { Navbar } from '@/components'

import { RequireAuth } from '@/components/RequireAuth'
import { AuthProvider } from '@/context/Auth.context'
import { GroupProvider } from '@/context/Group.context'
import { ParticipantProvider } from '@/context/Participant.context'
import { Home, Login, Register } from '@/pages'

function App() {

  return (
    <Box
      minH='100vh'
    >
      <BrowserRouter>
        <AuthProvider>
          <GroupProvider>
            <ParticipantProvider>
              <Navbar />

              <Routes>
                <Route path='/' element={
                  <RequireAuth>
                    <Home />
                  </RequireAuth>
                } />
                <Route path='/login' element={<Login />} />
                <Route path='/register' element={<Register />} />
              </Routes>
            </ParticipantProvider>
          </GroupProvider>
        </AuthProvider>
      </BrowserRouter>
    </Box>
  )
}

export default App
