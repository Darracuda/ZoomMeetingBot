package org.example.mailApi

import com.sun.mail.util.BASE64DecoderStream
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Part
import javax.mail.internet.MimeBodyPart

class AttachmentManager(private val message: Message) {
    fun getAttachments(): MutableList<Attachment>{
        val attachments = mutableListOf<Attachment>()
        val contentType: String = message.contentType
        if (contentType.startsWith("multipart", ignoreCase = true)) {
            val multiPart = message.content as Multipart
            for (i in 0 until multiPart.count) {
                val part = multiPart.getBodyPart(i) as MimeBodyPart
                if (part.disposition.equals(Part.ATTACHMENT, ignoreCase = true)) {
                    val fileName= part.fileName
                    val contentStream = part.content as BASE64DecoderStream
                    val content = contentStream.readAllBytes()
                    val attachment = Attachment(fileName, content)
                    attachments.add(attachment)
                }
            }
        }
        return attachments
    }
}