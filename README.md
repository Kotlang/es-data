# es-data
Simple transparent ORM for elasticsearch and Kotlin supporting rolling indices.

- Provides Entity-Repository interfaces for elasticsearch in Kotlin/Java.
- Supports rolling indices particularly to index documents in different indices based on a field in the document. 
For ex. to index documents from different countries in different elasticsearch indices. Look for domain
based rolling index in below example.

## Installing

### Gradle
Add following details to build.gradle.kts

```sh
repositories {
	maven { url = uri("https://maven.pkg.github.com/Kotlang/es-data") }
}

dependencies {
            implementation("com.kotlang:EsData:0.16")
}
```

Username is your github username.
Token can be generated from https://github.com/settings/tokens

## Usage

```kotlin
class UserEntity(
            @Field(type = FieldType.keyword)
            val id: String,
            @Field(type = FieldType.keyword)
            val domain: String,
            @Field(type = FieldType.keyword)
            var place: String? = null,
            @Field(type = FieldType.text)
            var name: String? = null
    ): ESEntity() {
        override fun getIndexName(): String = "test_index_$domain"
        override fun id(): String = id
    }
    
val client = ESClient(RestClient.builder(HttpHost("localhost", 9200, "http")))
val repository = ESRepository(client)
// Will create a separate index as test_index_in
repository.save(UserEntity("p001", "in", "Bengaluru", "Ghanshyam"))
// Will search for document id p001 in index test_index_in
val user = repository.findById(UserEntity(id = "p001", domain = "in"))
```

## Recommended usage
Create a singleton for ESRepository wrapping ESClient.


