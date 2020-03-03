import React from 'react';
import './common.css';
import firebase from './Firestore.js'
import {Test} from "./Firestore";
import Table from "./Table"

export default class TestList extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            tests :[],
        }
    }

    componentDidMount() {
        this.fetchTestRuns().then(
            result => {}
        )
    }

    componentWillUnmount() {
    }

    fetchTests()
    {
        var tests = []
        const db = firebase.firestore();
        return db.collection("TestsFixtures").get().then(
            query => {
                query.forEach(doc => {
                    let test = new Test(doc)
                    test.testFixture.then(tf => {
                        test.testFixtureName = tf.get("Name")
                        test.test.then(t => {
                            test.testName = t.get("Name")
                            tests.push(test)
                            this.setState({tests:tests})
                        })
                    })
                })
            }
        )
    }

    render () {
        return <Table columns={[{"TestFixture":"testFixture"}, {"Test":"test"}]} data={this.state.tests}></Table>
    }
}



