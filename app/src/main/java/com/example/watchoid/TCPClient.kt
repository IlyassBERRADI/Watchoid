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
        fun getResponse(sentBuffer : ByteBuffer, server : SocketAddress, closeInput : Boolean, typeResponse : String, sizeBuffer:Int?) : String {
            var socketChannel: SocketChannel? = null
            try {
                socketChannel = SocketChannel.open()
                var bool = socketChannel.connect(server)
                Log.i("Server","Connected to server "+socketChannel.socket().remoteSocketAddress)
                sentBuffer.flip()
                var bytesWritten = socketChannel.write(sentBuffer)
                if (closeInput){
                    socketChannel.socket().shutdownOutput()
                }

                var bufferResponse : ByteBuffer
                when(typeResponse){
                    "Double" -> bufferResponse = ByteBuffer.allocate(Double.SIZE_BYTES)
                    "Int" -> bufferResponse = ByteBuffer.allocate(Int.SIZE_BYTES)
                    "Long" -> bufferResponse = ByteBuffer.allocate(Long.SIZE_BYTES)
                    else -> {
                        bufferResponse = ByteBuffer.allocate(Int.SIZE_BYTES)
                    }
                }
                var result : String

                while (socketChannel.read(bufferResponse)!=-1){
                    if (!bufferResponse.hasRemaining()){
                        break
                    }
                }
                bufferResponse.flip()

                when(typeResponse){
                    "Double" -> result = bufferResponse.getDouble().toString()
                    "Int" -> result = bufferResponse.getInt().toString()
                    "Long" -> result = bufferResponse.getLong().toString()
                    else -> {

                        if (sizeBuffer==null){
                            var size = bufferResponse.getInt()
                            bufferResponse = ByteBuffer.allocate(size)
                        }
                        else
                            bufferResponse = ByteBuffer.allocate(sizeBuffer)
                        while (socketChannel.read(bufferResponse)!=-1){
                            if (!bufferResponse.hasRemaining()){
                                break
                            }
                        }
                        bufferResponse.flip()
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