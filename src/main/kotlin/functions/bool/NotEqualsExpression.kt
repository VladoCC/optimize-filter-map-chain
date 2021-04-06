package functions.bool

import functions.Expression
import functions.IntExpression
import functions.NumExpression
import functions.VarExpression
import kotlin.math.ceil
import kotlin.math.floor

abstract class NotEqualsExpression(arg1: IntExpression, arg2: IntExpression):
    SimpleBooleanExpression(arg1, arg2) {

    override fun simplify(): Expression<Boolean> {
        val res = super.simplify()
        if (res != this) {
            return res
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
}