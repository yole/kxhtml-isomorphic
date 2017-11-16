package org.jetbrains.kxhtml.isomorphic

import kotlinx.html.*

fun HtmlBlockTag.renderMessage(message: Message) {
    div(classes = "message") {
        +message.text
        i {
            + " by "
            + message.author
            + " at "
            + message.date.toReadableDateTimeString()
        }
    }
}
