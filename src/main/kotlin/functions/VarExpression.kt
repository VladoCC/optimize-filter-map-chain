package functions

object VarExpression: IntExpression() {
    override fun toAlgExp(): xyz.avarel.aljava.Expression {
        return xyz.avarel.aljava.Expression("x")
    }

    override fun execute(variable: Int): Int {
        return variable;
    }
    override fun simplify(): Expression<Int> {
        return this
    }
    override fun toString(): String {
        return "element"
    }
    override fun replaceElem(with: IntExpression) {}
}