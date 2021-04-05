package functions.higher_order

import functions.Expression
import functions.IntExpression

abstract class HigherOrderFunction<T>(var expression: Expression<T>) {
    abstract fun execute(list: List<Int>): List<Int>
    open fun simplify() { expression = expression.simplify() }
    open fun replaceElem(with: IntExpression) { expression.replaceElem(with) }
}