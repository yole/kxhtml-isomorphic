package org.jetbrains.kxhtml.isomorphic

import kotlinx.serialization.*

@Serializable
data class Message(val text: String, val author: String)

