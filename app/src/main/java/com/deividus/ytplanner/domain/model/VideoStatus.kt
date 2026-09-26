package com.deividus.ytplanner.domain.model

/**
 * Lifecycle of a piece of content, from first idea to being live on the channel.
 */
enum class VideoStatus(val displayName: String) {
    IDEA("Idea"),
    GUION("Guion"),
    EDICION("Edicion"),
    PROGRAMADO("Programado"),
    PUBLICADO("Publicado")
}
