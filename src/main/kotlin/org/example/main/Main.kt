package org.example.main

import org.example.mailApi.IcsMeetingManager
import org.example.mailApi.MailboxManager
import org.example.settings.SettingsManager
import org.example.zoomApi.MeetingsApi
import org.example.zoomApi.infrastructure.ClientException
import org.example.zoomApi.infrastructure.ServerException
import org.example.zoomApi.models.CreateMeetingRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.*

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger: Logger = LoggerFactory.getLogger(Main::class.java)
            logger.info("Program started")

            val propFile = File(javaClass.classLoader.getResource("config.properties").file)

            val settingsManager = SettingsManager(propFile)
            settingsManager.loadProp()

            val zoomLogin = settingsManager.zoomLogin
            val zoomPassword = settingsManager.zoomPassword
            val zoomToken = settingsManager.zoomToken

            val mailboxManager = MailboxManager(settingsManager)

            val icsMeetingManager = IcsMeetingManager(mailboxManager)
            logger.info("Starting meetings download")
            val meetings = icsMeetingManager.getMeetingsFromMailbox()
            logger.info("Meeting download complete")

            for(meeting in meetings){
                val apiInstance = MeetingsApi()
                val meetingSettings = CreateMeetingRequest.MeetingSettings(
                    host_video = settingsManager.hostVideo,
                    participant_video = settingsManager.participantVideo,
                    cn_meeting = settingsManager.cnMeeting,
                    in_meeting = settingsManager.inMeeting,
                    join_before_host = settingsManager.joinBeforeHost,
                    mute_upon_entry = settingsManager.muteUponEntry,
                    watermark = settingsManager.watermark,
                    use_pmi = settingsManager.usePmi,
                    approval_type = settingsManager.approvalType,
                    audio = settingsManager.audio,
                    auto_recording = settingsManager.autoRecording,
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
                    mailboxManager.sendMessage(meeting!!)
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