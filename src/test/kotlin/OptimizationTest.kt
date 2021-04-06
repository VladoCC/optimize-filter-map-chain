import functions.ResultStatement
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFails

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

    @Test(expected = SyntaxError::class)
    fun syntaxErrorTest() {
        convert("filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)")
    }

    @Test(expected = TypeError::class)
    fun typeErrorFilterTest() {
        convert("filter{(element+0)}%>%filter{(element<0)}%>%map{(element*element)}")
    }

    @Test(expected = TypeError::class)
    fun typeErrorMapTest() {
        convert("filter{(element>0)}%>%filter{(element<0)}%>%map{(element>element)}")
    }

    @Test
    fun normalFormEqualTest() {
        val chain = "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}"
        val normal = convert(chain)
        if (normal != null) {
            processingTest(normal) { list -> list.map { it + 10 }.filter { it > 10 }.map { it * it } }
        }
    }

    @Test
    fun noFilterTest() {
        val chain = "map{(element+10)}"
        processingChainTest(chain) {
            it.map { i -> i + 10 }
        }
    }

    @Test
    fun noFilterMinusTest() {
        val chain = "map{(element-10)}"
        processingChainTest(chain) {
            it.map { i -> i - 10 }
        }
    }

    @Test
    fun noFilterMultiplicationTest() {
        val chain = "map{(element*10)}"
        processingChainTest(chain) {
            it.map { i -> i * 10 }
        }
    }

    @Test
    fun noFilterSquareTest() {
        val chain = "map{(element*element)}"
        processingChainTest(chain) {
            it.map { i -> i * i }
        }
    }

    @Test
    fun noMapTest() {
        val chain = "filter{(element>10)}"
        processingChainTest(chain) {
            it.filter { i -> i > 10 }
        }
    }

    @Test
    fun lessTest() {
        val chain = "filter{(element<10)}"
        processingChainTest(chain) {
            it.filter { i -> i < 10 }
        }
    }

    @Test
    fun equalsTest() {
        val chain = "filter{(element=10)}"
        processingChainTest(chain) {
            it.filter { i -> i == 10 }
        }
    }

    @Test
    fun lessOrEqualsTest() {
        val chain = "filter{((element<10)|(element=10))}"
        processingChainTest(chain) {
            it.filter { i -> i <= 10 }
        }
    }

    @Test
    fun twoIntervalsTest() {
        val chain = "filter{(((element>0)&(element<10))|(element=-10))}"
        processingChainTest(chain) {
            it.filter { i -> (i in 1..9) || i == -10 }
        }
    }

    @Test
    fun intervalOrTrueTest() {
        val chain = "filter{(((element>0)&(element<10))|(1=1))}"
        processingChainTest(chain) {
            it.filter { _ -> true}
        }
    }

    @Test
    fun intervalOrFalseTest() {
        val chain = "filter{(((element>0)&(element<10))|(1=0))}"
        processingChainTest(chain) {
            it.filter { i -> (i in 1..9) }
        }
    }

    @Test
    fun intervalAndTrueTest() {
        val chain = "filter{(((element>0)&(element<10))&(1=1))}"
        processingChainTest(chain) {
            it.filter { i -> (i in 1..9) }
        }
    }

    @Test
    fun intervalAndFalseTest() {
        val chain = "filter{(((element>0)&(element<10))&(1=0))}"
        processingChainTest(chain) {
            it.filter { _ -> false }
        }
    }

    @Test
    fun trueAndFalseTest() {
        val chain = "filter{((1=1)&(1=0))}"
        processingChainTest(chain) {
            it.filter { _ -> false }
        }
    }

    @Test
    fun trueOrFalseTest() {
        val chain = "filter{((1=1)|(1=0))}"
        processingChainTest(chain) {
            it.filter { _ -> true }
        }
    }

    @Test
    fun falseOrTrueTest() {
        val chain = "filter{((1=0)|(1=1))}"
        processingChainTest(chain) {
            it.filter { _ -> true }
        }
    }

    @Test
    fun filtersEverythingTest() {
        val chain = "filter{(element>0)}%>%filter{(element<0)}"
        processingChainTest(chain) {
            it.filter { _ -> false }
        }
    }

    private fun processingChainTest(chain: String, func: (List<Int>) -> List<Int>) {
        val normal = convert(chain)
        if (normal != null) {
            processingTest(normal, func)
        }
    }

    private fun processingTest(statement: ResultStatement, func: (List<Int>) -> List<Int>) {
        for (i in (1..10000)) {
            val test = mutableListOf<Int>()
            for (j in (1..Random.nextInt(1, 1000))) {
                test.add(Random.nextInt(-1000, 1000))
            }
            val expected = func.invoke(test)
            val actual = statement.execute(test)
            assertEquals(expected, actual)
        }
    }
}