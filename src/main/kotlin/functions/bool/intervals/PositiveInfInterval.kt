package functions.bool.intervals

import functions.NumExpression
import functions.VarExpression
import functions.bool.*

class PositiveInfInterval(p1: Int): SimpleInterval(p1, Int.MAX_VALUE) {
    override fun toExpression(): BooleanExpression<*> {
        val point1 = NumExpression(p1 - 1)
        return MoreExpression(VarExpression, point1)
    }
}