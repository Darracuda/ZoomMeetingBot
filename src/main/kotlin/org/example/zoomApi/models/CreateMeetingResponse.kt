package org.example.zoomApi.models

import java.time.ZonedDateTime

/**
 *
 * @param host_id ID of the user set as host of meeting
 * @param id Meeting ID, also known as meeting number
 * @param uuid Meeting unique ID
 */
data class CreateMeetingResponse (
    val host_id: String? = null,
    val id: String? = null,
    val uuid: String? = null,
    val start_time: ZonedDateTime? = null,
    val created_at: ZonedDateTime? = null,
    val start_url: String? = null,
    val join_url: String? = null,
    val encrypted_password: String? = null,
    val pstn_password: String? = null,
    val host_email: String? = null,
)

