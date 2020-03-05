import React from 'react';
import './Table.css';
import '../common.css';

export default class Table extends React.Component {
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
                <thead class={"header"}>
                    <tr>
                        {Object.keys(this.props.columns).map((column, i) => {
                            return (
                                <th className={"header-cell header-cell-alignLeft " + (i == 0 ? "primary-cell" : "")}>
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
                           <tr key={i} className={"body-row"}>
                               {
                                   Object.values(this.props.columns).map((column, i) => {
                                        return (
                                            <td class={i == 0 ? "primary-cell" : ""}>
                                                {data[column]}
                                            </td>
                                        )
                                    })
                               }
                           </tr>)
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



