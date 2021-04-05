package functions.bool.intervals

import functions.NumExpression
import functions.VarExpression
import functions.bool.EqualsExpression

class PointInterval internal constructor(point: Int): SimpleInterval(point, point) {
    override fun toExpression() = EqualsExpression(VarExpression, NumExpression(p1))
}