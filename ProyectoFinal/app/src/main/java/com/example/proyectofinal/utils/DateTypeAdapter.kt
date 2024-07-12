package com.example.proyectofinal.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTypeAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {

    private val dateFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd",
        "MM-dd-yyyy",
        "dd-MM-yyyy",
        "yyyy.MM.dd",
        "dd/MM/yyyy",
        "EEE MMM dd HH:mm:ss zzz yyyy",
        "EEE MMM dd HH:mm:ss zzz yyyy",
        "EEE MMM dd HH:mm:ss zzz yyyy",
        "EEE MMM dd HH:mm:ss z yyyy"
    ).map { SimpleDateFormat(it, Locale.US) }

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(dateFormats[0].format(src))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        if (json == null) {
            return null
        }

        val dateStr = json.asString
        for (dateFormat in dateFormats) {
            try {
                return dateFormat.parse(dateStr)
            } catch (e: ParseException) {
                // Try the next format
            }
        }

        throw JsonParseException("Unparseable date: \"$dateStr\"")
    }
}