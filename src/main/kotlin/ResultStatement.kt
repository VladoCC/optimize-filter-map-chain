package functions

import functions.bool.FalseExpression
import functions.higher_order.FilterFunction
import functions.higher_order.MapFunction

data class ResultStatement(val filter: FilterFunction, val map: MapFunction) {
    fun simplify(): ResultStatement {
        filter.simplify()
        if (filter.expression is FalseExpression) {
            map.expression = NumExpression(0)
        } else {
            map.simplify()
        }
        return this
    }

    fun execute(list: List<Int>): List<Int> {
        return map.execute(filter.execute(list))
    }
}