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
        fun getResponse(sentBuffer : ByteBuffer, server : SocketAddress, closeInput : Boolean, typeResponse : String) : String {
            var socketChannel: SocketChannel? = null
            try {
                socketChannel = SocketChannel.open()
                var bool = socketChannel.connect(server)
                Log.i("Server","Connected to server "+socketChannel.socket().remoteSocketAddress)
                /*var requestBuffer = UTF8_CHARSET.encode(request)
                var bufferInt = ByteBuffer.allocate(Int.SIZE_BYTES)
                bufferInt.putInt(requestBuffer.remaining())
                bufferInt.flip()
                socketChannel.write(bufferInt)*/
                sentBuffer.flip()
                //Log.i("sentBufferstring", UTF8_CHARSET.decode(sentBuffer).toString())
                //Log.i("sentBuffer", sentBuffer.remaining().toString())
                var bytesWritten = socketChannel.write(sentBuffer)
                //bufferInt.clear()
                if (closeInput){
                    Log.i("yessir", "yessir")
                    socketChannel.socket().shutdownOutput()
                }
                var bufferResponse = ByteBuffer.allocate(BUFFER_SIZE)
                var result : String

                while (socketChannel.read(bufferResponse)!=-1){
                    if (!bufferResponse.hasRemaining()){
                        break
                    }
                    /*Log.i("Server","Server stopped connection")
                    return "";*/
                }
                bufferResponse.flip()
                Log.i("bufferResponse", bufferResponse.remaining().toString())
                /*var size = bufferInt.getInt()
                var readBuffer = ByteBuffer.allocate(size)

                if (!readFully(socketChannel, readBuffer)){
                    Log.i("Error2","Client stopped connection")
                    return "";
                }
                readBuffer.flip()*/
                when(typeResponse){
                    "Double" -> result = bufferResponse.getDouble().toString()
                    "Int" -> result = bufferResponse.getInt().toString()
                    "Long" -> result = bufferResponse.getLong().toString()
                    else -> {
                        result = Charset.forName(typeResponse).decode(bufferResponse).toString()
                    }
                }
                return result
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