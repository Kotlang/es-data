package com.kotlang.esdata

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.search.builder.SearchSourceBuilder

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
        return findById(entity)!!
    }

    inline fun<reified T: ESEntity> findById(entity: T): T? {
        index.getOrCreateIndex(entity)

        val request = GetRequest(entity.getIndexName(), entity.id())
        val response = client.get(request)
        return if (response.isExists) {
            mapper.readValue(response.sourceAsString, object : TypeReference<T>() {})
        } else null
    }

    inline fun<reified T: ESEntity> deleteById(entity: T) {
        index.getOrCreateIndex(entity)

        val request = DeleteRequest(entity.getIndexName(), entity.id())
        client.delete(request)
    }

    inline fun<reified T: ESEntity> search(indexName: String,
                                           searchSourceBuilder: SearchSourceBuilder): List<T> {
        val searchRequest = SearchRequest(indexName)
        searchRequest.source(searchSourceBuilder)
        val result = client.search(searchRequest).hits
        return result.map { it.sourceAsString }
            .map { mapper.readValue(it, object : TypeReference<T>() {}) }
    }
}