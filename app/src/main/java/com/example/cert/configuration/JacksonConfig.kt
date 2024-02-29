package com.example.cert.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.Module
import dagger.Provides

@Module
class JacksonConfig {

    @Provides
    fun jacksonMapper(): ObjectMapper {
        return objectMapper
    }

    companion object {
        val objectMapper
            get() = ObjectMapper().apply {
                registerModule(JavaTimeModule())
                registerModule(KotlinModule())

                propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
                disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            }
    }
}
