import React from 'react';
import './Table.css';
import '../common.css';
import {Route, NavLink, HashRouter, BrowserRouter, Link, withRouter} from "react-router-dom"

Object.valueFromPath = (path, obj) => path.split(".").reduce((previousValue, currentValue) => previousValue[currentValue], obj)

class Table extends React.Component {
    constructor(props)
    {
        super(props);
    }
    
    componentDidMount() {
    }
    componentWillUnmount() {
    }

    render () {
        return (
            <table>
                <thead className={"header"}>
                    <tr>
                        {Object.keys(this.props.columns).map((column, i) => {
                            return (
                                <th key={i} className={"header-cell header-cell-alignLeft " + (i == 0 ? "primary-cell" : "")}>
                                    <span>
                                        {column}
                                        <ArrowIcon/>
                                    </span>
                                </th>
                            )
                        })}
                    </tr>
                </thead>
                <tbody>

                    {this.props.data.map((data, i) => {
                        return (
                                <tr key={i} className={"body-row"} >
                                    {
                                        Object.values(this.props.columns).map((column, j) => {
                                            return (
                                                <td key={j} className={j == 0 ? "primary-cell" : ""}>
                                                    <Link to={`${this.props.match.url}/${data[this.props.idField]}`} >
                                                        <div>{Object.valueFromPath(column, data)}</div>
                                                    </Link>
                                                </td>
                                            )
                                        })
                                    }
                                </tr>
                            )
                        })
                    }

                </tbody>
            </table>
        )
    }
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

export const RoutedTable = withRouter(Table)


