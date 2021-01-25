package org.example.main

import org.example.mailApi.EmailSender
import org.example.mailApi.MeetingManager
import org.example.zoomApi.MeetingsApi
import org.example.zoomApi.infrastructure.ClientException
import org.example.zoomApi.infrastructure.ServerException
import org.example.zoomApi.models.CreateMeetingRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.time.*
import java.util.*

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger: Logger = LoggerFactory.getLogger(Main::class.java)
            logger.info("Program started")

            val propFile = File("/Users/d.kolpakova/Documents/config.properties")
            val prop = Properties()
            FileInputStream(propFile).use { prop.load(it) }

            val imapHost = prop.getProperty("imapHost")
            val imapPort = prop.getProperty("imapPort")
            val imapLogin = prop.getProperty("imapLogin")
            val imapPassword = prop.getProperty("imapPassword")

            val zoomLogin = prop.getProperty("zoomLogin")
            val zoomPassword = prop.getProperty("zoomPassword")
            val zoomToken = prop.getProperty("zoomToken")

            val meetingManager = MeetingManager()
            logger.info("Starting meetings download")
            val meetings = meetingManager.getMeetingsFromMailbox(
                imapHost,
                imapPort,
                imapLogin,
                imapPassword
            )
            logger.info("Meeting download complete")

            for(meeting in meetings){
                val apiInstance = MeetingsApi()
                val meetingSettings = CreateMeetingRequest.MeetingSettings(
                    host_video = prop.getProperty("host_video").toBoolean(),
                    participant_video = prop.getProperty("participant_video").toBoolean(),
                    cn_meeting = prop.getProperty("cn_meeting").toBoolean(),
                    in_meeting =prop.getProperty("in_meeting").toBoolean(),
                    join_before_host = prop.getProperty("join_before_host").toBoolean(),
                    mute_upon_entry = prop.getProperty("mute_upon_entry").toBoolean(),
                    watermark = prop.getProperty("watermark").toBoolean(),
                    use_pmi = prop.getProperty("use_pmi").toBoolean(),
                    approval_type = CreateMeetingRequest.MeetingSettings.ApprovalType.valueOf(prop.getProperty("approval_type")),
                    audio = CreateMeetingRequest.MeetingSettings.Audio.valueOf(prop.getProperty("audio")),
                    auto_recording = CreateMeetingRequest.MeetingSettings.AutoRecording.valueOf(prop.getProperty("auto_recording")),
                )
                val request = CreateMeetingRequest(
                    topic = meeting?.subject,
                    agenda = meeting?.description,
                    type = CreateMeetingRequest.MeetingType.ScheduledMeeting,
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
                    //logger.info("Message sent")
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