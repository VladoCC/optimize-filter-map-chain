package functions

class NumExpression(val num: Int): IntExpression() {
    override fun toAlgExp(): xyz.avarel.aljava.Expression {
        return xyz.avarel.aljava.Expression(num)
    }

    override fun execute(variable: Int): Int {
        return num;
    }
    override fun simplify(): Expression<Int> {
        return this
    }
    override fun toString(): String {
        return "$num"
    }

    override fun replaceElem(with: IntExpression) {}
}