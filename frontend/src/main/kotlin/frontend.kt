package org.jetbrains.kxhtml.isomorphic.frontend

import kotlin.browser.window

fun main(args: Array<String>) {
    window.setInterval({
        console.log("Update!")

    }, 1000)
}
