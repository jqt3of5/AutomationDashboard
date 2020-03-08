import React from 'react';
import './common.css';
import firebase from './Firebase/Firestore.js'
import Test from "./Firebase/Test";
import {RoutedTable} from "./Shared/Table"


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
        this.db
            .collection("Tests")
            .get().then(
                query => {
                    query.forEach(doc => {
                        Test.getTest(doc).then(test => {
                            tests.push(test)
                            this.setState({tests:tests})
                        })
                    })
                }
        )
    }

    render () {
        return <RoutedTable columns={{"TestFixture":"fixture.name", "Test":"name"}} idField = {"testId"} data={this.state.tests}></RoutedTable>
    }
}



