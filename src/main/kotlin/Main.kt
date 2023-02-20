import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}

private typealias Callback<T> = (T) -> Unit

class PomSaxHandler

data class Element(val tag: String)
interface Handler {
    fun enter(el: Element): Handler? = null
    fun chars(charArray: CharArray, from: Int, len: Int) {}
    fun leave() {}
}

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

class ProjectHandler(val callback: Callback<Project>) : Handler {
    private var groupId = ""
    private var artifactId = ""
    private var version = ""
    private var parent: Parent? = null
    private var properties = emptyMap<String, String>()
    private var dependencies = emptyList<Dependency>()
    private var dependencyManagements = emptyList<Dependency>()
    override fun enter(el: Element) = when (el.tag) {
        "groupId" -> TextHandler { groupId = it }
        "artifactId" -> TextHandler { artifactId = it }
        "version" -> TextHandler { version = it }
        "parent" -> ParentHandler { parent = it }
        "properties" -> PropertiesHandler { properties = it }
        "dependencies" -> DependenciesHandler { dependencies = it }
        "dependencyManagements" -> DependenciesHandler { dependencyManagements = it }
        else -> null
    }

    override fun leave() =
        callback(Project(groupId, artifactId, version, parent, properties, dependencies, dependencyManagements))
}

class ExclusionsHandler(val callback: Callback<List<Exclusion>>) : Handler {
    private val list = ArrayList<Exclusion>()
    override fun enter(el: Element) = when (el.tag) {
        "exclusion" -> ExclusionHandler { list += it }
        else -> null
    }

    override fun leave() = callback(list)
}

class ExclusionHandler(val callback: Callback<Exclusion>) : Handler {
    private var groupId = ""
    private var artifactId = ""
    override fun enter(el: Element) = when (el.tag) {
        "groupId" -> TextHandler { groupId = it }
        "artifactId" -> TextHandler { artifactId = it }
        else -> null
    }

    override fun leave() = callback(Exclusion(groupId, artifactId))
}

class DependencyManagementsHandler(val callback: Callback<List<Dependency>>) : Handler {
    override fun enter(el: Element) = when (el.tag) {
        "dependencies" -> DependenciesHandler(callback)
        else -> null
    }
}

class DependenciesHandler(val callback: Callback<List<Dependency>>) : Handler {
    private val list = ArrayList<Dependency>()
    override fun enter(el: Element) = when (el.tag) {
        "dependency" -> DependencyHandler { list += it }
        else -> null
    }
}

class DependencyHandler(val callback: Callback<Dependency>) : Handler {
    private var groupId = ""
    private var artifactId = ""
    private var version = ""
    private var scope = ""
    private var optional = false
    private var exclusions: List<Exclusion> = emptyList()
    override fun enter(el: Element) = when (el.tag) {
        "groupId" -> TextHandler { groupId = it }
        "artifactId" -> TextHandler { artifactId = it }
        "version" -> TextHandler { version = it }
        "scope" -> TextHandler { scope = it }
        "optional" -> TextHandler { optional = it.toBooleanStrict() }
        "exclusions" -> ExclusionsHandler { exclusions = it }
        else -> null
    }

    override fun leave() = callback(Dependency(groupId, artifactId, version, scope, optional, exclusions))
}

class ParentHandler(val callback: Callback<Parent>) : Handler {
    private var groupId = ""
    private var artifactId = ""
    private var version = ""
    private var relativePath = ""
    override fun enter(el: Element) = when (el.tag) {
        "groupId" -> TextHandler { groupId = it }
        "artifactId" -> TextHandler { artifactId = it }
        "version" -> TextHandler { version = it }
        "relativePath" -> TextHandler { relativePath = it }
        else -> null
    }

    override fun leave() = callback(Parent(groupId, artifactId, version, relativePath))
}

class PropertiesHandler(val callback: Callback<Map<String, String>>) : Handler {
    private val m = HashMap<String, String>()
    override fun enter(el: Element) = TextHandler { m[el.tag] = it }
    override fun leave() = callback(m)
}

class TextHandler(val callback: Callback<String>) : Handler {
    private val cb = StringBuilder()
    override fun chars(charArray: CharArray, from: Int, len: Int) {
        cb.appendRange(charArray, from, from + len)
    }

    override fun leave() {
        callback(cb.toString())
    }
}

class SaxHandler : DefaultHandler() {
    private val stack = ArrayDeque<Handler>()
    override fun startElement(uri: String?, localName: String?, qName: String, attributes: Attributes?) {
        TODO()
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        stack.last().chars(ch, start, length)
    }

    override fun endElement(uri: String?, localName: String?, qName: String) {
        TODO()
    }
}
