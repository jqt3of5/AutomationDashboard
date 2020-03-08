import React from 'react';
import './common.css';
import firebase from './Firebase/Firestore.js'
import Fixture from "./Firebase/Fixture";
import {RoutedTable} from "./Shared/Table";

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
        return <RoutedTable columns={{
                "TestFixture": "testFixtureName",
                "Description": "description"
                }} data={this.state.fixtures}/>
    }
}



