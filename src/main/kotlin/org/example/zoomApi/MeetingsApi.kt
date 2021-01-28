package org.example.zoomApi

import org.example.zoomApi.models.CreateMeetingRequest
import org.example.zoomApi.models.CreateMeetingResponse
import org.example.zoomApi.infrastructure.*

class MeetingsApi(basePath: String = "https://api.zoom.us/v2") : ApiClient(basePath) {
    @Suppress("UNCHECKED_CAST")
    fun createMeeting(token: String, userId: String, request: CreateMeetingRequest) : CreateMeetingResponse {
        val acceptsHeaders: Map<String,String> = mapOf("Accept" to "application/json, application/xml")
        val tokenHeaders: Map<String,String> = mapOf("Authorization" to "Bearer $token")
        val headers: MutableMap<String,String> = mutableMapOf()
        headers.putAll(acceptsHeaders)
        headers.putAll(tokenHeaders)

        val config = RequestConfig(
            RequestMethod.POST,
            "/users/${userId}/meetings",
            query = mapOf(),
            headers = headers
        )
        val response = request<CreateMeetingResponse>(
            config,
            request
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as CreateMeetingResponse
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
}