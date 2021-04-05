package functions.bool

import functions.Expression
import functions.IntExpression
import functions.bool.intervals.CombinedInterval
import functions.bool.intervals.Interval
import functions.bool.intervals.NothingInterval

class AndExpression(arg1: Expression<Boolean>, arg2: Expression<Boolean>): ComplexBooleanExpression(arg1, arg2) {
    override fun execute(variable: Int): Boolean {
        return arg1.execute(variable) && arg2.execute(variable)
    }
    override fun simplify(): Expression<Boolean> {
        super.simplify()
        return this
    }
    override fun toString(): String {
        return "($arg1&$arg2)"
    }
    override fun replaceElem(with: IntExpression) {
        arg1.replaceElem(with)
        arg2.replaceElem(with)
    }
    override fun toInterval(): Interval? {
        val interval1 = (arg1 as BooleanExpression<*>).toInterval()
        val interval2 = (arg2 as BooleanExpression<*>).toInterval()
        if (interval1 == null || interval2 == null) {
            return if(interval1 !is NothingInterval && interval2 !is NothingInterval) {
                null
            } else {
                NothingInterval
            }
        }
        return CombinedInterval(interval1).intersection(interval2)
    }
}