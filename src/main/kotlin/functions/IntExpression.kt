package functions

import functions.arithmetical.ArithmeticalExpression

abstract class IntExpression: Expression<Int> {
    abstract fun toAlgExp(): xyz.avarel.aljava.Expression

    fun containsVar(): Boolean {
        return (this is VarExpression) || (this is ArithmeticalExpression && !this.containsVarArg())
    }
}