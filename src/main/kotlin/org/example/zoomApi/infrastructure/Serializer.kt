package org.example.zoomApi.infrastructure

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import org.example.zoomApi.adapters.JsonZonedDateTimeAdapter
import java.time.ZonedDateTime
import java.util.*

object Serializer {
    @JvmStatic
    val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(ZonedDateTime::class.java, JsonZonedDateTimeAdapter())
            .build()
}