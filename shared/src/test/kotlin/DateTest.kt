package org.jetbrains.kxhtml.isomorphic

import kotlin.test.Test
import kotlin.test.assertEquals

class DateTest {
    @Test fun testParse() {
        val date = parseDate("2017-10-24T13:31:19")
        assertEquals(2017, date.getFullYear())
        assertEquals(9, date.getMonth())
        assertEquals(24, date.getDate())
    }
}
