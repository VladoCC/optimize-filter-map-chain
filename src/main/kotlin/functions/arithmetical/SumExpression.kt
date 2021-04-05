package functions.arithmetical

import functions.Expression
import functions.IntExpression
import functions.NumExpression
import functions.VarExpression

class SumExpression(arg1: IntExpression, arg2: IntExpression): ArithmeticalExpression(arg1, arg2) {
    override fun execute(variable: Int): Int {
        return arg1.execute(variable) + arg2.execute(variable)
    }
    override fun simplify(): Expression<Int> {
        super.simplify()
        var exp: Expression<Int> = this
        if (arg2 is NumExpression && (arg2 as NumExpression).num < 0) {
            exp = SubtractExpression(arg1, NumExpression(-(arg2 as NumExpression).num))
        }
        return exp
    }
    override fun toAlgExp(): xyz.avarel.aljava.Expression {
        return arg1.toAlgExp().plus(arg2.toAlgExp())
    }
    override fun toString(): String {
        return "($arg1+$arg2)"
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