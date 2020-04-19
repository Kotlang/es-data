package com.kotlang.esdata.search.dsl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SearchQueryTest {
    private val mapper: ObjectMapper = jacksonObjectMapper()

    @Test
    fun testBuildSearchQuery() {
        val searchQuery = SearchQuery(
            query = Query(
                bool = BoolFilter(
                    must = listOf(
                        Match( mapOf( "title" to "Search" ) ),
                        Match( mapOf( "content" to "ElasticSearch" ) )
                    ),

                    filter = listOf(
                        Term( mapOf( "status" to "published" ) ),
                        Range( mapOf( "publish_date" to  mapOf("gte" to "2015-01-01") ) )
                    )
                )
            )
        )

        Assertions.assertEquals(
            mapper.writeValueAsString(searchQuery),
            """
                {
                  "query": {
                    "bool": {
                      "must": [
                        { "match": { "title":   "Search"        }},
                        { "match": { "content": "ElasticSearch" }}
                      ],
                      "filter": [
                        { "term":  { "status": "published" }},
                        { "range": { "publish_date": { "gte": "2015-01-01" }}}
                      ]
                    }
                  }
                }
            """.trimIndent().replace("\n","").replace(" ", "")
        )
    }

    @Test
    fun testBuildSearchQueryWithSort() {
        val searchQuery = SearchQuery(
            sort = listOf(
                mapOf("post_date" to SortOrder.desc),
                mapOf("user" to SortOrder.asc)
            ),
            query = Query(
                BoolFilter(
                    filter = listOf(
                        Term(mapOf("user" to "kimchy"))
                    )
                )
            )
        )

        Assertions.assertEquals(
            mapper.writeValueAsString(searchQuery),
            """
                {"query":{"bool":{"filter":[{"term":{"user":"kimchy"}}]}},"sort":[{"post_date":"desc"},{"user":"asc"}]}
            """.trimIndent()
        )
    }
}