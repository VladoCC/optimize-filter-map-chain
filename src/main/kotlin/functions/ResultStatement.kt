package functions

import functions.higher_order.FilterFunction
import functions.higher_order.MapFunction

data class ResultStatement(val filter: FilterFunction, val map: MapFunction) {
    fun simplify(): ResultStatement {
        filter.simplify()
        map.simplify()
        return this
    }
}