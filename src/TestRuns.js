import React from 'react';
import './TestRuns.css';
import firebase from './Firestore.js'

class TestRuns extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            tests :[]
        }
    }

    componentDidMount() {
        this.fetchTestRuns().then(
            result => {}
        )
    }

    componentWillUnmount() {
    }

    fetchTestRuns()
    {
        var tests = []
        const db = firebase.firestore();
        return db.collection("TestRuns").get().then(
            query => {
                query.forEach(doc => {
                        var test = {}
                        test.automationAgent = doc.get("AutomationAgent")
                        test.targetBranchName = doc.get("TargetBranchName")
                        test.targetBuildNumber = doc.get("TargetBuildNumber")
                        test.targetCommitHash = doc.get("TargetCommitHash")
                        test.codeLink = doc.get("CodeLink")
                        test.start = doc.get("TestStart").toDate().toString()
                        test.end = doc.get("TestEnd").toDate().toString()
                        test.testParams = doc.get("TestParams")
                        test.videoURL = doc.get("VideoURL")
                        test.status = doc.get("TestStatus")

                        doc.get("TestFixture").get().then(
                            tfDoc => {
                                test.testFixture = tfDoc.get("Name")
                                doc.get("Test").get().then(tDoc => {
                                    test.test = tDoc.get("Name")
                                    tests.push(test)
                                    this.setState({tests:tests})
                                })
                            })
                    })
            }
        )
    }

    render () {
        return (
                <div class={"table-root"}>
                    <table class={"test-run-table"}>
                        <thead class={"header"}>
                            <tr>
                                <th class ="root-header-cell primary-cell header-cell-alignLeft">Fixture</th>
                                <th class={"header-cell-alignLeft"}>Test</th>
                                <th className={"header-cell-alignLeft"}>Target Branch</th>
                                <th class={"header-cell-alignLeft"}>Start</th>
                                <th className={"header-cell-alignLeft"}>End</th>
                                <th className={"header-cell-alignLeft"}>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {console.log("testrows: " + this.state.tests.length)}
                            {this.state.tests.map(test => {
                                   return  (<TestRow
                                        key={this.state.tests.indexOf(test)}
                                        fixtureName={test.testFixture}
                                        codeLink={test.codeLink}
                                        testName={test.test}
                                        targetBranch={test.targetBranchName}
                                        testStart={test.start}
                                        testEnd={test.end}
                                        status={test.status}/>)
                                })
                            }
                        </tbody>
                    </table>
                </div>
        )
    }
}

function TestRow(props) {
    return (
        <tr>
            <td className={"primary-cell"}>{props.fixtureName}</td>
            <td>
                <a href={props.codeLink}>{props.testName}</a>
            </td>
            <td>{props.targetBranch}</td>
            <td>{props.testStart}</td>
            <td>{props.testEnd}</td>
            <td>{props.status}</td>
        </tr>
    )
}

export default TestRuns;