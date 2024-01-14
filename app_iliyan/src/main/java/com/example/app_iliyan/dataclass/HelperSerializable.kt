package com.example.app_iliyan.dataclass

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Timestamp

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Timestamp::class)
object TimestampSerializer : KSerializer<Timestamp> {
  override fun serialize(encoder: Encoder, value: Timestamp) {
    encoder.encodeLong(value.time)
  }

  override fun deserialize(decoder: Decoder): Timestamp {
    return Timestamp(decoder.decodeLong())
  }
}
