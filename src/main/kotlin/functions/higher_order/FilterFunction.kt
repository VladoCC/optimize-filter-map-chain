package functions.higher_order

import functions.bool.BooleanExpression

class FilterFunction(expression: BooleanExpression<*>): HigherOrderFunction<Boolean>(expression) {
    override fun execute(list: List<Int>): List<Int> {
        return list.filter { expression.execute(it) }
    }
    override fun toString(): String {
        return "filter{$expression}"
    }

    override fun simplify() {
        super.simplify()
        val interval = (expression as BooleanExpression<*>).toInterval()
        if (interval != null) {
            expression = interval.toExpression()
        }
    }
}