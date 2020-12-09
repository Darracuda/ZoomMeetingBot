package org.example.zoomApi.models

import com.squareup.moshi.Json
import java.time.ZonedDateTime

/**
 * Meeting object
 */
data class CreateMeetingRequest(
        val topic: String? = null,
        val type: MeetingType? = null,
        /* Meeting start time. When using a format like \"yyyy-MM-dd'T'HH:mm:ss'Z'\", always use GMT time. When using a format like \"yyyy-MM-dd'T'HH:mm:ss\", you should use local time and you will need to specify the time zone. Only used for scheduled meetings and recurring meetings with fixed time. */
        val start_time: ZonedDateTime? = null,
        /* Meeting duration (minutes). Used for scheduled meetings only */
        val duration: Int? = null,
        /* Timezone to format start_time. For example, \"America/Los_Angeles\". For scheduled meetings only. Please reference our [timezone](#timezones) list for supported timezones and their formats. */
        val timezone: kotlin.String? = null,
        /* Password to join the meeting. Password may only contain the following characters: [a-z A-Z 0-9 @ - _ *]. Max of 10 characters. */
        val password: String? = null,
        val agenda: String? = null,
        val tracking_fields: Array<MeetingInfoTrackingFields>? = null,
        val settings: MeetingSettings? = null
) {
    enum class MeetingType(val value: Int) {
        @Json(name = "1")
        instantMeeting(1),
        @Json(name = "2")
        scheduledMeeting(2),
        @Json(name = "3")
        recurringMeetingWithNoFixedTime(3),
        @Json(name = "8")
        recurringMeetingWithFixedTime(8),
    }

    /**
     * Meeting Settings
     * @param host_video Start video when host joins meeting
     * @param participant_video Start video when participants join meeting
     * @param cn_meeting Host meeting in China
     * @param in_meeting Host meeting in India
     * @param join_before_host Allow participants to join the meeting before the host starts the meeting. Only used for scheduled or recurring meetings.
     * @param mute_upon_entry Mute participants upon entry
     * @param watermark Add watermark when viewing shared screen
     * @param use_pmi Use Personal Meeting ID. Only used for scheduled meetings and recurring meetings with no fixed time.
     * @param approval_type
     * @param registration_type Registration type. Used for recurring meeting with fixed time only.
     * @param audio Determine how participants can join the audio portion of the meeting
     * @param auto_recording
     * @param enforce_login Only signed-in users can join this meeting
     * @param enforce_login_domains Only signed-in users with specified domains can join meetings
     * @param alternative_hosts Alternative hosts emails or IDs. Multiple value separated by comma.
     * @param close_registration Close registration after event date
     * @param waiting_room Enable waiting room
     */
    data class MeetingSettings (
            val host_video: Boolean? = null,
            val participant_video: Boolean? = null,
            val cn_meeting: Boolean? = null,
            val in_meeting: Boolean? = null,
            val join_before_host: Boolean? = null,
            val mute_upon_entry: Boolean? = null,
            val watermark: Boolean? = null,
            val use_pmi: Boolean? = null,
            val approval_type: ApprovalType? = null,
            val registration_type: RegistrationType? = null,
            val audio: Audio? = null,
            val auto_recording: AutoRecording? = null,
            val enforce_login: Boolean? = null,
            val enforce_login_domains: String? = null,
            val alternative_hosts: String? = null,
            val close_registration: Boolean? = null,
            val waiting_room: Boolean? = null
    ) {

        /**
         * Determine how participants can join the audio portion of the meeting
         * Values: both,telephony,voip
         */
        enum class Audio(val value: String){
            @Json(name = "both") both("both"),
            @Json(name = "telephony") telephony("telephony"),
            @Json(name = "voip") voip("voip");
        }

        /**
         *
         * Values: local,cloud,none
         */
        enum class AutoRecording(val value: String){
            @Json(name = "local") local("local"),
            @Json(name = "cloud") cloud("cloud"),
            @Json(name = "none") none("none");
        }

        enum class ApprovalType(val value: Int){
            @Json(name = "1") automaticallyApprove(1),
            @Json(name = "2") manuallyApprove(2),
            @Json(name = "3") noRegistrationRequired(3),
        }

        enum class RegistrationType(val value: Int){
            @Json(name = "0") registerOnceAndAttendAny(0),
            @Json(name = "1") registerEach(1),
            @Json(name = "2") registerOnceAndChoose(2),
        }
    }

    /**
     *
     * @param field Tracking fields type
     * @param value Tracking fields value
     */
    data class MeetingInfoTrackingFields (
            val field: String? = null,
            val value: String? = null
    )
}