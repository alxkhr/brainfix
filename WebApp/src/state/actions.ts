import { AnyAction } from 'redux';

export const LOG_IN = 'LOG_IN';
export const LOGGED_IN = 'LOGGED_IN';

export function login(username: string, password: string): AnyAction {
  return { type: LOG_IN, username, password };
}

export function loggedIn(): AnyAction {
  return { type: LOGGED_IN };
}
