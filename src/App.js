import React from 'react';
import logo from './logo.svg';
import './App.css';
import './common.css';
import Sidebar from './Sidebar';
import TestRuns from "./TestRuns";
import TestSteps from "./TestSteps";
import TestList from "./TestList";

class App extends React.Component
{
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div class="app">
                <Sidebar></Sidebar>
                <div className={"content"}>
                    <Header title={"Test Runs"}></Header>
                    <TestList></TestList>
                    {/*<TestSteps
                        fixtureName={"Fixture"}
                        testName={"Test"}
                        runId={"Y8Cssyp7AVaX0dwxrRrK"}
                    ></TestSteps>*/}

                </div>
            </div>
        );
    }
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
