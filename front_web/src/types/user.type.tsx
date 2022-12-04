export interface User {
  id?: number;
  firstname?: string;
  lastname?: string;
  email: string;
  password: string;
  role?: string;
  avatar?: string;
  created_at?: string;
}

export const UserDefault: User = {
  id: 0,
  firstname: "",
  lastname: "",
  email: "",
  password: "",
  role: "",
  avatar: "",
  created_at: "",
}

export interface UserWithToken {
  user: User
  token: string
} 
