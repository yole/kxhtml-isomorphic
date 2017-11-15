package org.jetbrains.kxhtml.isomorphic

import kotlinx.serialization.KInput
import kotlinx.serialization.KOutput
import kotlinx.serialization.KSerialClassDesc
import kotlinx.serialization.KSerializer
import kotlinx.serialization.internal.SerialClassDescImpl

object DateSerializer : KSerializer<Date> {
    override fun load(input: KInput): Date = Date(input.readLongValue())

    override fun save(output: KOutput, obj: Date) {
        output.writeLongValue(obj.getTime().toLong())
    }

    override val serialClassDesc: KSerialClassDesc
        get() = SerialClassDescImpl("org.jetbrains.kxhtml.isomorphic.Date")
}

