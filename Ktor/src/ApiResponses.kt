package com.example

import kotlinx.serialization.Serializable

enum class ObjectType {
    Fixture,
    Test,
    TestStep,
    Hub,
    Node,
}
@Serializable
data class ObjectId(val objectType : ObjectType, val id : String)

@Serializable
data class Error(val message : String, val status : String = "error")