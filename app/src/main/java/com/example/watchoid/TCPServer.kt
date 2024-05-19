package com.example.watchoid

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.charset.Charset

class TCPServer {
    private val serverSocketChannel : ServerSocketChannel = ServerSocketChannel.open()

    constructor(port : Int?){
        val serverSocket = serverSocketChannel.socket()
        if (port!=null){
            serverSocket.bind(InetSocketAddress("127.0.0.1", port))
            Log.i("server", this.javaClass.name + " starts on port "+port)
        }
    }

    fun launch(){
        Log.i("server", "Server started")
        while (true){
            var client = serverSocketChannel.accept()
            try {
                Log.i("Connection", "Connection accepted from " + client.socket().remoteSocketAddress)
                serve(client)
            } catch (e: IOException){
                Log.i("Connection", "Connection terminated with client by IOException")
            } finally {
                client?.close()
            }
        }

    }

    fun serve(sc : SocketChannel){
        var bufferInt = ByteBuffer.allocate(Int.SIZE_BYTES)
        if (!readFully(sc, bufferInt)){
            Log.i("Connection", "Connection lost")
            return
        }
        bufferInt.flip()
        var size = bufferInt.getInt()
        var bufferStr = ByteBuffer.allocate(size)
        if (!readFully(sc, bufferStr)){
            Log.i("Connection", "Connection lost")
            return
        }
        bufferStr.flip()
        var strUpper = UTF8_CHARSET.decode(bufferStr).toString().uppercase(java.util.Locale.ROOT)
        bufferStr = UTF8_CHARSET.encode(strUpper)
        bufferInt.clear()
        bufferInt.putInt(bufferStr.remaining())
        bufferInt.flip()
        sc.write(bufferInt)
        var wrote = sc.write(bufferStr);
        Log.i("Connection", "Connection closed by client")
    }
    companion object {
        val UTF8_CHARSET = Charset.forName("UTF-8")
        val BUFFER_SIZE = 1024

        fun readFully(socketChannel : SocketChannel, buffer : ByteBuffer) : Boolean{
            while (socketChannel.read(buffer)!=-1){
                if (!buffer.hasRemaining()){
                    return true
                }
            }
            return  false
        }

    }


}