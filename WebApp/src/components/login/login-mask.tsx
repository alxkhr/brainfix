import * as React from 'react';
import { connect } from 'react-redux';
import { Dispatch } from 'redux';

import { login } from '../../state/actions';

interface LoginMaskPropTypes {
  doLogin: (username: string, password: string) => void;
}

interface LoginMaskState {
  username: string;
  password: string;
}

class LoginMask extends React.PureComponent<LoginMaskPropTypes, LoginMaskState> {
  constructor(props: LoginMaskPropTypes) {
    super(props);
    this.state = {
      username: '',
      password: '',
    };
    this.onSubmit = this.onSubmit.bind(this);
    this.onChangeUsername = this.onChangeUsername.bind(this);
    this.onChangePassword = this.onChangePassword.bind(this);
  }

  public render(): JSX.Element {
    return (
      <form>
        <label>Username</label>
        <input onChange={this.onChangeUsername} value={this.state.username} />
        <br />
        <label>Password</label>
        <input type="password" onChange={this.onChangePassword} value={this.state.password} />
        <br />
        <button onClick={this.onSubmit}>submit</button>
      </form>
    );
  }

  public onChangeUsername(event: React.ChangeEvent<HTMLInputElement>): void {
    this.setState({ username: event.target.value });
  }

  public onChangePassword(event: React.ChangeEvent<HTMLInputElement>): void {
    this.setState({ password: event.target.value });
  }

  public onSubmit(event: React.MouseEvent<HTMLButtonElement>): void {
    event.preventDefault();
    this.props.doLogin(this.state.username, this.state.password);
  }
}

export default connect(
  null,
  (dispatch: Dispatch) => ({
    doLogin: (username: string, password: string) => {
      dispatch(login(username, password));
    },
  }),
)(LoginMask);
