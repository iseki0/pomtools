package space.iseki.maven.pom.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer

internal object PropertiesSerializer : KSerializer<Properties> {
    private val serializer = serializer<Map<String, String>>()
    override val descriptor: SerialDescriptor
        get() = serializer.descriptor

    override fun deserialize(decoder: Decoder): Properties = Properties.wrap(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: Properties) {
        serializer.serialize(encoder, value)
    }

}
