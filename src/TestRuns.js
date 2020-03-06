import React from 'react';
import './TestRuns.css';
import './common.css';
import firebase from './Firebase/Firestore.js'
import TestRun from "./Firebase/TestRun";
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
        var testRuns = []
        const db = firebase.firestore();

        var collection = db.collection("TestRuns")

        if (this.props.testid !== undefined)
        {
            const test = db.collection("Tests").doc(this.props.testid)
            collection = collection.where("Test", "==", test)
        }

        return collection.get().then(
                query => {
                    query.forEach(doc => {
                            console.log(doc)
                            TestRun.getTestRun(doc).then(testRun => {
                                testRuns.push(testRun)
                                this.setState({testRuns:testRuns})
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
        return <Table baseUrl={"/testruns"} columns={columns} data={this.state.testRuns}/>
    }
}

export default TestRuns;