import * as React from 'react';
import { connect } from 'react-redux';

import AppState from '../state/app-state';
import LoginState from '../state/auth/model/login-state';
import LoginMask from './login/login-mask';

interface AppPropTypes {
  loginState: LoginState;
}

function App(props: AppPropTypes): JSX.Element {
  switch (props.loginState) {
    case LoginState.LOGGED_IN:
      return <div>Fix Brain Fix Brain Fix</div>;
    case LoginState.LOGGING_IN:
      return <div>logging in animation</div>;
    default:
      return <LoginMask />;
  }
}

export default connect((state: AppState) => ({ loginState: state.auth.loginState }))(App);
