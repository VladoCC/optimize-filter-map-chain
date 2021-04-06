import functions.*
import functions.bool.*
import functions.arithmetical.MultiplyExpression
import functions.NumExpression
import functions.arithmetical.SubtractExpression
import functions.arithmetical.SumExpression
import functions.higher_order.FilterFunction
import functions.higher_order.HigherOrderFunction
import functions.higher_order.MapFunction

private fun findComplex(exp: String): Int {
    var counter = 0
    exp.forEachIndexed { i, it ->
        if (it == '(') {
            counter++
        } else if (it == ')') {
            counter--
        }

        if (counter == 0) {
            return i + 1
        }
    }
    return -1
}

private fun findNum(exp: String): Int{
    exp.forEachIndexed { i, it ->
        if (!it.isDigit()) {
            return i
        }
    }
    return  -1
}

private data class BinaryOperation(val operator: Char, val arg1: String, val arg2: String)

private fun parseBinaryOperation(exp: String): BinaryOperation {
    val innerExp = exp.substring(1, exp.length - 1)
    val operatorPos: Int
    operatorPos = when {
        innerExp.startsWith("element") -> {
            7
        }
        innerExp[0].isDigit() -> {
            findNum(innerExp)
        }
        innerExp[0] == '-' -> {
            findNum(innerExp.substring(1)) + 1
        }
        innerExp[0] == '(' -> {
            findComplex(innerExp)
        }
        else -> {
            -1
        }
    }
    if (operatorPos > -1) {
        return BinaryOperation(innerExp[operatorPos],
            innerExp.substring(0, operatorPos),
            innerExp.substring(operatorPos + 1, innerExp.length))
    } else {
        throw SyntaxError("Invalid syntax for binary expression in the string: $exp")
    }
}

private fun parseComplexIntExpression(exp: String): IntExpression {
    val operation = parseBinaryOperation(exp)
    return when (operation.operator) {
        '+' -> SumExpression(parseIntExpression(operation.arg1),
            parseIntExpression(operation.arg2))
        '-' -> SubtractExpression(parseIntExpression(operation.arg1),
            parseIntExpression(operation.arg2))
        '*' -> MultiplyExpression(parseIntExpression(operation.arg1),
            parseIntExpression(operation.arg2))
        else -> throw TypeError("Arithmetical expression not found in the string: $exp")
    }
}

fun parseIntExpression(exp: String): IntExpression {
    return when {
        exp.startsWith("(") -> {
            parseComplexIntExpression(exp)
        }
        exp == "element" -> {
            VarExpression
        }
        exp.matches(Regex("-?\\d+")) -> {
            NumExpression(exp.toInt())
        }
        else -> throw SyntaxError("Invalid syntax for int expression in the string: $exp")
    }
}

fun parseBooleanExpression(exp: String): BooleanExpression<*> {
    val operation = parseBinaryOperation(exp)

    return when (operation.operator) {
       '&' -> AndExpression(parseBooleanExpression(operation.arg1),
           parseBooleanExpression(operation.arg2))
       '|' -> OrExpression(parseBooleanExpression(operation.arg1),
           parseBooleanExpression(operation.arg2))
       '<' -> LessExpression(parseIntExpression(operation.arg1),
           parseIntExpression(operation.arg2))
       '>' -> MoreExpression(parseIntExpression(operation.arg1),
           parseIntExpression(operation.arg2))
       '=' -> EqualsExpression(parseIntExpression(operation.arg1),
           parseIntExpression(operation.arg2))
        else -> throw TypeError("Boolean expression not found in the string: $exp")
    }
}

fun parseHigherOrderFunction(func: String): HigherOrderFunction<*> {
    if (func.matches(Regex("map\\{.+}"))) {
        val exp = parseIntExpression(func.substring(4, func.length - 1))
        return MapFunction(exp)
    } else if (func.matches(Regex("filter\\{.+}"))) {
        val exp = parseBooleanExpression(func.substring(7, func.length - 1))
        return FilterFunction(exp)
    }
    throw SyntaxError("Invalid syntax for higher order function in the string: $func")
}