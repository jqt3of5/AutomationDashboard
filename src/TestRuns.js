import React from 'react';
import './TestRuns.css';
import './common.css';
import firebase from './Firestore.js'
import {TestRun} from "./Firestore";
import Table from "./Shared/Table"

class TestRuns extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            testRuns :[],
        }
    }

    componentDidMount() {
        this.fetchTestRuns()
    }

    componentWillUnmount() {
    }

    fetchTestRuns()
    {
        var tests = []
        const db = firebase.firestore();
        return db
            .collection("TestRuns")
            .get()
            .then(
                query => {
                    query.forEach(doc => {
                            let test = new TestRun(doc)
                            test.testFixture.then(tf => {
                                test.testFixtureName = tf.get("Name")
                                test.test.then(t => {
                                    test.testName = t.get("Name")
                                    tests.push(test)
                                    this.setState({testRuns:tests})
                                })
                            })
                        })
                }
        )
    }

    render () {
        var columns = {
            "Branch Name":"targetBranchName",
            "Build Number":"targetBuildNumber",
            "Started":"start",
            "Ended":"end",
            "Test Fixture":"testFixtureName",
            "Test Name":"testName"}
        return <Table columns={columns} data={this.state.testRuns}/>
    }
}

export default TestRuns;