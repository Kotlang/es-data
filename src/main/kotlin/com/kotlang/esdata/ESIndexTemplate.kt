package com.kotlang.esdata

import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.CreateIndexRequest

class ESIndexTemplate(private val client: RestHighLevelClient) {

    fun getMapping(entity: ESEntity): Map<String, Any> {
        val mapping = mutableMapOf<String, Any>()

        val fields = entity.javaClass.declaredFields
        for (field in fields) {
            val fieldAnnotation = field.annotations.
            find { it is Field } as? Field
            fieldAnnotation?.let {
                mapping[field.name] = mapOf("type" to it.type.name)
            }
        }

        return mapOf("properties" to mapping)
    }

    fun createIndex(entity: ESEntity) {
        val request = CreateIndexRequest(entity.getIndexName())
        request.mapping(getMapping(entity))
        client.indices().create(request, RequestOptions.DEFAULT)
    }
}

