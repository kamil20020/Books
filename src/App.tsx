import React from "react";
import logo from "./logo.svg";
import "./App.css";
import { Route, Routes } from "react-router";
import NotFound from "./error/NotFound";
import Users from "./pages/Users";
import Books from "./pages/Books";
import Publishers from "./pages/Publishers";
import Roles from "./pages/Roles";
import Authors from "./pages/Authors";
import Header from "./layout/header/Header";
import Navigation from "./layout/Navigation";
import Footer from "./layout/Footer";
import Content from "./layout/Content";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <>
      <Header />
      <Content>
        <Routes>
          <Route index element={<Books />} />
          <Route path="/management-frontend" element={<Books />} />
          <Route path="/publishers" element={<Publishers />} />
          <Route path="/authors" element={<Authors />} />
          <Route path="/users" element={
            <ProtectedRoute 
              isRequiredAdmin 
              content={
                <Users />
            }/>
          } />
          <Route path="/roles" element={
              <ProtectedRoute 
                isRequiredAdmin 
                content={
                  <Roles />
              }/>
          } />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Content>
      <Footer />
    </>
  );
}

export default App;
