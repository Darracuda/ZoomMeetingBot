package org.example.mailApi

import com.sun.mail.util.BASE64DecoderStream
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Part
import javax.mail.internet.MimeBodyPart

class AttachmentManager(private val message: Message) {

    fun getAttachments(): List<Attachment>{
        val contentType: String = message.contentType
        if(!contentType.startsWith("multipart", ignoreCase = true)){
            return emptyList()
        }

        val multiPart = message.content as Multipart
        return (0 until multiPart.count)
            .map{ i -> multiPart.getBodyPart(i) as MimeBodyPart}
            .filter { part -> part.disposition.equals(Part.ATTACHMENT, ignoreCase = true) }
            .map{ part ->
                val fileName = part.fileName
                val contentStream = part.content as BASE64DecoderStream
                val content = contentStream.readAllBytes()
                Attachment(fileName, content)
            }
    }
}