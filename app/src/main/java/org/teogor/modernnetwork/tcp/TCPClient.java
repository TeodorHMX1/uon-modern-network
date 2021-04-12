/*
 * Copyright (c) 2021 TeodorHMX1 (Teodor G.)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.teogor.modernnetwork.tcp;

import android.os.StrictMode;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.Util;

import java.net.InetSocketAddress;

public class TCPClient
{

    private final String host;
    private final int port;
    private AsyncSocket socket;

    public TCPClient(String host, int port)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        this.host = host;
        this.port = port;
        setup();
    }

    private void setup()
    {
        AsyncServer.getDefault().connectSocket(new InetSocketAddress(host, port), this::handleConnectCompleted);
    }

    private void handleConnectCompleted(Exception ex, final AsyncSocket socket)
    {
        if (ex != null) throw new RuntimeException(ex);
        this.socket = socket;

        socket.setDataCallback((emitter, bb) -> System.out.println("[TCPClient] Received Message " + new String(bb.getAllByteArray())));

        socket.setClosedCallback(ex12 ->
        {
            if (ex12 != null) throw new RuntimeException(ex12);
            System.out.println("[TCPClient] Successfully closed connection");
        });

        socket.setEndCallback(ex13 ->
        {
            if (ex13 != null) throw new RuntimeException(ex13);
            System.out.println("[TCPClient] Successfully end connection");
        });
    }

    public void sendMessage(String text)
    {
        if(socket == null)
        {
            return;
        }
        Util.writeAll(socket, text.getBytes(), ex1 ->
        {
            if (ex1 != null) throw new RuntimeException(ex1);
            System.out.println("[TCPClient] Successfully wrote message");
        });
    }

}
