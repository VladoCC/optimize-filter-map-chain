package functions.bool

import functions.Expression
import functions.IntExpression
import functions.NumExpression
import functions.arithmetical.ArithmeticalExpression
import functions.bool.intervals.CombinedInterval
import functions.bool.intervals.EverythingInterval
import functions.bool.intervals.Interval

class OrExpression(arg1: Expression<Boolean>, arg2: Expression<Boolean>): ComplexBooleanExpression(arg1, arg2) {
    override fun execute(variable: Int): Boolean {
        return arg1.execute(variable) || arg2.execute(variable)
    }
    override fun simplify(): Expression<Boolean> {
        super.simplify()
        return this
    }

    override fun toInterval(): Interval? {
        val interval1 = (arg1 as BooleanExpression<*>).toInterval()
        val interval2 = (arg2 as BooleanExpression<*>).toInterval()
        if (interval1 == null || interval2 == null) {
            return if(interval1 !is EverythingInterval && interval2 !is EverythingInterval) {
                null
            } else {
                EverythingInterval
            }
        }
        return CombinedInterval(interval1).union(interval2)
    }

    override fun toString(): String {
        return "($arg1|$arg2)"
    }
    override fun replaceElem(with: IntExpression) {
        arg1.replaceElem(with)
        arg2.replaceElem(with)
    }

}