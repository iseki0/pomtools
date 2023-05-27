package space.iseki.maven.pom.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoordinateTest {
    private val coordinate = Coordinate("group", "artifact", "version")
    private val parent = Parent("group", "artifact", "version", "")

    @Serializable
    private data class StringWrapper(val s: CoordinateString)

    @Test
    fun testCoordinateSerialization() {
        val t = Json.encodeToString(StringWrapper(coordinate))
        println(t)
        assertEquals(coordinate, Json.decodeFromString<StringWrapper>(t).s)
    }

    @Test
    fun testValid() {
        assertTrue { coordinate.isValid }
    }

    @Test
    fun testIs() {
        assertTrue { !parent.isLocal }
        assertEquals(coordinate, parent.coordinate)
    }

}
