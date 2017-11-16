package org.jetbrains.kxhtml.isomorphic

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.resources
import io.ktor.content.static
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import kotlinx.serialization.SerialContext
import kotlinx.serialization.json.JSON
import java.util.*

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(StatusPages) {
        exception<Throwable> { cause ->
            call.response.status(HttpStatusCode.InternalServerError)
        }
    }
    install(Routing) {
        get("/") {
            call.respondText(renderIndexPage(), ContentType.Text.Html)
        }
        get("/newMessages") {
            call.respondText(serializeNewMessages(), ContentType.Application.Json)
        }
        static("static/js") {
            resources("js")
        }
        static("static/css") {
            resources("css")
        }
    }
    val port = environment.config.config("ktor").config("deployment").property("port").getString()
    println("Backend running on http://localhost:$port")
}

private fun renderIndexPage(): String {
    val messages = generateRandomMessages(5)
    return createHTML().html {
        head {
            script(src = "static/js/frontend.bundle.js") {}
            styleLink("static/css/style.css")
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

private fun serializeNewMessages(): String {
    val messages = generateRandomMessages(1)
    return messages[0].toJSON()
}

fun generateRandomMessages(count: Int): List<Message> {
    return List(count) {
        Message(generateRandomText(), generateRandomAuthor(), Date())
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

