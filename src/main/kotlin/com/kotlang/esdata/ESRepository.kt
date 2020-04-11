package com.kotlang.esdata

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import java.util.*

abstract class ESRepository<T: ESEntity> (private val client: RestHighLevelClient) {
    private val mapper = jacksonObjectMapper()
    private val index = ESIndexTemplate(client)

    fun save(entity: T): T {
        index.getOrCreateIndex(entity)

        val request = IndexRequest(entity.getIndexName())
        request.id(entity.id())
        request.source(mapper.writeValueAsString(entity), XContentType.JSON)
        client.index(request, RequestOptions.DEFAULT)
        return findById(entity).get()
    }

    fun findById(entity: T): Optional<T> {
        index.getOrCreateIndex(entity)

        val request = GetRequest(entity.getIndexName(), entity.id())
        val response = client.get(request, RequestOptions.DEFAULT)
        return if (response.isExists) {
            val resEntity = mapper.readValue(response.sourceAsString, object : TypeReference<T>() {})
            Optional.of(resEntity)
        } else {
            Optional.empty()
        }
    }
}