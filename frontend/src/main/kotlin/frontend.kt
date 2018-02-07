package org.jetbrains.kxhtml.isomorphic.frontend

import kotlinx.coroutines.experimental.async
import kotlinx.html.div
import kotlinx.html.dom.append
import org.jetbrains.kxhtml.isomorphic.Message
import org.jetbrains.kxhtml.isomorphic.fromJSON
import org.jetbrains.kxhtml.isomorphic.renderMessage
import org.w3c.dom.HTMLElement
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window
import kotlin.coroutines.experimental.suspendCoroutine

suspend fun httpGet(url: String): String = suspendCoroutine { c ->
    val xhr = XMLHttpRequest()
    xhr.onreadystatechange = {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status / 100 == 2) {
                c.resume(xhr.response as String)
            }
            else {
                c.resumeWithException(RuntimeException("HTTP error: ${xhr.status}"))
            }
        }
        null
    }
    xhr.open("GET", url)
    xhr.send()
}

fun fetchNewMessages() {
    async {
        console.log("Fetching new messages")
        val data = httpGet("http://localhost:8080/newMessages")
        val message = Message.fromJSON(data)
        val contentDiv = document.getElementsByClassName("content").item(0) as HTMLElement
        contentDiv.append.div {
            renderMessage(message)
        }
    }
}

fun main(args: Array<String>) {
    console.log("Frontend loaded")
    window.setInterval(::fetchNewMessages, 1000)
}
