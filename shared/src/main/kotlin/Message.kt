package org.jetbrains.kxhtml.isomorphic

import kotlinx.serialization.Serializable

@Serializable
data class Message(val text: String, val author: String, val date: Date) {
    companion object {
    }
}
