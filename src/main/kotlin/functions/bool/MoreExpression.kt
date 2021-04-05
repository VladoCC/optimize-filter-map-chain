package functions.bool

import functions.Expression
import functions.IntExpression
import functions.arithmetical.ArithmeticalExpression
import functions.VarExpression
import functions.NumExpression
import functions.bool.intervals.Interval
import functions.bool.intervals.NegativeInfInterval
import functions.bool.intervals.PositiveInfInterval
import isInt
import kotlin.math.ceil
import kotlin.math.floor

class MoreExpression(arg1: IntExpression, arg2: IntExpression): SimpleBooleanExpression(arg1, arg2) {
    override fun execute(variable: Int): Boolean {
        return arg1.execute(variable) > arg2.execute(variable)
    }
    override fun simplify(): Expression<Boolean> {
        super.simplify()
        if (arg1 is ArithmeticalExpression && !(arg1 as ArithmeticalExpression).containsVar() &&
            arg2 is ArithmeticalExpression && !(arg2 as ArithmeticalExpression).containsVar()) {
            val result1 = arg1.execute(0)
            val result2 = arg2.execute(0)
            return if (result1 > result2) {
                TrueExpression
            } else {
                FalseExpression
            }
        }
        val solutions = solveExpression()
        if (solutions != null) {
            if (solutions.size == 2) {
                solutions.sort()
                val r1 = floor(solutions[0]).toInt()
                val result = execute(r1 - 1)
                if (floor(solutions[1]) - floor(solutions[0]) < 0.005) {
                    return if (result) {
                        TrueExpression
                    } else {
                        FalseExpression
                    }
                } else {
                    return if (result) {
                        OrExpression(
                            LessExpression(VarExpression, NumExpression(ceil(solutions[0]).toInt())),
                            MoreExpression(VarExpression, NumExpression(floor(solutions[1]).toInt())))
                    } else {
                        AndExpression(
                            MoreExpression(VarExpression, NumExpression(floor(solutions[0]).toInt())),
                            LessExpression(VarExpression, NumExpression(ceil(solutions[1]).toInt())))
                    }
                }
            } else if (solutions.size == 1) {
                val root = ceil(solutions[0]).toInt()
                return if (execute(root - 2)) {
                    LessExpression(VarExpression, NumExpression(root))
                } else {
                    MoreExpression(VarExpression, NumExpression(root))
                }
            }
        }
        return this
    }
    override fun toString(): String {
        return "($arg1>$arg2)"
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
        return PositiveInfInterval(num + 1)
    }
}