package com.kotlang.esdata

abstract class ESEntity {
    abstract fun getIndexName(): String
    abstract fun id(): String
}