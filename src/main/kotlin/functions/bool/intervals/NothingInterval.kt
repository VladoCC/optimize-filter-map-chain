package functions.bool.intervals

import functions.bool.FalseExpression

object NothingInterval: SimpleInterval(Int.MAX_VALUE, Int.MAX_VALUE) {
    override fun intersection(interval: SimpleInterval): SimpleInterval {
        return this
    }

    override fun toExpression() = FalseExpression
}