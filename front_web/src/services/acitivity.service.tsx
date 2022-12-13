import { Activity } from "@/types/activity.type"
import { Response } from "@/types/response.type"

const API_URL = import.meta.env.VITE_APIURL

export const findAllActivities = (groupId: number, token: string): Promise<Response<Activity[]>> => {
  return fetch(API_URL + `activity?groupid=${groupId}`, {
    method: "GET",
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

export const createActivity = (activity: Activity, token: string): Promise<Response<Activity>> => {
  return fetch(API_URL + `activity`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(activity)
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}

export const deleteActivity = (id: number, token: string) => {
  return fetch(API_URL + `activity/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    }
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
    })
}

export const editActivity = (activity: Activity, token: string): Promise<Response<Activity>> => {
  return fetch(API_URL + `activity`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(activity)
  })
    .then(resp => {
      if (!resp.ok) {
        throw new Error(resp.statusText)
      }
      return resp.json()
    })
}