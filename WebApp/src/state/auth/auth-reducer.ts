import { AnyAction } from 'redux';

import { LOG_IN, LOGGED_IN } from '../actions';
import AuthState from './model/auth-state';
import LoginState from './model/login-state';

function login(state: AuthState): AuthState {
  return { ...state, loginState: LoginState.LOGGING_IN };
}

function loggedIn(state: AuthState): AuthState {
  return { ...state, loginState: LoginState.LOGGED_IN };
}

const defaultAuthState: AuthState = {
  loginState: localStorage.getItem('authToken') ? LoginState.LOGGED_IN : LoginState.LOGGED_OUT,
};

export function auth(state: AuthState = defaultAuthState, action: AnyAction): AuthState {
  switch (action.type) {
    case LOG_IN:
      return login(state);
    case LOGGED_IN:
      return loggedIn(state);
    default:
      return state;
  }
}
