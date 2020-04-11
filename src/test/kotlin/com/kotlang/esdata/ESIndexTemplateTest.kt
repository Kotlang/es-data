package com.kotlang.esdata

import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class ESIndexTemplateTest {

    class MockEntity(
            val domain: String,
            @Field(type = FieldType.keyword)
            var place: String? = null,
            @Field(type = FieldType.text)
            var name: String? = null
    ): ESEntity() {
        override fun getIndexName(): String = "test_index_$domain"
        override fun id(): String = "mockId"
    }

    @Test
    fun testGetMapping() {
        val entity = MockEntity(domain = "mydomain")
        val esClient = Mockito.mock(RestHighLevelClient::class.java)

        val mapping = ESIndexTemplate(esClient).getMapping(entity)
        Assertions.assertEquals(mapping,
                mapOf(
                        "properties" to mapOf(
                                "place" to mapOf("type" to "keyword"),
                                "name" to mapOf("type" to "text")
                        )
                ))
    }
}