package functions

interface Expression<T> {
    fun execute(variable: Int): T
    fun simplify(): Expression<T>
    fun replaceElem(with: IntExpression)
}