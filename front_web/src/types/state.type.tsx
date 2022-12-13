export interface State {
  error: Boolean;
  loading: Boolean;
  succes: Boolean;
}

export const StateDefault: State = {
  error: false,
  loading: false,
  succes: false,
}