# es-data
Simple transparent ORM for elasticsearch and Kotlin supporting rolling indices.

- Provides Entity-Repository interfaces for elasticsearch in Kotlin/Java.
- Supports rolling indices particularly to index documents in different indices based on a field in the document. 
For ex. to index documents from different departments in different elasticsearch indices simply override getIndexName in entity 
to index document based on department.

## Usage

```kotlin
class UserEntity(
            val domain: String,
            @Field(type = FieldType.keyword)
            var place: String? = null,
            @Field(type = FieldType.text)
            var name: String? = null
    ): ESEntity() {
        override fun getIndexName(): String = "test_index_$domain"
        override fun id(): String = "mockId"
    }
    
class UserRepository (client: RestHighLevelClient): ESRepository<UserEntity>(client)
```
UserRepository will give all standard methods to findById, save based on rolling index.
ESIndexTemplate lets one create index with specific mappings defined using annotations on Entity.