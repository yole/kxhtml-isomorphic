package org.jetbrains.kxhtml.isomorphic

import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import kotlinx.html.i

fun HtmlBlockTag.renderMessage(message: Message) {
    div {
        +message.text
        i {
            + " by "
            + message.author
            + " at "
            + message.date.toReadableDateTimeString()
        }
    }
}
