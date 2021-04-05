package functions.bool

import functions.Expression

abstract class ComplexBooleanExpression(arg1: Expression<Boolean>, arg2: Expression<Boolean>) :
    BooleanExpression<Boolean>(arg1, arg2) {
}