package com.kotlang.esdata

import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.IndicesClient
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClientBuilder
import org.elasticsearch.client.RestHighLevelClient

open class ESClient (clientBuilder: RestClientBuilder) {
    private val client = RestHighLevelClient(clientBuilder)

    open fun index(request: IndexRequest): IndexResponse = client.index(request, RequestOptions.DEFAULT)
    open fun get(request: GetRequest): GetResponse = client.get(request, RequestOptions.DEFAULT)
    open fun delete(request: DeleteRequest): DeleteResponse = client.delete(request, RequestOptions.DEFAULT)
    open fun indices(): IndicesClient = client.indices()
}