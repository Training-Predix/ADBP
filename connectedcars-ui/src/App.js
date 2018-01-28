import React, { Component } from 'react';
import { Route, Link, Switch, Redirect } from 'react-router-dom';
import Dashboard1 from './components/Dashboard1';
import axios from 'axios';

class App extends Component {

  render() {
    return (
      <div className='App'>
        <px-branding-bar />

        <Switch>
          <Route exact path='/dashboard1' component={Dashboard1} />
          <Route path='/' render={() => {
            return <Redirect to='/dashboard1' />
          }}/>
        </Switch>
      </div>
    );
  }
}

export default App;
