package com.example.springpushnotification.service

import com.example.springpushnotification.model.NotificationJPA
import com.example.springpushnotification.repository.NotificationRepository
import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


@Service
class NotificationService(
    private val notificationRepository: NotificationRepository
) {

    fun listAll(): MutableList<NotificationJPA> {
        return notificationRepository.findAll()
    }

    fun create(userId: String): NotificationJPA {
        val notification = NotificationJPA(
            username = "User: $userId",
            idOneSignal = userId
        )
        return notificationRepository.save(notification)
    }

    fun sendMessageToAllUsers(message: String) {
        val connection = createConnection()
        val jsonBody = generateJsonBody(message = message)

        enableStreamingAndCreateByteArray(connection, jsonBody).also {
            sendRequest(connection, it)
        }
    }

    fun sendMessageToUser(userId: String, message: String) {
        val connection = createConnection()
        val jsonBody = generateJsonBody(userId = userId, message = message)

        enableStreamingAndCreateByteArray(connection, jsonBody).also {
            sendRequest(connection, it)
        }
    }

    fun createConnection(): HttpURLConnection {
        val url = URL(ONE_SIGNAL_URL)
        val connection = url.openConnection() as HttpURLConnection
        configureConnection(connection)
        return connection
    }

    fun configureConnection(connection: HttpURLConnection) {
        connection.also {
            it.useCaches = false
            it.doOutput = true
            it.doInput = true
            it.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            it.setRequestProperty("Authorization", "Basic $REST_API_KEY")
            it.requestMethod = "POST"
        }
    }

    fun generateJsonBody(userId: String? = null, message: String): String {
        return """
            {
                "app_id": "$APP_ID",
                "included_segments": ${userId ?: "\"Subscribed Users\""},
                "data": {
                    "foo": "bar"  
                },
                "contents": {
                    "en": "$message"
                }
            }
            """
    }

    fun enableStreamingAndCreateByteArray(connection: HttpURLConnection, jsonBody: String): ByteArray {
        val sendBytes = jsonBody.toByteArray(Charsets.UTF_8)
        connection.setFixedLengthStreamingMode(sendBytes.size)
        return sendBytes
    }

    fun sendRequest(connection: HttpURLConnection, sendBytes: ByteArray): String {
        val outputStream = connection.outputStream
        outputStream.write(sendBytes)
        return createResponseRequest(connection, connection.responseCode)
    }

    fun createResponseRequest(connection: HttpURLConnection, httpResponse: Int): String {
        return if (httpResponse >= HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            createJsonResponse(connection.inputStream)
        } else {
            createJsonResponse(connection.errorStream)
        }
    }

    fun createJsonResponse(inputStream: InputStream): String {
        val scanner = Scanner(inputStream, "UTF-8")
        val jsonResponse = if (scanner.useDelimiter("\\A").hasNext()) scanner.next() else ""
        scanner.close()
        return jsonResponse
    }

    companion object {
        const val REST_API_KEY = ""
        const val APP_ID = ""
        const val ONE_SIGNAL_URL = "https://onesignal.com/api/v1/notifications"
    }

}