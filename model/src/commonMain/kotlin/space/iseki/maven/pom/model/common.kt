@file:JvmName("-Common")
@file:Suppress("NOTHING_TO_INLINE")
@file:JvmSynthetic

package space.iseki.maven.pom.model

import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

internal inline fun String.hasDollar() = '$' in this

internal inline fun String.noDollar() = !hasDollar()

internal fun <T> List<T>.reverseIterator() = iterator { for (i in lastIndex downTo 0) yield(this@reverseIterator[i]) }
