package functions.arithmetical

import functions.Expression
import functions.IntExpression
import functions.VarExpression

class MultiplyExpression(arg1: IntExpression, arg2: IntExpression): ArithmeticalExpression(arg1, arg2) {
    override fun execute(variable: Int): Int {
        return arg1.execute(variable) * arg2.execute(variable)
    }
    override fun simplify(): Expression<Int> {
        super.simplify()
        return this
    }

    override fun toAlgExp(): xyz.avarel.aljava.Expression {
        return arg1.toAlgExp().times(arg2.toAlgExp())
    }

    override fun toString(): String {
        return "($arg1*$arg2)"
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