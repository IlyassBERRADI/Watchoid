package com.example.watchoid

import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels

class HTTPClient {
    companion object{
        fun readStream(inputStream: InputStream): ByteBuffer {
            var byteBuffer = ByteBuffer.allocateDirect(1024)
            val channel = Channels.newChannel(inputStream)

            while (channel.read(byteBuffer) > 0) {
                // Ensure there is space in the buffer
                if (byteBuffer.remaining() == 0) {
                    val newBuffer = ByteBuffer.allocateDirect(byteBuffer.capacity() * 2)
                    byteBuffer.flip()
                    newBuffer.put(byteBuffer)
                    byteBuffer = newBuffer
                }
            }
            byteBuffer.flip()
            return byteBuffer
        }
        fun getRequest() : String{
            val url = URL("http://www.android.com/")
            val urlConnection = url.openConnection() as HttpURLConnection
            //val data = urlConnection.inputStream.bufferedReader().readText()
            val data: String
            try {
                /*BufferedInputStream(urlConnection.inputStream).use { inputStream ->
                    val data = readStream(inputStream)
                    // Do something with the data
                    println("Read ${data.limit()} bytes.")
                }*/
                urlConnection.inputStream.bufferedReader().use { reader ->
                    data = reader.readText()
                }
                return data
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}