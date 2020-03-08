import React from 'react';
import logo from './logo.svg';
import './App.css';
import './common.css';
import {Route, NavLink, HashRouter, BrowserRouter, Switch} from "react-router-dom"
import TestRuns from "./TestRuns";
import TestRunDetails from "./TestRunDetails";
import TestList from "./TestList";
import FixtureList from "./FixtureList";
import {TestDetails} from "./TestDetails";

class App extends React.Component
{
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="app">
                <BrowserRouter>
                    <div className="sidebar">
                        <div className={"sidebar-header"}>Automation</div>
                        <NavLink to={"/tests"}><SidebarItem title={"Tests"} selected={"true"}/></NavLink>
                        <NavLink to={"/testruns"}><SidebarItem title={"Test Runs"}/></NavLink>
                        <NavLink to={"/preferences"}><SidebarItem title={"Preferences"}/></NavLink>
                    </div>
                    <div className={"content"}>
                        <Header title={"Test Runs"}/>
                        <div className={"page"}>

                            <Switch>
                                <Route exact path={"/fixtures"} component={FixtureList}/>
                                <Route exact path={"/tests"} component={TestList}/>
                                <Route exact path={"/tests/:testid"} component={TestDetails}/>

                                <Route exact path={"/testruns"} component={TestRuns}/>
                                <Route exact path={"/testruns/:testrunid"} component={TestRunDetails}/>

                                <Route exact path={"/preferences"}/>
                            </Switch>

                        </div>
                    </div>
                </BrowserRouter>

            </div>
        );
    }
}

function SidebarItem(props){
    return (
        <div className={"sidebar-item"}>
            <div className={"selected-ribbon"}></div>
            <PrefIcon/>
            <p>{props.title}</p>
        </div>)
}

function PrefIcon() {
    return (<svg className={"sidebar-icon"} focusable="false" viewBox="0 0 24 24" aria-hidden="true" role="presentation">
        <path transform="scale(1.2, 1.2)" fill="none" d="M0 0h20v20H0V0z"></path>
        <path transform="scale(1.2, 1.2)"
              d="M15.95 10.78c.03-.25.05-.51.05-.78s-.02-.53-.06-.78l1.69-1.32c.15-.12.19-.34.1-.51l-1.6-2.77c-.1-.18-.31-.24-.49-.18l-1.99.8c-.42-.32-.86-.58-1.35-.78L12 2.34c-.03-.2-.2-.34-.4-.34H8.4c-.2 0-.36.14-.39.34l-.3 2.12c-.49.2-.94.47-1.35.78l-1.99-.8c-.18-.07-.39 0-.49.18l-1.6 2.77c-.1.18-.06.39.1.51l1.69 1.32c-.04.25-.07.52-.07.78s.02.53.06.78L2.37 12.1c-.15.12-.19.34-.1.51l1.6 2.77c.1.18.31.24.49.18l1.99-.8c.42.32.86.58 1.35.78l.3 2.12c.04.2.2.34.4.34h3.2c.2 0 .37-.14.39-.34l.3-2.12c.49-.2.94-.47 1.35-.78l1.99.8c.18.07.39 0 .49-.18l1.6-2.77c.1-.18.06-.39-.1-.51l-1.67-1.32zM10 13c-1.65 0-3-1.35-3-3s1.35-3 3-3 3 1.35 3 3-1.35 3-3 3z"></path>
    </svg>)
}

function Header (props){
    return (
        <div>
            <div className={"content-header"}>
                <p className={"content-header-inner"}>{props.title}</p>
            </div>
        </div>

    )
}

export default App;
