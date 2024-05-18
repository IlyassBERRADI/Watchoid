package com.example.watchoid


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.charset.Charset

class TCPClient() {
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
                var bufferInt = ByteBuffer.allocate(Int.SIZE_BYTES)
                bufferInt.putInt(requestBuffer.remaining())
                bufferInt.flip()
                socketChannel.write(bufferInt)
                var bytesWritten = socketChannel.write(requestBuffer)
                bufferInt.clear()
                if (!readFully(socketChannel, bufferInt)){
                    Log.i("Server","Server stopped connection")
                    return "";
                }
                bufferInt.flip()
                var size = bufferInt.getInt()
                var readBuffer = ByteBuffer.allocate(size)

                if (!readFully(socketChannel, readBuffer)){
                    Log.i("Error2","Client stopped connection")
                    return "";
                }
                readBuffer.flip()
                var charBuffer = UTF8_CHARSET.decode(readBuffer)
                return charBuffer.toString()
            } finally {
                socketChannel?.close()
            }
        }

        @Throws(IOException::class)
        private fun readFully(socketChannel : SocketChannel, buffer : ByteBuffer) : Boolean{
            while (socketChannel.read(buffer)!=-1){
                if (!buffer.hasRemaining()){
                    return true
                }
            }
            return  false
        }
    }



}