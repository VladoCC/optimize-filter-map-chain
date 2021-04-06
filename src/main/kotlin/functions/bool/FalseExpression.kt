package functions.bool

import functions.IntExpression
import functions.NumExpression
import functions.bool.intervals.Interval
import functions.bool.intervals.NothingInterval

object FalseExpression: SimpleBooleanExpression(NumExpression(1), NumExpression(0)) {
    override fun execute(variable: Int) = false
    override fun replaceElem(with: IntExpression) {}
    override fun toString(): String {
        return "(1=0)"
    }
    override fun toInterval(num: Int): Interval {
        return NothingInterval
    }

    override val simplificationRule: (Int, Int) -> Boolean
        get() = { _, _ -> true }
}