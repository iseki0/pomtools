package space.iseki.maven.pom.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic

@Serializable
data class Coordinate(
    val groupId: String,
    val artifactId: String,
    val version: String,
) {
    val isValid: Boolean
        get() = groupId.isNotBlank() && groupId.noDollar()
                && artifactId.isNotBlank() && artifactId.noDollar()
                && version.isNotBlank() && version.noDollar()

    override fun toString(): String = when {
        version.isNotEmpty() -> "$groupId:$artifactId:$version"
        else -> "$groupId:$artifactId"
    }

    companion object {
        @JvmStatic
        fun parse(s: String): Coordinate {
            val c = s.split(':')
            return when (c.size) {
                2 -> Coordinate(c[0], c[1], "")
                3 -> Coordinate(c[0], c[1], c[2])
                else -> throw IllegalArgumentException("bad coordinate: $s")
            }
        }
    }
}

