package io.palette.data.models

import android.annotation.SuppressLint
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class Response<out T>(
        val status: Status,
        val data: T? = null,
        val error: Throwable? = null
) {
    enum class Status { LOADING, SUCCESS, ERROR }

    companion object {
        fun <T> loading(): Response<T> = Response(Status.LOADING, null, null)
        fun <T> success(data: T? = null): Response<T> = Response(Status.SUCCESS, data, null)
        fun <T> error(error: Throwable): Response<T> = Response(Status.ERROR, null, error)
    }
}

data class Optional<out T>(val value: T? = null) {
    fun isEmpty() = value == null
    fun isNotEmpty() = value != null
}

enum class Source { CAMERA, GALLERY }

@Parcelize
@SuppressLint("ParcelCreator")
data class Unsplash(
        @Json(name = "id") val id: String = "",
        @Json(name = "created_at") val createdAt: String = "",
        @Json(name = "updated_at") val updatedAt: String = "",
        @Json(name = "width") val width: Long = 0L,
        @Json(name = "height") val height: Long = 0L,
        @Json(name = "description") val description: String? = "",
        @Json(name = "urls") val urls: Urls? = null,
        @Json(name = "links") val links: Links? = null,
        @Json(name = "user") val user: User? = null
) : Parcelable {
    companion object {

        fun from(uri: String): Unsplash {
            return Unsplash(
                    id = "",
                    createdAt = "",
                    updatedAt = "",
                    width = 2040L,
                    height = 2900L,
                    description = "",
                    urls = Urls(uri, uri, uri, uri, uri),
                    links = null,
                    user = null
            )
        }
    }
}

@Parcelize
@SuppressLint("ParcelCreator")
data class Urls(
        @Json(name = "raw") val raw: String = "",
        @Json(name = "full") val full: String = "",
        @Json(name = "regular") val regular: String = "",
        @Json(name = "small") val small: String = "",
        @Json(name = "thumb") val thumb: String = ""
) : Parcelable

@Parcelize
@SuppressLint("ParcelCreator")
data class Links(
        @Json(name = "self") val self: String = "",
        @Json(name = "html") val html: String = "",
        @Json(name = "download") val download: String = "",
        @Json(name = "download_location") val downloadLocation: String = ""
) : Parcelable

@Parcelize
@SuppressLint("ParcelCreator")
data class User(
        @Json(name = "id") val id: String = "",
        @Json(name = "username") val userName: String = "",
        @Json(name = "name") val name: String = ""
) : Parcelable

data class GeneratedPalette(
        val hexCode: String,
        val rgb: String,
        val population: Int
)