package com.kotlang.esdata

import com.kotlang.esdata.utils.TestEntity
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.elasticsearch.action.get.GetResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ESRepositoryTest {
    private fun getDocumentFromMockResponse(mockGetResponse: GetResponse): TestEntity {
        val esClient = mock<ESClient> {
            on { get(any()) }.thenReturn(mockGetResponse)
        }

        val mockIndexTemplate = mock<ESIndexTemplate> {
            on { getOrCreateIndex(any()) }.then {  }
        }

        val testRepo = ESRepository(esClient, mockIndexTemplate)
        return testRepo.findById(TestEntity(domain = "test_domain")).get()
    }

    @Test
    fun testFindById() {
        val mockGetResponse = mock<GetResponse> {
            on { sourceAsString } doReturn """
                { 
                    "domain": "test_domain",
                    "place": "India",
                    "name": "Ghanshyam"
                }
            """.trimIndent()

            on { isExists } doReturn true
        }

        val entityFromES = getDocumentFromMockResponse(mockGetResponse)

        Assertions.assertEquals(entityFromES,
            TestEntity(domain = "test_domain", place = "India", name = "Ghanshyam"))
    }

    @Test
    fun doNotFailOnUnknownProperties() {
        val mockGetResponse = mock<GetResponse> {
            on { sourceAsString } doReturn """
                { 
                    "domain": "test_domain",
                    "place": "India",
                    "name": "Ghanshyam",
                    "extraProperty": "noUse"
                }
            """.trimIndent()

            on { isExists } doReturn true
        }

        val entityFromES = getDocumentFromMockResponse(mockGetResponse)

        Assertions.assertEquals(entityFromES,
            TestEntity(domain = "test_domain", place = "India", name = "Ghanshyam"))
    }
}