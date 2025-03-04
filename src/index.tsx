import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router';
import { RolesProvider } from './context/RolesContext';
import { NotificationProvider } from './context/NotificationContext';
import './index.css';
import "./components/components.css"

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const urlPostFix = process.env.REACT_APP_URL_POSTFIX

root.render(
  <React.StrictMode>
    <BrowserRouter basename={`/${urlPostFix}`}>
      <NotificationProvider content={
          <RolesProvider content={
            <App />
          }/>
      }/>
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
