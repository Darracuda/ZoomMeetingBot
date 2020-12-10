package org.example.zoomApi.adapters

import com.squareup.moshi.*
import java.time.*

class JsonInstantAdapter : JsonAdapter<Instant>() {

    override fun toJson(writer: JsonWriter, input: Instant?) {
        val s = input?.toString()
        writer.value(s)
    }

    override fun fromJson(reader: JsonReader): Instant? {
        val s = reader.readJsonValue() as CharSequence
        if (!s.endsWith("Z")) {
            throw IllegalArgumentException("Unable to convert DateTime from Json")
        }
        return Instant.parse(s)
    }
}
