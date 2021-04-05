package functions.bool

import functions.Expression
import functions.IntExpression
import functions.NumExpression
import functions.VarExpression
import functions.arithmetical.ArithmeticalExpression
import functions.bool.intervals.Interval
import xyz.avarel.aljava.Equation
import xyz.avarel.aljava.Fraction

abstract class SimpleBooleanExpression(arg1: IntExpression, arg2: IntExpression) :
    BooleanExpression<Int>(arg1, arg2) {
    override fun simplify(): Expression<Boolean> {
        super.simplify()
        return this
    }

    protected fun solveExpression(): DoubleArray? {
        val equation = Equation((arg1 as IntExpression).toAlgExp(), (arg2 as IntExpression).toAlgExp())
        return try {
            val solution = equation.solveFor("x")
            return if (solution.size == 1) {
                doubleArrayOf((solution[0] as xyz.avarel.aljava.Expression).constant().toDouble())
            } else if (solution.size == 2) {
                if (solution[0] is Double) {
                    doubleArrayOf(solution[0] as Double, solution[1] as Double)
                } else if (solution[0] is Fraction) {
                    doubleArrayOf((solution[0] as Fraction).toDouble(), (solution[1] as Fraction).toDouble())
                } else {
                    return null
                }
            } else {
                return null
            }
        } catch (e: Throwable) {
            null
        }
    }
    override fun toInterval(): Interval? {
        return if (arg1 is VarExpression && arg2 is NumExpression) {
            toInterval((arg2 as NumExpression).num)
        } else {
            null
        }
    }
    protected abstract fun toInterval(num: Int): Interval
}