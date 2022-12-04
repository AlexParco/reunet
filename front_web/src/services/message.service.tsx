import { Message } from "@/types/message.type"
import { Response } from "@/types/response.type"

const API_URL = import.meta.env.VITE_APIURL

export const findAllMessages = (groupId: number, token: string): Promise<Response<Message[]>> => {
  return fetch(API_URL + `message?groupid=${groupId}`, {
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

export const createMessage = (message: Message, token: string): Promise<Response<Message>> => {
  return fetch(API_URL + `message`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(message)
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
} 
