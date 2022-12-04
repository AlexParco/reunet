import { Response } from "@/types/response.type"
import { User, UserWithToken } from "@/types/user.type"

const API_URL = import.meta.env.VITE_APIURL

export function loginService(user: User): Promise<Response<UserWithToken>> {
  return fetch(API_URL + "auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user)
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}

export function registerService(user: User) {
  return fetch(API_URL + "auth/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user)
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
    })
}