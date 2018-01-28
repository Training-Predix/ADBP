import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css';
import { HashRouter } from 'react-router-dom';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

ReactDOM.render(
  <HashRouter>
    <MuiThemeProvider>
      <App />
    </MuiThemeProvider>
  </HashRouter>, 
  document.getElementById('root')
);
