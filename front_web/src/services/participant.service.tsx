import { Participant } from "@/types/participant.type"
import { Response } from "@/types/response.type"

const API_URL = import.meta.env.VITE_APIURL

export const findAllPrtByGrpId = (groupId: number, token: string): Promise<Response<Participant[]>> => {
  return fetch(API_URL + `participants?groupid=${groupId}`, {
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

export const addParticipanat = (participant: Participant, token: string): Promise<Response<Participant>> => {
  return fetch(API_URL + `participants`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(participant)
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}

export const deleteParticipant = (participantId: number, token: string) => {
  return fetch(API_URL + `participants/${participantId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
    })

}

