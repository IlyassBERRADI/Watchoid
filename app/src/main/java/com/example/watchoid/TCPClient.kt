package com.example.watchoid


import java.net.SocketAddress
import java.nio.channels.SocketChannel
import java.nio.charset.Charset

class TCPClient() {
    companion object {
        val UTF8_CHARSET = Charset.forName("UTF-8")
        val BUFFER_SIZE = 1024

        fun getResponse(request : String, server : SocketAddress, bufferSize : Int) : String{
            var socketChannel: SocketChannel = SocketChannel.open()
            socketChannel.connect(server)
            var requestBuffer = UTF8_CHARSET.encode(request)
            socketChannel.write(requestBuffer)
            socketChannel.shutdownOutput()
        }
    }



}