package functions.higher_order

import functions.Expression
import functions.IntExpression
import functions.VarExpression
import parseIntExpression
import toParsableString

class MapFunction(expression: IntExpression) : HigherOrderFunction<Int>(expression) {
    override fun execute(list: List<Int>): List<Int> {
        return list.map { expression.execute(it) }
    }
    override fun toString(): String {
        return "map{$expression}"
    }
    override fun replaceElem(with: IntExpression) {
        if (expression is VarExpression) {
            expression = with
        } else {
            super.replaceElem(with)
        }
    }

    override fun simplify() {
        super.simplify()
        expression = parseIntExpression((expression as IntExpression).toAlgExp().toParsableString())
    }
}