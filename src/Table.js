import React from 'react';
import './TestRuns.css';
import './common.css';
import firebase from './Firestore.js'
import {Test} from "./Firestore";

export default class Table extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {
            columns:props.columns,
            data:props.data,
            sort: ["", undefined]
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
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
        this.setState((state, props) =>({sort:[columnName,state.sort[0] === columnName ? nextDirection(state.sort[1]) : "descending"]}))
    }

    sortDirection = (columnName) =>
    {
        return this.state.sort[0] === columnName ? this.state.sort[1] : undefined
    }

    render () {
        return (
            <div class={"table-root"}>
                <table class={"test-run-table"}>
                    <thead class={"header"}>
                    <tr>
                        {this.state.columns.map(column => <TestHeaderCell columnName={column} sortDirection={this.sortDirection} onClick={this.sortBy} primary={this.state.columns.indexOf(column) == 0}/>)}
                    </tr>
                    </thead>
                    <tbody>

                    {this.state.tests.map(test => {
                           return  (<TestRow key={this.state.tests.indexOf(test)} test={test}/>)
                        })
                    }
                    </tbody>
                </table>
            </div>
        )
    }
}

function TestHeaderCell(props)
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
function  TestRow(props) {
    return (
        <tr className={"test-body-row"} onClick={props.onClick}>
            <td className={"primary-cell"}>{props.test.testFixtureName}</td>
            <td>
                <a href={props.test.codeLink}>{props.test.testName}</a>
            </td>
            <td>{props.test.targetBranchName}</td>
            <td>{props.test.start}</td>
            <td>{props.test.end}</td>
            <td>{props.test.duration}</td>
            <td>{props.test.status}</td>
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
            <path d="M20 12l-1.41-1.41L13 16.17V4h-2v12.17l-5.58-5.59L4 12l8 8 8-8z"/>
        </svg>
    )
}



