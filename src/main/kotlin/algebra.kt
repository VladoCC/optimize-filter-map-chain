import xyz.avarel.aljava.Expression
import java.lang.StringBuilder
import kotlin.math.absoluteValue

fun Double.isInt(): Boolean {
    return absoluteValue % 1.0 < 0.005
}

fun Expression.toParsableString(): String {
    var processed = toString().replace(" + ", "+")
        .replace(" - ", "-")
        .replace(Regex("\\dx")) {
            return@replace it.value[0] + "*" + it.value[1]
        }
        .replace(Regex("\\d+\\*x")) {
            return@replace "(${it.value})"
        }
        .replace(Regex("x\\^\\d+")) {
            val stringBuilder = StringBuilder("(x")
            val count = it.value.substring(2).toInt() - 1
            for (i in (1..count)) {
                stringBuilder.append("*x)")
            }
            return@replace stringBuilder.toString()
        }
        .replace("x", "element")
        .replace("-", "+-")

    val result = addBrackets(processed, '+')
        .replace("+(-", "-(")
    return result
}

private fun addBrackets(expression: String, operator: Char): String {
    val split = expression.split(operator)
    var result = ""
    for (i in (0 until split.size - 1)) {
        result += '(' + split[i] + operator
    }
    result += split[split.size - 1]
    for (i in (1 until split.size)) {
        result += ')'
    }
    return result
}