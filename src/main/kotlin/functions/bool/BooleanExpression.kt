package functions.bool

import functions.Expression
import functions.bool.intervals.Interval

abstract class BooleanExpression<T>(var arg1: Expression<T>, var arg2: Expression<T>):
    Expression<Boolean> {
    override fun simplify(): Expression<Boolean> {
        arg1 = arg1.simplify()
        arg2 = arg2.simplify()
        return this
    }

    abstract fun toInterval(): Interval?
}