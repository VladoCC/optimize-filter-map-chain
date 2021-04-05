package functions.bool.intervals

import functions.NumExpression
import functions.VarExpression
import functions.bool.*

class NegativeInfInterval(p2: Int): SimpleInterval(Int.MIN_VALUE, p2) {
    override fun toExpression(): BooleanExpression<*> {
        val point2 = NumExpression(p2 + 1)
        return LessExpression(VarExpression, point2)
    }
}