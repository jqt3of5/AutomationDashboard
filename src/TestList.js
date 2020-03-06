import React from 'react';
import './common.css';
import firebase from './Firestore.js'
import {Test} from "./Firestore";
import Table from "./Shared/Table"


export default class TestList extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            tests :[],
        }

        this.db = firebase.firestore()
    }

    componentDidMount() {
        this.fetchTests()
    }

    componentWillUnmount() {
    }

    fetchTests()
    {
        var tests = []
        return this.db
            .collection("Tests")
            .get().then(
                query => {
                    query.forEach(doc => {
                        let test = new Test(doc)

                        test.testFixture.then(tf => {
                            test.testFixtureName = tf.get("Name")
                            tests.push(test)

                            this.setState({tests:tests})
                        })
                    })
                }
        )
    }

    render () {
        return <Table baseUrl={"/tests"} columns={{"TestFixture":"testFixtureName", "Test":"name"}} data={this.state.tests}></Table>
    }
}



