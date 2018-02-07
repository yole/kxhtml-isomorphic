package org.jetbrains.kxhtml.isomorphic

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.content.resources
import io.ktor.content.static
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import kotlinx.serialization.SerialContext
import kotlinx.serialization.json.JSON
import java.util.*

data class Committer(val name: String, val date: String)

data class Commit(val message: String, val author: Committer, val committer: Committer)

data class CommitData(val commit: Commit)

val messages = mutableListOf<Message>()
var lastSentMessageIndex = 0

fun Application.main() {
    runBlocking {
        val client = HttpClient(Apache) {
            install(JsonFeature)
        }

        val data = client.get<Array<CommitData>>("https://api.github.com/repos/jetbrains/kotlin/commits")
        data.mapTo(messages) { datum ->
            Message(datum.commit.message.smartTruncate(), datum.commit.author.name, parseDate(datum.commit.author.date))
        }
    }

    install(DefaultHeaders)
    install(CallLogging)
    install(StatusPages) {
        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError)
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
    val messages = messages.take(5)
    lastSentMessageIndex = 5
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
    return messages.drop(lastSentMessageIndex++).firstOrNull()?.toJSON() ?: "[]"
}

private fun String.smartTruncate(): String {
    if (length < 50) return this
    val index = lastIndexOf(' ', 50)
    if (index < 0) return substring(0, 50) + "..."
    return substring(0, index).trim() + "..."
}
