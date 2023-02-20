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

data class Parent(val projectId: String, val artifactId: String, val version: String, val relativePath: String)

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
        else -> error("unknown element: ${el.tag}")
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
