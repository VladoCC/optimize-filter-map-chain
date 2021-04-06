package functions.bool

import functions.Expression
import functions.IntExpression
import functions.arithmetical.ArithmeticalExpression
import functions.VarExpression
import functions.NumExpression
import functions.bool.intervals.Interval
import functions.bool.intervals.NegativeInfInterval
import functions.bool.intervals.PointInterval
import isInt
import kotlin.math.absoluteValue

class EqualsExpression(arg1: IntExpression, arg2: IntExpression): SimpleBooleanExpression(arg1, arg2) {
    override fun execute(variable: Int): Boolean {
        return arg1.execute(variable) == arg2.execute(variable)
    }
    override fun simplify(): Expression<Boolean> {
        val res = super.simplify()
        if (res != this) {
            return res
        }
        val solution = solveExpression()
        if (solution != null) {
            var expression: BooleanExpression<*>? = null
            if (solution[0].isInt()) {
                expression = EqualsExpression(VarExpression, NumExpression(solution[0].toInt()))
            }
            if (solution.size == 2 && solution[1].isInt()) {
                val secondExpression = EqualsExpression(VarExpression, NumExpression(solution[1].toInt()))
                expression = if (expression == null) {
                    secondExpression
                } else {
                    OrExpression(expression, secondExpression)
                }
            }
            return expression?: TrueExpression
        }
        return this
    }

    override fun toString(): String {
        return "($arg1=$arg2)"
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
    override fun toInterval(num: Int): Interval {
        return PointInterval(num)
    }

    override val simplificationRule: (Int, Int) -> Boolean
        get() = { arg1, arg2 -> arg1 == arg2 }
}