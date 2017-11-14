package org.jetbrains.kxhtml.isomorphic

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.resources
import io.ktor.content.static
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.util.*

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            call.respondText(renderIndexPage(), ContentType.Text.Html)
        }
        static("static") {
            resources()
        }
    }
}

private fun renderIndexPage(): String {
    val messages = generateRandomMessages(5)
    return createHTML().html {
        head {
            script(src = "static/js/frontend.bundle.js") {}
        }
        body {
            div("content") {
                for (message in messages) {
                    renderMessage(message)
                }
            }
        }
    }
}

fun generateRandomMessages(count: Int): List<Message> {
    return List(5) {
        Message(generateRandomText(), generateRandomAuthor())
    }
}

fun generateRandomText() =
    buildString {
        for (i in 1..5) {
            append(((Random().nextInt(26) + 'a'.toInt()).toChar()))
        }
    }

val authors = listOf("Alpha", "Bravo", "Charlie", "Delta", "Echo")

fun generateRandomAuthor() =
    authors[Random().nextInt(5)]

