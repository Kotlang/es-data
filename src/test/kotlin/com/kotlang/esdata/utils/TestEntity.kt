package com.kotlang.esdata.utils

import com.kotlang.esdata.ESEntity
import com.kotlang.esdata.Field
import com.kotlang.esdata.FieldType

data class TestEntity (
    val domain: String,
    @Field(type = FieldType.keyword)
    var place: String? = null,
    @Field(type = FieldType.text)
    var name: String? = null
): ESEntity() {
    override fun getIndexName(): String = "test_index_$domain"
    override fun id(): String = "mockId"
}