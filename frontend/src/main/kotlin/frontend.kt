package org.jetbrains.kxhtml.isomorphic.frontend

import kotlinx.html.div
import kotlinx.html.dom.append
import org.jetbrains.kxhtml.isomorphic.Message
import org.jetbrains.kxhtml.isomorphic.fromJSON
import org.jetbrains.kxhtml.isomorphic.renderMessage
import org.w3c.dom.HTMLElement
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.startCoroutine
import kotlin.coroutines.experimental.suspendCoroutine
import kotlin.js.Promise

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

fun <T> async(x: suspend () -> T): Promise<T> {
    return Promise { resolve, reject ->
        x.startCoroutine(object : Continuation<T> {
            override val context = EmptyCoroutineContext

            override fun resume(value: T) {
                resolve(value)
            }

            override fun resumeWithException(exception: Throwable) {
                reject(exception)
            }
        })
    }
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
