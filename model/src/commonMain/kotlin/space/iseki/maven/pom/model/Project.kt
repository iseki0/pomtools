package space.iseki.maven.pom.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

@Serializable
data class Project(
    val groupId: String = "",
    val artifactId: String = "",
    val version: String = "",
    val parent: Parent? = null,
    val properties: Properties = Properties.EMPTY,
    val dependencies: List<Dependency>,
    val dependencyManagement: List<Dependency>,
) {
    @get:JvmName("coordinate")
    val coordinate: Coordinate
        get() = Coordinate(
            groupId = groupId.ifEmpty { parent?.groupId }.orEmpty(),
            artifactId = artifactId.ifEmpty { parent?.artifactId }.orEmpty(),
            version = version.ifEmpty { parent?.version }.orEmpty(),
        )
}

