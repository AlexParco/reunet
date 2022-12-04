import { Group } from "@/types/group.type"
import { Response } from "@/types/response.type"

const API_URL = import.meta.env.VITE_APIURL

export const createGroup = (group: Group, token: string): Promise<Response<Group>> => {
  return fetch(API_URL + "group", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(group)
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}

export const findAllGroups = (token: string): Promise<Response<Group[]>> => {
  console.log(token)
  return fetch(API_URL + "group", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}
