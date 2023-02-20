data class Project(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val parent: Parent?,
    val properties: Map<String, String> = emptyMap(),
    val dependencies: List<Dependency> = emptyList(),
    val dependencyManagements: List<Dependency> = emptyList(),
)

data class Parent(val projectId: String, val artifactId: String, val version: String, val relativePath: String)
data class Dependency(
    val projectId: String,
    val artifactId: String,
    val version: String,
    val scope: String,
    val optional: Boolean,
    val exclusions: List<Exclusion>,
)

data class Exclusion(val groupId: String, val artifactId: String)
