import React from 'react';
import './common.css';
import firebase from './Firebase/Firestore.js'
import Fixture from "./Firebase/Fixture";
import Table from "./Shared/Table"


export default class FixtureList extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            fixtures :[],
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
        var fixtures = []
        return this.db
            .collection("Fixtures")
            .get().then(
                query => {
                    query.forEach(doc => {
                        Fixture.getFixture(doc).then(fixture => {
                            fixtures.push(test)
                            this.setState({fixtures:fixtures})
                        })
                    })
                }
            )
    }

    render () {
        return <Table baseUrl={"/fixtures"} columns={{"TestFixture":"testFixtureName", "Description":"description"}} data={this.state.fixtures}></Table>
    }
}



