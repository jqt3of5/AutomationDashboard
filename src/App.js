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
import SettingsIcon from '@material-ui/icons/Settings';
import DoubleArrowIcon from '@material-ui/icons/DoubleArrow';
import AssignmentIcon from '@material-ui/icons/Assignment';
import MenuIcon from '@material-ui/icons/Menu';


class App extends React.Component
{
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div className="app">
                <BrowserRouter>
                    <div className="sidebar expanded">
                        <div className={"sidebar-header"}>
                            <XIcon className={"header-icon"}/>
                            <p>Automation</p>
                            <MenuIcon className={"menu-icon"}/>
                        </div>
                        <NavLink to={"/tests"}>
                            <div className={"sidebar-item"}>
                                <div className={"selected-ribbon"}></div>
                                <AssignmentIcon className={"sidebar-icon"}/>
                                <p>{"Tests"}</p>
                            </div>
                        </NavLink>

                        <NavLink to={"/testruns"}>
                            <div className={"sidebar-item"}>
                                <div className={"selected-ribbon"}></div>
                                <DoubleArrowIcon className={"sidebar-icon"}/>
                                <p>{"Test Runs"}</p>
                            </div>
                        </NavLink>
                        <NavLink to={"/preferences"}>
                            <div className={"sidebar-item"}>
                                <div className={"selected-ribbon"}></div>
                                <SettingsIcon className={"sidebar-icon"}/>
                                <p>{"Preferences"}</p>
                            </div>
                        </NavLink>

                    </div>
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
                </BrowserRouter>

            </div>
        );
    }
}

function XIcon(props) {
    return (<svg className={props.className} width="31px" height="26px" viewBox="0 0 31 26" version="1.1">
        <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
            <g id="Dashboard-Navigation-Behavior" transform="translate(-712.000000, -92.000000)" fill="#FFFFFF">
                <g id="Nav/Dashboard/Projects-COLLAPSED" transform="translate(703.000000, 64.000000)">
                    <polygon id="X" points="21.221123 39.9544829 14.9064631 28 23.1145268 28 25.9793468 34.6413794 30.9479487 28 39.1560124 28 29.0545449 39.9544829 36.3829181 53.6800003 28.3451461 53.6800003 24.2909268 45.3016448 17.0718303 53.6800003 9 53.6800003"/>
                </g>
            </g>
        </g>
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
