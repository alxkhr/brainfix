import { combineReducers } from 'redux';

import AppState from './app-state';
import { auth } from './auth/auth-reducer';

export default combineReducers<AppState>({ auth });
