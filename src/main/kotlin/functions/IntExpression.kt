package functions

abstract class IntExpression: Expression<Int> {
    abstract fun toAlgExp(): xyz.avarel.aljava.Expression
}