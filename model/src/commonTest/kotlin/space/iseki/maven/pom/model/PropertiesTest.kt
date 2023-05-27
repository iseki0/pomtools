package space.iseki.maven.pom.model

import kotlin.test.Test
import kotlin.test.assertEquals

class PropertiesTest {

    private val m = mapOf(
        "foo" to "bar",
        "bar" to "\${foo}",
        "c" to "\${c}",
    )

    @Test
    fun resolve() {
        val v = Properties.from(m).resolve("aa\${foo}zz\${bar}\${c}\${foo}\${}")
        println(v)
        assertEquals("aabarzzbar\${c}bar\${}", v)
    }

    @Test
    fun eq(){
        assertEquals(Properties.from(m), Properties.from(m))
        assertEquals(Properties.from(m).hashCode(), Properties.from(m).hashCode())
        assertEquals(Properties.from(m).toString(), Properties.from(m).toString())
    }

    @Test
    fun ts(){
        println(Properties.from(m))
    }
}

