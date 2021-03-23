/*
 * Copyright (c) 2021 Teodor G.
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

package org.teogor.modernnetwork.udp;

import android.os.StrictMode;

import com.koushikdutta.async.AsyncDatagramSocket;
import com.koushikdutta.async.AsyncServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class UDPClient
{

    private final InetSocketAddress host;
    private AsyncDatagramSocket asyncDatagramSocket;

    public UDPClient(String host, int port)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        this.host = new InetSocketAddress(host, port);
        setup();
    }

    private void setup()
    {
        try
        {
            asyncDatagramSocket = AsyncServer.getDefault().connectDatagram(host);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        asyncDatagramSocket.setClosedCallback(ex ->
        {
            if (ex != null) throw new RuntimeException(ex);
            System.out.println("[TCPClient] Successfully closed connection");
        });

        asyncDatagramSocket.setEndCallback(ex ->
        {
            if (ex != null) throw new RuntimeException(ex);
            System.out.println("[TCPClient] Successfully end connection");
        });
    }

    public void send(String msg)
    {
        asyncDatagramSocket.send(host, ByteBuffer.wrap(msg.getBytes()));
    }

}
