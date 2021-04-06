import functions.*
import functions.bool.AndExpression
import functions.bool.EqualsExpression
import functions.NumExpression
import functions.bool.BooleanExpression
import functions.higher_order.FilterFunction
import functions.higher_order.HigherOrderFunction
import functions.higher_order.MapFunction

fun main(args: Array<String>) {
    val chain = readLine()
    if (chain == null) {
        throw SyntaxError("Chain not found")
    } else {
        val result = convert(chain, simplify = true)
        if (result != null) {
            println("${result.filter}%>%${result.map}")
        }
    }
}

fun convert(chain: String, simplify: Boolean = false): ResultStatement? {
    val objects = getFunctions(chain)
    val result = normalize(objects)
    return if (simplify) {
        result.simplify()
    } else {
        result
    }
}

fun getFunctions(chain: String): List<HigherOrderFunction<*>> {
    val calls = chain.split("%>%")
    return calls.map {
        return@map parseHigherOrderFunction(it)
    }
}

private fun normalize(list: List<HigherOrderFunction<*>>): ResultStatement {
    var filter: FilterFunction? = null
    var map: MapFunction? = null

    for (it in list) {
        if (map != null) {
            it.replaceElem(map.expression as IntExpression)
        }

        if (it is FilterFunction) {
            if (filter != null) {
                filter.expression = AndExpression(filter.expression, it.expression)
            } else {
                filter = FilterFunction(it.expression as BooleanExpression<*>)
            }
        } else if (it is MapFunction) {
            map = it
        }
    }

    filter = filter?: FilterFunction(EqualsExpression(NumExpression(0), NumExpression(0)))
    map = map?: MapFunction(VarExpression)
    return ResultStatement(filter, map)
}

class SyntaxError(text: String): Error(text)
class TypeError(text: String): Error(text)