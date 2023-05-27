@file:JvmName("-Properties")

package space.iseki.maven.pom.model

import kotlinx.serialization.Serializable
import kotlin.jvm.*

@Serializable(PropertiesSerializer::class)
class Properties private constructor(private val m: Map<String, String>) : Map<String, String> by m {
    override fun toString(): String = m.toString()
    override fun hashCode(): Int = m.hashCode()
    override fun equals(other: Any?): Boolean = other is Properties && other.m == m

    companion object {
        @JvmField
        val EMPTY = Properties(emptyMap())

        @JvmName("-from")
        @JvmSynthetic
        @JvmStatic
        internal fun wrap(m: Map<String, String>) = Properties(m)

        @JvmStatic
        fun from(m: Map<String, String>) = Properties(m.toMap())

    }

    fun resolve(input: String) = buildString(input.length) { resolveRec(input, 0, m, null) }

}

private tailrec fun StringBuilder.resolveRec(input: String, off: Int, m: Map<String, String>, crd: CircleRefDetector?) {
    if (off > input.lastIndex) return
    val base = input.indexOf("\${", off)
    val rb = if (base < 0) -1 else input.indexOf('}', base + 2)
    if (rb < 0) {
        append(input, off, input.length)
        return
    }
    append(input, off, base)
    val key = input.substring(base + 2, rb)
    val value = m[key]
    if (key.isNotEmpty() && value != null  && !match(key, crd)) {
        @Suppress("NON_TAIL_RECURSIVE_CALL") resolveRec(value, 0, m, crd.wrap(key))
    } else {
        append(input, base, rb + 1)
    }
    return resolveRec(input, rb + 1, m, crd)
}

private data class CircleRefDetector(val ref: String, val parent: CircleRefDetector? = null)

private fun CircleRefDetector?.wrap(ref: String) = CircleRefDetector(ref, this)

private tailrec fun match(ref: String, crd: CircleRefDetector?): Boolean {
    if (ref == crd?.ref) return true
    return match(ref, crd?.parent ?: return false)
}

