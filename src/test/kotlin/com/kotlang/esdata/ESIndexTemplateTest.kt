package com.kotlang.esdata

import com.kotlang.esdata.utils.TestEntity
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ESIndexTemplateTest {
    @Test
    fun testGetMapping() {
        val entity = TestEntity(domain = "mydomain")
        val mapping = ESIndexTemplate(mock {}).getMapping(entity)
        Assertions.assertEquals(mapping,
                mapOf(
                        "properties" to mapOf(
                                "place" to mapOf("type" to "keyword"),
                                "name" to mapOf("type" to "text")
                        )
                ))
    }
}