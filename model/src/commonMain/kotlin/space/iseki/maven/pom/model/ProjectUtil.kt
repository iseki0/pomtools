@file:Suppress("unused")

package space.iseki.maven.pom.model

import kotlin.jvm.JvmStatic

object ProjectUtil {

    @JvmStatic
    fun mergeParents(list: List<Project>): Project {
        require(list.isNotEmpty()) { "no projects" }
        val p = list.first()
        val properties = buildMap { for (project in list.reverseIterator()) putAll(project.properties) }
            .let(Properties.Companion::wrap)
        with(properties) {
            val dependencies = sequence { list.forEach { yieldAll(it.dependencies) } }
                .map { it.resolveProperty() }
                .distinctBy { it.key }
                .toList()
            val dependencyManagement = sequence { list.forEach { yieldAll(it.dependencyManagement) } }
                .map { it.resolveProperty() }
                .distinctBy { it.key }
                .toList()
            val (groupId, artifactId, version) = p.coordinate.resolveProperty()
            return Project(
                groupId = groupId,
                artifactId = artifactId,
                version = version,
                parent = null,
                properties = properties,
                dependencies = dependencies,
                dependencyManagement = dependencyManagement,
            )
        }
    }

    @JvmStatic
    fun mergeImports(project: Project, imports: Collection<Project>): Project {
        val importMap = imports.associateBy { it.coordinate }
        return project.copy(
            dependencyManagement = project.dependencyManagement.flatMap {
                when (it.scope) {
                    "import" -> importMap[it.coordinate]?.dependencyManagement ?: listOf(it)
                    else -> listOf(it)
                }
            }.distinctBy { it.key }.toList()
        )
    }


    @JvmStatic
    private val Dependency.key: Pair<String, String>
        get() = groupId to artifactId

    context (Properties)
    @JvmStatic
    private fun Dependency.resolveProperty() = copy(
        groupId = resolve(groupId),
        artifactId = resolve(artifactId),
        version = resolve(version),
        scope = resolve(scope),
    )

    context (Properties)
    @JvmStatic
    private fun Coordinate.resolveProperty() = copy(
        groupId = resolve(groupId),
        artifactId = resolve(artifactId),
        version = resolve(version),
    )

}

