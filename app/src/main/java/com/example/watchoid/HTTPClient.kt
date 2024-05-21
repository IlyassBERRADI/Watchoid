package com.example.watchoid

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import android.util.Log
import com.jayway.jsonpath.JsonPath
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.text.FieldPosition

class HTTPClient {
    companion object{

        @Throws(IOException::class)
        fun readStream(inputStream: InputStream): ByteBuffer {
            var byteBuffer = ByteBuffer.allocateDirect(1024)
            val channel = Channels.newChannel(inputStream)

            while (channel.read(byteBuffer) != -1) {
                // Ensure there is space in the buffer
                if (!byteBuffer.hasRemaining()) {
                    val newBuffer = ByteBuffer.allocateDirect(byteBuffer.capacity() * 2)
                    byteBuffer.flip()
                    newBuffer.put(byteBuffer)
                    byteBuffer = newBuffer
                }
            }
            byteBuffer.flip()
            return byteBuffer
        }

        @Throws(IOException::class)
        fun requestGET(url : String) : String{
            var url = URL(url)
            var urlConnection = url.openConnection() as HttpURLConnection
            var responseCode = urlConnection.responseCode
            //val data = urlConnection.inputStream.bufferedReader().readText()
            val data: String
            try {

                /*BufferedInputStream(urlConnection.inputStream).use { inputStream ->
                    val data = readStream(inputStream)
                    // Do something with the data
                    println("Read ${data.limit()} bytes.")
                }*/
                /*var response : StringBuilder = java.lang.StringBuilder()
                val headers = urlConnection.headerFields
                headers.forEach { (key, value) ->
                    println("$key: $value")
                }*/
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    urlConnection.inputStream.bufferedReader().use { reader ->
                        data = reader.readText()
                    }
                    return data
                }
                else{
                    Log.i("HTTP Response", "The response is not OK")
                    return ""
                }
            } finally {
                urlConnection.disconnect()
            }
        }


        fun findInText(pattern:String, input:String) : Boolean{
            return Regex(pattern = pattern).matches(input)
        }

        fun findInHtml(tag:String, position: Int, content: String): Boolean{
            val document: Document = Jsoup.parse(content)
            val elements : Elements = document.getElementsByTag(tag)
            return elements[position].text()==content
        }

        fun findInJSON(jsonString: String, path: String, value: String, type: String) : Boolean{
            val inputJsonPath = JsonPath.parse(jsonString)
            var result : String? = null
            when(type){
                "Int" -> result = (inputJsonPath.read(path) as Int).toString()
                "Long"  -> result = (inputJsonPath.read(path) as Long).toString()
                "Double" -> result = (inputJsonPath.read(path) as Double).toString()
                "Boolean" -> result = (inputJsonPath.read(path) as Boolean).toString()
                "String" -> result = inputJsonPath.read(path) as String
            }
            return result == value
        }
    }
}