package org.example.settings

import org.example.zoomApi.models.CreateMeetingRequest
import java.io.File
import java.io.FileInputStream
import java.util.*

class SettingsManager(private val propFile: File) {

    var imapHost: String? = ""
    var imapPort: String? = ""
    var imapLogin: String? = ""
    var imapPassword: String? = ""

    var smtpHost: String? = ""
    var smtpPort: String? = ""
    var smtpLogin: String? = ""
    var smtpPassword: String? = ""
    var smtpEmailAddressFrom: String? = ""

    var zoomLogin: String? = ""
    var zoomPassword: String? = ""
    var zoomToken: String? = ""

    var hostVideo: Boolean = false
    var participantVideo: Boolean = false
    var cnMeeting: Boolean = false
    var inMeeting: Boolean = false
    var joinBeforeHost: Boolean = false
    var muteUponEntry: Boolean = false
    var watermark: Boolean = false
    var usePmi: Boolean = false
    lateinit var approvalType: CreateMeetingRequest.MeetingSettings.ApprovalType
    lateinit var audio: CreateMeetingRequest.MeetingSettings.Audio
    lateinit var autoRecording: CreateMeetingRequest.MeetingSettings.AutoRecording



    fun loadProp() {
        val prop = Properties()
        FileInputStream(propFile).use { prop.load(it) }

        imapHost = prop.getProperty("imap.host")
        imapPort = prop.getProperty("imap.port")
        imapLogin = prop.getProperty("imap.login")
        imapPassword = prop.getProperty("imap.password")

        smtpHost = prop.getProperty("smtp.host")
        smtpPort = prop.getProperty("smtp.port")
        smtpLogin = prop.getProperty("smtp.login")
        smtpPassword = prop.getProperty("smtp.password")
        smtpEmailAddressFrom = prop.getProperty("smtp.emailAddressFrom")

        zoomLogin = prop.getProperty("zoomMeeting.login")
        zoomPassword = prop.getProperty("zoomMeeting.password")
        zoomToken = prop.getProperty("zoomMeeting.token")

        hostVideo = prop.getProperty("zoomMeeting.host_video").toBoolean()
        participantVideo = prop.getProperty("zoomMeeting.participant_video").toBoolean()
        cnMeeting = prop.getProperty("zoomMeeting.cn_meeting").toBoolean()
        inMeeting = prop.getProperty("zoomMeeting.in_meeting").toBoolean()
        joinBeforeHost = prop.getProperty("zoomMeeting.join_before_host").toBoolean()
        muteUponEntry = prop.getProperty("zoomMeeting.mute_upon_entry").toBoolean()
        watermark = prop.getProperty("zoomMeeting.watermark").toBoolean()
        usePmi = prop.getProperty("zoomMeeting.use_pmi").toBoolean()
        approvalType = CreateMeetingRequest.MeetingSettings.ApprovalType.valueOf(prop.getProperty("zoomMeeting.approval_type"))
        audio = CreateMeetingRequest.MeetingSettings.Audio.valueOf(prop.getProperty("zoomMeeting.audio"))
        autoRecording = CreateMeetingRequest.MeetingSettings.AutoRecording.valueOf(prop.getProperty("zoomMeeting.auto_recording"))
    }
}