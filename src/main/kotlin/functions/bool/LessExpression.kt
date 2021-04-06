package functions.bool

import functions.IntExpression
import functions.VarExpression
import functions.bool.intervals.Interval
import functions.bool.intervals.NegativeInfInterval

class LessExpression(arg1: IntExpression, arg2: IntExpression): SimpleBooleanExpression(arg1, arg2) {
    override fun execute(variable: Int): Boolean {
        return arg1.execute(variable) < arg2.execute(variable)
    }
    override fun toInterval(num: Int): Interval {
        return NegativeInfInterval(num - 1)
    }

    override val simplificationRule: (Int, Int) -> Boolean
        get() = { arg1, arg2 -> arg1 < arg2 }

    override fun toString(): String {
        return "($arg1<$arg2)"
    }
    override fun replaceElem(with: IntExpression) {
        if (arg1 is VarExpression) {
            arg1 = with
        } else {
            arg1.replaceElem(with)
        }

        if (arg2 is VarExpression) {
            arg2 = with
        } else {
            arg2.replaceElem(with)
        }
    }
}