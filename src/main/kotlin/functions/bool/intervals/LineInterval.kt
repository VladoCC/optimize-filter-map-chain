package functions.bool.intervals

import functions.NumExpression
import functions.VarExpression
import functions.bool.*

class LineInterval internal constructor(p1: Int, p2: Int): SimpleInterval(p1, p2) {
    override fun toExpression(): BooleanExpression<*> {
        val point1 = NumExpression(p1 - 1)
        val point2 = NumExpression(p2 + 1)
        return AndExpression(
            MoreExpression(VarExpression, point1),
            LessExpression(VarExpression, point2)
        )
    }
}