package org.example.main

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
            logger.error("Error")

            val mailboxHost = "imap.gmail.com"
            val mailboxPort = "993"
            val mailboxLogin = ""
            val mailboxPassword = ""

            val zoomLogin = "diane.kolpakova@gmail.com"
            val zoomPassword = "123pas"
            val zoomToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6ImctQ0M4VUdzVFdDNHpmRi0wbkdobWciLCJleHAiOjE2MDgyMzMxNzMsImlhdCI6MTYwNzYyODM3NH0.ML7XBrj4lvdoXwgofQBKVSdzcsvyz7MGvXBJbdn5fSs"

            val meetingManager = MeetingManager()
            val meetings = meetingManager.getMeetingsFromMailbox(
                mailboxHost,
                mailboxPort,
                mailboxLogin,
                mailboxPassword
            )
            for(meeting in meetings){
                println(
                    meeting?.subject
                            + " \n     ***** \n"
                            + meeting?.description
                            + " \n     ***** \n"
                            + toLocal(meeting?.startTime)
                            + " \n     ***** \n"
                            + meeting?.duration
                            + " \n     ***** \n"
                            + meeting?.attendees
                            + " \n\n\n\n\n     #############################     \n\n\n\n\n"
                )

            }

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
                    println("start time = " + toLocal(response.start_time))
                    println(response)
                } catch (e: ClientException) {
                    println("4xx response calling MeetingsApi#meetingCreate")
                    e.printStackTrace()
                } catch (e: ServerException) {
                    println("5xx response calling MeetingsApi#meetingCreate")
                    e.printStackTrace()
                } catch (e: Exception) {
                    println("error")
                    e.printStackTrace()
                }
            }
        }

        private fun toLocal(input: Instant?): LocalDateTime? {
            return LocalDateTime.ofInstant(input, ZoneOffset.UTC)
        }
    }
}