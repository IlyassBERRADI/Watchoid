package com.example.watchoid

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
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HTTPClient {
    companion object{


        @Throws(IOException::class)
        fun requestGET(url : String) : String{
            var url = URL(url)
            var urlConnection = url.openConnection() as HttpURLConnection

            val data: String
            try {
                urlConnection.inputStream.bufferedReader().use { reader ->
                    data = reader.readText()
                }
                return data
            } finally {
                urlConnection.disconnect()
            }
        }


        fun findInText(pattern:String, input:String) : Boolean{
            return Regex(pattern = pattern).containsMatchIn(input)
        }

        fun findInHtml(tags:String, content: String, pattern: String): Boolean{
            val document: Document = Jsoup.parse(content)
            var elements = document.selectXpath(tags)
            return elements.eachText().contains(pattern)
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