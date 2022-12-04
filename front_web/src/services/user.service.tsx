import { User } from "@/types/user.type"
import { Response } from "@/types/response.type"

const API_URL = import.meta.env.VITE_APIURL

export const findUserById = (userId: number, token: string): Promise<Response<User>> => {
  return fetch(API_URL + `user/${userId}`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    }
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}
export const findAllUsers = (token: string): Promise<Response<User[]>> => {
  return fetch(API_URL + `user`, {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    }
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}