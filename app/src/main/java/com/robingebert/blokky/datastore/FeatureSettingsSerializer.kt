package com.robingebert.blokky.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class FeatureEnabledStatus(
    val appName: String,
    val featureName: String?,
    val enabled: Boolean
)

@Serializable
data class FeatureEnabledPrefs(
    val statuses: List<FeatureEnabledStatus> = emptyList()
)

object FeatureEnabledSerializer : Serializer<FeatureEnabledPrefs> {
    override val defaultValue: FeatureEnabledPrefs
        get() = FeatureEnabledPrefs()

    override suspend fun readFrom(input: InputStream): FeatureEnabledPrefs {
        return try {
            Json.decodeFromString(
                deserializer = FeatureEnabledPrefs.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: FeatureEnabledPrefs, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = FeatureEnabledPrefs.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}
