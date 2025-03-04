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
import Home from "./pages/Home";
import Header from "./layout/header/Header";
import Navigation from "./layout/Navigation";
import Footer from "./layout/Footer";
import Content from "./layout/Content";

function App() {
  return (
    <>
      <Header />
      <Content>
        <Routes>
          <Route index element={<Home />} />
          <Route path="/management-frontend" element={<Home />} />
          <Route path="/books" element={<Books />} />
          <Route path="/publishers" element={<Publishers />} />
          <Route path="/authors" element={<Authors />} />
          <Route path="/users" element={<Users />} />
          <Route path="/roles" element={<Roles />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Content>
      <Footer />
    </>
  );
}

export default App;
