package com.example.watchoid


import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import java.io.IOException
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.channels.UnresolvedAddressException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class TCPClient() {
    companion object {
        private val UTF8_CHARSET: Charset = Charset.forName("UTF-8")
        private const val BUFFER_SIZE = 1024


        @Throws(IOException::class, UnresolvedAddressException::class)
        fun getResponse(sentBuffer : ByteBuffer, server : SocketAddress, closeInput : Boolean, outEncoding : String, sizeBuffer:Int?/*, sizeBeforeResponse : Boolean*/) : String {
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
                when(outEncoding){
                    "Double" -> bufferResponse = ByteBuffer.allocate(Double.SIZE_BYTES)
                    "Int" -> bufferResponse = ByteBuffer.allocate(Int.SIZE_BYTES)
                    "Long" -> bufferResponse = ByteBuffer.allocate(Long.SIZE_BYTES)
                    else -> {


                        if (sizeBuffer==null){
                            bufferResponse = ByteBuffer.allocate(Int.SIZE_BYTES)
                            while (socketChannel.read(bufferResponse)!=-1){
                                if (!bufferResponse.hasRemaining()){
                                    break
                                }
                            }
                            var size = bufferResponse.getInt()
                            bufferResponse = ByteBuffer.allocate(size)
                        }
                        else {
                            bufferResponse = ByteBuffer.allocate(sizeBuffer)
                        }

                    }
                }
                var result : String

                while (socketChannel.read(bufferResponse) != -1) {
                    if (!bufferResponse.hasRemaining()) {
                        break
                    }
                }
                bufferResponse.flip()

                when(outEncoding){
                    "Double" -> result = bufferResponse.getDouble().toString()
                    "Int" -> result = bufferResponse.getInt().toString()
                    "Long" -> result = bufferResponse.getLong().toString()
                    else -> {

                        result = Charset.forName(outEncoding).decode(bufferResponse).toString()

                    }
                }
                return result
            } finally {
                socketChannel?.close()
            }
        }




        private fun selectAllFrom(tableName: String) = SimpleSQLiteQuery("SELECT * FROM $tableName")

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