package org.example.mailApi

import com.sun.mail.smtp.SMTPTransport
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSender{
    fun sendMessage(emailToAddresses: Array<String>){
        val host = "smtp.gmail.com"
        val port = "587"
        val username = ""
        val password = ""
        val emailFromAddress = ""
        val emailToCCAddresses = emptyArray<String>()

        val emailSubject = "Test Send Email via SMTP"
        val emailText = "Hello Java Mail \n ABC123"

        val session = getSession(host, port)
        val msg: Message = MimeMessage(session)

        try {
            msg.setFrom(InternetAddress(emailFromAddress))
            msg.setRecipients(Message.RecipientType.TO, emailToAddresses.map{ a -> InternetAddress(a) }.toTypedArray())
            msg.setRecipients(Message.RecipientType.CC, emailToCCAddresses.map{ a -> InternetAddress(a) }.toTypedArray())
            msg.subject = emailSubject
            msg.setText(emailText)
            msg.sentDate = Date()
            val t = session.getTransport("smtp") as SMTPTransport
            t.connect(host, username, password)
            t.sendMessage(msg, msg.allRecipients)
            println("Response: " + t.lastServerResponse)
            t.close()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun getSession(host: String?, port: String?): Session {
        val properties = Properties()
        properties["mail.smtp.host"] = host
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = port
        properties["mail.smtp.starttls.enable"] = "true"
        return Session.getInstance(properties)
    }
}