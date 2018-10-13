import { applyMiddleware, createStore, Store } from 'redux';
import { createLogger } from 'redux-logger';
import createSagaMiddleware from 'redux-saga';

import AppState from './app-state';
import reducer from './reducers';
import sagas from './sagas';

const saga = createSagaMiddleware();
const logger = createLogger();

export default function configureStore(): Store<AppState> {
  const store = createStore(reducer, applyMiddleware(saga, logger));
  saga.run(sagas);
  return store;
}
