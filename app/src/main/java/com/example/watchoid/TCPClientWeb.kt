package com.example.watchoid


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.charset.Charset

class TCPClientWeb() {
    companion object {
        private val UTF8_CHARSET: Charset = Charset.forName("UTF-8")
        private const val BUFFER_SIZE = 1024


        @Throws(IOException::class)
        fun getResponse(request : String, server : SocketAddress) : String {
            var socketChannel: SocketChannel? = null
            try {
                socketChannel = SocketChannel.open()
                var bool = socketChannel.connect(server)
                var requestBuffer = UTF8_CHARSET.encode(request)
                var bytesWritten = socketChannel.write(requestBuffer)
                socketChannel.socket().shutdownOutput()
                var readBuffer = ByteBuffer.allocate(BUFFER_SIZE)
                while (socketChannel.read(readBuffer)!=-1){
                    if (!readBuffer.hasRemaining()){
                        break
                    }
                }
                readBuffer.flip()
                var charBuffer = UTF8_CHARSET.decode(readBuffer)
                return charBuffer.toString()
            } finally {
                socketChannel?.close()
            }
        }

    }



}