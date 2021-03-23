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

public class UDPServer
{

    private final InetSocketAddress host;

    public UDPServer(String host, int port)
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

        AsyncDatagramSocket asyncDatagramSocket;
        try
        {
            asyncDatagramSocket = AsyncServer.getDefault().openDatagram(host, true);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        asyncDatagramSocket.setDataCallback((emitter, bb) -> System.out.println("[UDPServer] Received Message " + new String(bb.getAllByteArray())));

        asyncDatagramSocket.setClosedCallback(ex ->
        {
            if (ex != null) throw new RuntimeException(ex);
            System.out.println("[UDPServer] Successfully closed connection");
        });

        asyncDatagramSocket.setEndCallback(ex ->
        {
            if (ex != null) throw new RuntimeException(ex);
            System.out.println("[UDPServer] Successfully end connection");
        });
    }

}
