package functions.bool.intervals

import functions.bool.TrueExpression

object EverythingInterval: SimpleInterval(Int.MIN_VALUE, Int.MAX_VALUE) {
    override fun union(interval: SimpleInterval): Interval {
        return this
    }

    override fun toExpression() = TrueExpression
}