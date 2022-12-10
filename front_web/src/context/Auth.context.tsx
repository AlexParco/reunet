import { loginService, registerService } from "@/services/auth.service";
import { State, StateDefault } from "@/types/state.type";
import { User, UserDefault } from "@/types/user.type";
import { createContext, ReactNode, useContext, useState } from "react";

interface IAuthContext {
  login: ({ email, password }: User) => void;
  register: ({ email, password }: User) => void;
  logout: () => void;
  isLogged: boolean;
  token: string;
  state: State;
  user: User;
}

const AuthContext = createContext<IAuthContext>({
  login: () => null,
  logout: () => null,
  register: () => null,
  isLogged: false,
  token: "",
  state: StateDefault,
  user: UserDefault
})

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [state, setState] = useState<State>(StateDefault)
  const [token, setToken] = useState<string>(() => JSON.parse(localStorage.getItem("token") as string))

  const [user, setUser] = useState<User>(() => {
    const storedValue = localStorage.getItem("user")
    return storedValue ? JSON.parse(storedValue) : UserDefault
  })

  const login = (user: User) => {
    setState({ error: false, loading: true, succes: false })

    loginService(user)
      .then(resp => {
        window.localStorage.setItem("user", JSON.stringify(resp.data.user))
        window.localStorage.setItem("token", JSON.stringify(resp.data.token))
        setUser(resp.data.user)
        setToken(resp.data.token)
        console.log(resp.data)
        setState({ error: false, loading: false, succes: true })
      })
      .catch(error => {
        console.log(error)

        setState({ error: true, loading: false, succes: false })
      })
  }

  const register = (user: User) => {
    registerService(user)
      .catch(error => {
        console.log(error)
      })

  }

  const logout = () => {
    window.localStorage.removeItem("user")
    window.localStorage.removeItem("token")
    setToken("")
    setUser(UserDefault)
  }

  return (
    <AuthContext.Provider value={{ login, register, logout, state, user, isLogged: Boolean(token), token }}>
      {children}
    </AuthContext.Provider>
  )

}


export const useAuth = () => {
  const {
    login, register, logout, state, user, isLogged, token
  } = useContext(AuthContext)

  return {
    login, register, logout, state, user, isLogged, token
  }
}


