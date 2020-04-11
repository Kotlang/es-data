package com.kotlang.esdata

enum class FieldType {
    keyword,
    text
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val type: FieldType)