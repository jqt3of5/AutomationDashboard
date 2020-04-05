import kotlinx.html.classes
import react.*
import react.dom.*

external interface TableProps : RProps {
    var columns : List<String>
    var data : List<List<String>>
}

class Table : RComponent<TableProps, RState>(){
    override fun RBuilder.render() {
        table {
            thead {
                tr {
                   for (column in props.columns)
                    {
                        th {
                            val index = props.columns.indexOf(column)
                            key = index.toString()

                            attrs["className"] = "header-cell header-cell-alignLeft"
                            if (index == 0)
                            {
                                attrs["className"] += " primary-cell"
                            }
                            span {
                                +column
                            }
                        }
                    }
                }
            }
            tbody {
                props.data.forEachIndexed{ i, it ->
                    tr {
                        attrs["className"] = "body-row"
                        key = i.toString()
                        it.forEachIndexed { j, it ->
                            td {
                                key = j.toString()
                                if (j == 0)
                                    attrs["className"] = "primary-cell"
                                link {
                                    +it
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
external interface SortArrowProps : RProps {
    var direction : String?
}
class ArrowIcon : RComponent<SortArrowProps, RState>() {
    fun getDirectionClass() : String {
        return when(props.direction)
        {
            "descending" -> "arrow-icon-descending"
            "ascending"-> "arrow-icon-ascending"
            else -> ""
        }
    }
    override fun RBuilder.render() {
        svg (classes = "arrow-icon ${getDirectionClass()}"){

        }
    }
}