package space.iseki.maven.pom.model

import kotlinx.serialization.Serializable

@Serializable
data class Parent(
    val groupId: String = "",
    val artifactId: String = "",
    val version: String = "",
    val relativePath: String = "",
){
    val isLocal: Boolean
        get() = relativePath.isNotEmpty()
    val coordinate: Coordinate
        get() = Coordinate(groupId, artifactId, version)
}
