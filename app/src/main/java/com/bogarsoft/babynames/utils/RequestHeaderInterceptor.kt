package com.bogarsoft.babynames.utils

import okhttp3.Interceptor
import okhttp3.Response

class RequestHeaderInterceptor(
    private val name: String,
    private val value: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(name, value)
            .build()
        return chain.proceed(request)
    }
}