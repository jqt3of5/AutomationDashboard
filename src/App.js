import React from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  return (
      <div class="app">
        <Header title="Automation Dashboard"></Header>
        <Sidebar></Sidebar>
      </div>
   );
}

function Header(props) {
  return (
      <header class="header">
          <div class="header-logo">
              {props.title}
          </div>
      </header>)
}

function Sidebar(){
  return (
      <div class="sidebar">
        <SidebarItem title={"test1"}/>
        <SidebarItem title={"test1"}/>
        <SidebarItem title={"test1"}/>
      </div>)
}

function SidebarItem(props){
    return <div class="sidebar-item"> {props.title}</div>
}
export default App;
