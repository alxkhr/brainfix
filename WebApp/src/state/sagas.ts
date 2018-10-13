import { AnyAction } from 'redux';
import { call, CallEffect, ForkEffect, put, PutEffect, takeEvery } from 'redux-saga/effects';

import { LOG_IN, LOGGED_IN } from './actions';

function* requestToken(action: AnyAction): Iterator<CallEffect | PutEffect<AnyAction>> {
  try {
    const response: Response = yield call(() =>
      fetch(`/api/requestToken?username=${action.username}&password=${action.password}`, {
        method: 'POST',
      }),
    );
    response.text().then((token) => localStorage.setItem('authToken', token));
    yield put({ type: LOGGED_IN });
  } catch (e) {
    console.error('ERROR_ON_REQUEST_TOKEN', e);
    // TODO yield put({ type: actions.ERROR_ON_REQUEST_TOKEN, message: e.message });
  }
}

function* root(): Iterator<ForkEffect> {
  yield takeEvery(LOG_IN, requestToken);
  // yield takeEvery(CREATED_NOTE, saveNote);
}

export default root;
