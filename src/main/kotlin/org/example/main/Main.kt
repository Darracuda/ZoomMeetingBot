package org.example.main

import org.example.mailApi.EmailSender
import org.example.mailApi.MeetingManager
import org.example.zoomApi.MeetingsApi
import org.example.zoomApi.infrastructure.ClientException
import org.example.zoomApi.infrastructure.ServerException
import org.example.zoomApi.models.CreateMeetingRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.*

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger: Logger = LoggerFactory.getLogger(Main::class.java)
            logger.info("Program started")

            val mailboxHost = "imap.gmail.com"
            val mailboxPort = "993"
            val mailboxLogin = ""
            val mailboxPassword = ""

            val zoomLogin = ""
            val zoomPassword = "123pas"
            val zoomToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6ImctQ0M4VUdzVFdDNHpmRi0wbkdobWciLCJleHAiOjE2MTIxMTQ0NjYsImlhdCI6MTYxMTUwOTY2N30.tMpE-9tKK9iWyPtUJnW48JhOibLRoBA1xAz1Hea97fs"

            val meetingManager = MeetingManager()
            logger.info("Starting meetings download")
            val meetings = meetingManager.getMeetingsFromMailbox(
                mailboxHost,
                mailboxPort,
                mailboxLogin,
                mailboxPassword
            )
            logger.info("Meeting download complete")

            for(meeting in meetings){
                val apiInstance = MeetingsApi()
                val meetingSettings = CreateMeetingRequest.MeetingSettings(
                    host_video = true,
                    participant_video = true,
                    cn_meeting = false,
                    in_meeting = false,
                    join_before_host = true,
                    mute_upon_entry = true,
                    watermark = false,
                    use_pmi = false,
                    approval_type = CreateMeetingRequest.MeetingSettings.ApprovalType.automaticallyApprove,
                    audio = CreateMeetingRequest.MeetingSettings.Audio.both,
                    auto_recording = CreateMeetingRequest.MeetingSettings.AutoRecording.local,
                )
                val request = CreateMeetingRequest(
                    topic = meeting?.subject,
                    agenda = meeting?.description,
                    type = CreateMeetingRequest.MeetingType.scheduledMeeting,
                    start_time = meeting?.startTime,
                    duration = meeting?.duration?.toMinutes(),
                    password = zoomPassword,
                    settings = meetingSettings,
                )
                try {
                    val response = apiInstance.createMeeting(zoomToken, zoomLogin, request)
                    logger.info("Meeting created")
                    logger.info("start time: ${toLocal(response.start_time)}")
                    logger.info("Response: $response")
                    val emailSender = EmailSender()
                    emailSender.sendMessage(meeting!!.attendees.toTypedArray())
                    logger.info("Message sent")
                } catch (e: ClientException) {
                    e.printStackTrace()
                    logger.error("4xx response calling MeetingsApi#meetingCreate")
                } catch (e: ServerException) {
                    e.printStackTrace()
                    logger.error("5xx response calling MeetingsApi#meetingCreate")

                } catch (e: Exception) {
                    logger.error("Exception: $e")
                    e.printStackTrace()
                }
            }
        }

        fun toLocal(input: Instant?): LocalDateTime? {
            return LocalDateTime.ofInstant(input, ZoneOffset.UTC)
        }
    }
}