import { User } from "./user.type";

export interface Message {
  message_id?: number;
  user_id?: number;
  group_id?: number;
  body?: string;
  created_at?: string;
  user?: User;
}