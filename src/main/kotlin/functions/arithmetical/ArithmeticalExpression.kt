package functions.arithmetical

import functions.Expression
import functions.IntExpression
import functions.NumExpression
import functions.VarExpression

abstract class ArithmeticalExpression(protected var arg1: IntExpression,
                                      protected var arg2: IntExpression
): IntExpression() {
    fun containsVar(): Boolean {
        if (arg1 is VarExpression || arg2 is VarExpression) {
            return true
        }
        var result = false
        if (arg1 is ArithmeticalExpression) {
            result = result || (arg1 as ArithmeticalExpression).containsVar()
        }
        if (arg2 is ArithmeticalExpression) {
            result = result || (arg2 as ArithmeticalExpression).containsVar()
        }
        return result
    }

    override fun simplify(): Expression<Int> {
        arg1 = arg1.simplify() as IntExpression
        arg2 = arg2.simplify() as IntExpression
        if (arg1 is ArithmeticalExpression && !(arg1 as ArithmeticalExpression).containsVar()) {
            arg1 = NumExpression(arg1.execute(0))
        }
        if (arg2 is ArithmeticalExpression && !(arg2 as ArithmeticalExpression).containsVar()) {
            arg2 = NumExpression(arg2.execute(0))
        }
        return this
    }
}