package com.kotlang.esdata.search.dsl

import com.fasterxml.jackson.annotation.JsonInclude

abstract class FilterLogic

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Match (
    var match: Map<String, String>? = null
): FilterLogic()

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Term (
    var term: Map<String, String>? = null
): FilterLogic()

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Range (
    var range: Map<String, Any>? = null
): FilterLogic()

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BoolFilter(
    var must: List<FilterLogic>? = null,
    var filter: List<FilterLogic>? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Query (
    var bool: BoolFilter? = null
)

enum class SortOrder {
    asc,
    desc
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SearchQuery (
    var query: Query? = null,
    var sort: List<Map<String, SortOrder>>? = null
)