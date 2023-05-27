package space.iseki.maven.pom.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object CoordinateStringSerializer : KSerializer<Coordinate> {
    override fun deserialize(decoder: Decoder): Coordinate = decoder.decodeString().let(Coordinate.Companion::parse)

    override val descriptor: SerialDescriptor
        get() = serialDescriptor<String>()

    override fun serialize(encoder: Encoder, value: Coordinate) {
        encoder.encodeString(value.toString())
    }
}
