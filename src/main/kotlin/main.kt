import functions.*
import functions.bool.AndExpression
import functions.bool.EqualsExpression
import functions.NumExpression
import functions.bool.BooleanExpression
import functions.bool.SimpleBooleanExpression
import functions.higher_order.FilterFunction
import functions.higher_order.HigherOrderFunction
import functions.higher_order.MapFunction
import xyz.avarel.aljava.Equation
import xyz.avarel.aljava.Expression
import java.lang.StringBuilder

fun main(args: Array<String>) {
    val chain = readLine()
    if (chain == null) {
        syntaxError()
    } else {
        val result = converter(chain, simplify = true)
        if (result != null) {
            println("${result.filter}%>%${result.map}")
        }
    }
}

fun converter(chain: String, simplify: Boolean = false): ResultStatement? {
    try {
        val objects = getFunctions(chain)
        val result = normalize(objects)
        return if (simplify) {
            result.simplify()
        } else {
            result
        }
    } catch (e: SyntaxException) {
        syntaxError()
    } catch (e: TypeException) {
        typeError()
    }
    return null
}

fun getFunctions(chain: String): List<HigherOrderFunction<*>> {
    val calls = chain.split("%>%")
    return calls.map {
        return@map parseHigherOrderFunction(it)
    }
}

fun normalize(list: List<HigherOrderFunction<*>>): ResultStatement {
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

fun syntaxError() {
    print("SYNTAX ERROR")
}

fun typeError() {
    print("TYPE ERROR")
}

class SyntaxException: Exception()
class TypeException: Exception()