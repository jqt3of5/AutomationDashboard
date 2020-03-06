import React from 'react';
import './common.css';
import firebase from './Firebase/Firestore.js'
import TestRuns from "./TestRuns";
import Test from "./Firebase/Test";

export class TestDetails extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            test: {},
        }

        this.db = firebase.firestore()
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    fetchTest() {
        this.db.collection("/Tests")
        .doc(this.props.match.params.testid).get()
        .then(doc => {
            return Test.getTest(doc)
        }).then(test => {
            this.setState({test:test})
        })
    }

    render () {

        return (
            <div>
                <TestRuns testid={this.props.match.params.testid}/>
            </div>)
        //<Table baseUrl={"/testruns"} columns={columns} data={this.state.testRuns}/>
    }
}
