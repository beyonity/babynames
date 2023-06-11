package com.bogarsoft.babynames.interfaces

interface OnShortLink {
    fun fetched(url: String)
    fun error(error: String)
}