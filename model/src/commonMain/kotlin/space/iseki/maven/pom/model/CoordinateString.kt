@file:JvmName("-CoordinateString")

package space.iseki.maven.pom.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

typealias CoordinateString = @Serializable(CoordinateStringSerializer::class) Coordinate
