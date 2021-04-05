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
    if (innerExp.startsWith("element")) {
        operatorPos = 7
    } else if (innerExp[0].isDigit()) {
        operatorPos = findNum(innerExp)
    } else if (innerExp[0] == '-') {
        operatorPos = findNum(innerExp.substring(1)) + 1
    } else if (innerExp[0] == '(') {
        operatorPos = findComplex(innerExp)
    } else {
        operatorPos = -1
    }
    if (operatorPos > -1) {
        return BinaryOperation(innerExp[operatorPos],
            innerExp.substring(0, operatorPos),
            innerExp.substring(operatorPos + 1, innerExp.length))
    } else {
        throw SyntaxException()
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
        else -> throw TypeException()
    }
}

public fun parseIntExpression(exp: String): IntExpression {
    if (exp.startsWith("(")) {
        return parseComplexIntExpression(exp)
    } else if (exp == "element") {
        return VarExpression
    } else if (exp.matches(Regex("-?\\d+"))) {
        return NumExpression(exp.toInt())
    }
    throw SyntaxException()
}

public fun parseBooleanExpression(exp: String): BooleanExpression<*> {
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
        else -> throw TypeException()
    }
}

public fun parseHigherOrderFunction(func: String): HigherOrderFunction<*> {
    if (func.matches(Regex("map\\{.+}"))) {
        val exp = parseIntExpression(func.substring(4, func.length - 1))
        return MapFunction(exp)
    } else if (func.matches(Regex("filter\\{.+}"))) {
        val exp = parseBooleanExpression(func.substring(7, func.length - 1))
        return FilterFunction(exp)
    }
    throw SyntaxException()
}