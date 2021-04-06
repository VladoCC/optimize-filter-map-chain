package functions.bool

import functions.IntExpression
import functions.NumExpression
import functions.bool.intervals.EverythingInterval
import functions.bool.intervals.Interval
import functions.bool.intervals.NegativeInfInterval

object TrueExpression: SimpleBooleanExpression(NumExpression(1), NumExpression(1)) {
    override fun execute(variable: Int) = true
    override fun replaceElem(with: IntExpression) { }
    override fun toString(): String {
        return "(1=1)"
    }
    override fun toInterval(num: Int): Interval {
        return EverythingInterval
    }

    override val simplificationRule: (Int, Int) -> Boolean
        get() = { _, _ -> true }
}