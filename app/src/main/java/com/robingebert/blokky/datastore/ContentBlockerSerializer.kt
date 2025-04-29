package com.robingebert.blokky.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

/*import com.robingebert.blokky.ContentBlockersProto.ContentBlockers

private object ContentBlockersSerializer : Serializer<ContentBlockers> {
    override val defaultValue: ContentBlockers = ContentBlockers.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): ContentBlockers =
        try { ContentBlockers.parseFrom(input) }
        catch (e: Exception) { throw CorruptionException("Cannot read proto.", e) }
    override suspend fun writeTo(t: ContentBlockers, output: OutputStream) =
        t.writeTo(output)
}*/