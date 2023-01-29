import React from 'react';
import Home from "./components/main/Home";
import Login from "./components/sign/Login";
import Signup from "./components/sign/Signup";
import Age from "./components/details/Age";
import Anniversary from "./components/details/Anniversary";
import Category from "./components/details/Category";
import Price from "./components/details/Price";
import Relationship from './components/details/Relationship';
import { Route, Routes } from 'react-router-dom';
import './components/style/App.css';

const App = () => (
  <Routes>
    <Route path="/" element={<Home />} />
    <Route path="/Login" element={<Login />} />
    <Route path="/Signup" element={<Signup />} />
    <Route path="/Age" element={<Age />} />
    <Route path="/Anniversary" element={<Anniversary />} />
    <Route path="/Category" element={<Category />} />
    <Route path="/Price" element={<Price />} />
    <Route path="/Relationship" element={<Relationship />} />
  </Routes>
);

export default App;
