import jdk.nashorn.internal.objects.NativeArray.forEach
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class OptimizationTest {
    @Test
    fun reproducibilityTest() {
        val chain = "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}"
        val result = getFunctions(chain).joinToString("%>%")
        assertEquals(chain, result)
    }

    @Test
    fun simpleExecutionTest() {
        val chain = "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}"
        val functions = getFunctions(chain)
        val test = listOf<Int>(0, 10, 3, 5, -5, -20, 2, -6)
        val expected = test.map { it + 10 }.filter { it > 10 }.map { it * it }
        var actual = test
        functions.forEach {
            actual = it.execute(actual)
        }
        assertEquals(expected, actual)
    }

    @Test
    fun normalFormEqualTest() {
        val chain = "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}"
        val normal = normalize(getFunctions(chain))
        for (i in (1..100)) {
            val test = mutableListOf<Int>()
            for (j in (1..100)) {
                test.add(Random.nextInt(-1000, 1000))
            }
            val expected = test.map { it + 10 }.filter { it > 10 }.map { it * it }
            var actual = normal.map.execute(normal.filter.execute(test))
            assertEquals(expected, actual)
        }
    }
}