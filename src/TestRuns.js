import React from 'react';
import './TestRuns.css';
import firebase from './Firestore.js'

class TestRuns extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            tests :[],
            sort: ["", undefined]
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
                        test.start = doc.get("TestStart").toDate().toLocaleString()
                        test.end = doc.get("TestEnd").toDate().toLocaleString()
                        let milis = (doc.get("TestEnd").toDate() - doc.get("TestStart").toDate())
                        let time = new Date(0,0,0,0,0,0,milis)

                        test.duration = time.getHours() + ":"+time.getMinutes() + ":" + time.getSeconds()
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

    //Experiemental public class field syntax for binding this to thesemethods.
    sortBy = (columnName) =>
    {
        //TODO: toggle logic broken
        function nextDirection(direction)
        {
            switch(direction)
            {
                case "descending":
                    return "ascending"
                case "ascending":
                    return undefined
                default:
                    return "descending"
            }
        }
        this.setState((state, props) =>({sort:[columnName,state.sort[0] == columnName ? nextDirection(state.sort[1]) : "descending"]}))
    }

    sortDirection = (columnName) =>
    {
        return this.state.sort[0] == columnName ? this.state.sort[1] : undefined
    }

    render () {
        return (
                <div class={"table-root"}>
                    <table class={"test-run-table"}>
                        <thead class={"header"}>
                            <tr>
                                <TestHeaderRow columnName={"Fixture"}       sortDirection={this.sortDirection} onClick={this.sortBy} primary={true}/>
                                <TestHeaderRow columnName={"Test"}          sortDirection={this.sortDirection} onClick={this.sortBy}/>
                                <TestHeaderRow columnName={"Target Branch"} sortDirection={this.sortDirection} onClick={this.sortBy}/>
                                <TestHeaderRow columnName={"Start"}         sortDirection={this.sortDirection} onClick={this.sortBy}/>
                                <TestHeaderRow columnName={"End"}           sortDirection={this.sortDirection} onClick={this.sortBy}/>
                                <TestHeaderRow columnName={"Duration"}           sortDirection={this.sortDirection} onClick={this.sortBy}/>
                                <TestHeaderRow columnName={"Status"}        sortDirection={this.sortDirection} onClick={this.sortBy}/>
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
                                        testDuration={test.duration}
                                        status={test.status}/>)
                                })
                            }
                        </tbody>
                    </table>
                </div>
        )
    }
}
function TestHeaderRow(props)
{
    return (
        <th className={"header-cell header-cell-alignLeft " + (props.primary ? "primary-cell" : "")} onClick={() => props.onClick(props.columnName)}>
            <span>
                {props.columnName}
                <ArrowIcon direction={props.sortDirection(props.columnName)}/>
            </span>
        </th>
    )
}
function TestRow(props) {
    return (
        <tr className={"test-body-row"} onClick={props.onClick}>
            <td className={"primary-cell"}>{props.fixtureName}</td>
            <td>
                <a href={props.codeLink}>{props.testName}</a>
            </td>
            <td>{props.targetBranch}</td>
            <td>{props.testStart}</td>
            <td>{props.testEnd}</td>
            <td>{props.testDuration}</td>
            <td>{props.status}</td>
        </tr>
    )
}

function ArrowIcon(props)
{
    function getDirectionClass()
    {
        switch(props.direction)
        {
            case "descending":
                return "arrow-icon-descending"
            case "ascending":
                return "arrow-icon-ascending"
            default:
                return ""
        }
    }

    return (
        <svg className={"arrow-icon " + getDirectionClass()} focusable="false"
             viewBox="0 0 24 24" aria-hidden="true" role="presentation">
            <path d="M20 12l-1.41-1.41L13 16.17V4h-2v12.17l-5.58-5.59L4 12l8 8 8-8z"></path>
        </svg>
    )
}

export default TestRuns;