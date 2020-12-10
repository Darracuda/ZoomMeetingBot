package org.example.zoomApi.models

import java.time.Instant

data class CreateMeetingResponse (
    val host_id: String? = null,
    val id: String? = null,
    val uuid: String? = null,
    val start_time: Instant? = null,
    val created_at: Instant? = null,
    val start_url: String? = null,
    val join_url: String? = null,
    val encrypted_password: String? = null,
    val pstn_password: String? = null,
    val host_email: String? = null,
)

