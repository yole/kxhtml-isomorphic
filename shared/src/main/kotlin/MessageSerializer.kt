package org.jetbrains.kxhtml.isomorphic

import kotlinx.serialization.SerialContext
import kotlinx.serialization.json.JSON

fun Message.toJSON(): String =
        JSON(context = createSerialContext()).stringify(this)

fun Message.Companion.fromJSON(json: String): Message =
        JSON(context = createSerialContext()).parse(json)

private fun createSerialContext() = SerialContext().apply {
    registerSerializer(Date::class, DateSerializer)
}
