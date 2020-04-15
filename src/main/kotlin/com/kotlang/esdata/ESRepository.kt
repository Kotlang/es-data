package com.kotlang.esdata

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.common.xcontent.XContentType
import java.util.*

open class ESRepository (
    val client: ESClient,
    val index: ESIndexTemplate = ESIndexTemplate(client)
) {
    val mapper: ObjectMapper = jacksonObjectMapper().configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    inline fun<reified T: ESEntity> save(entity: T): T {
        index.getOrCreateIndex(entity)

        val request = IndexRequest(entity.getIndexName())
        request.id(entity.id())
        request.source(mapper.writeValueAsString(entity), XContentType.JSON)
        client.index(request)
        return findById(entity).get()
    }

    inline fun<reified T: ESEntity> findById(entity: T): Optional<T> {
        index.getOrCreateIndex(entity)

        val request = GetRequest(entity.getIndexName(), entity.id())
        val response = client.get(request)
        return if (response.isExists) {
            val resEntity = mapper.readValue(response.sourceAsString, object : TypeReference<T>() {})
            Optional.of(resEntity)
        } else {
            Optional.empty()
        }
    }

    inline fun<reified T: ESEntity> deleteById(entity: T) {
        index.getOrCreateIndex(entity)

        val request = DeleteRequest(entity.getIndexName(), entity.id())
        client.delete(request)
    }
}