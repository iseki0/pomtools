@file:JvmName("-Dependency")

package space.iseki.maven.pom.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
data class Dependency(
    val groupId: String = "",
    val artifactId: String = "",
    val version: String = "",
    val scope: String = "",
    val optional: Boolean = false,
) {
    @get:JvmName("coordinate")
    val coordinate: Coordinate
        get() = Coordinate(groupId, artifactId, version)

    override fun toString(): String = "$coordinate $tagString"

    private val tagString: String
        get() = when {
            scope.isNotEmpty() -> "($scope, optional=$optional)"
            else -> "(optional=$optional)"
        }
}
