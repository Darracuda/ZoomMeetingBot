package org.example.zoomApi.models

import java.time.Instant

data class CreateMeetingResponse (
    val host_id: String,
    val id: String,
    val uuid: String,
    val start_time: Instant,
    val created_at: Instant,
    val start_url: String,
    val join_url: String,
    val encrypted_password: String,
    val pstn_password: String,
    val host_email: String,
)

