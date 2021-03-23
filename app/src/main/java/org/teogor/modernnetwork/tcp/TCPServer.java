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

package org.teogor.modernnetwork.tcp;

import android.os.StrictMode;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.AsyncServerSocket;
import com.koushikdutta.async.AsyncSocket;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.callback.ListenCallback;
import com.zeoflow.app.Entity;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TCPServer extends Entity
{

    private final InetAddress host;
    private final int port;

    public TCPServer(String host, int port)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
        try
        {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }

        this.port = port;

        setup();
    }

    private void setup()
    {
        AsyncServer.getDefault().listen(host, port, new ListenCallback()
        {

            @Override
            public void onAccepted(final AsyncSocket socket)
            {
                handleAccept(socket);
            }

            @Override
            public void onListening(AsyncServerSocket socket)
            {
                System.out.println("[TCPServer] TCPServer started listening for connections");
            }

            @Override
            public void onCompleted(Exception ex)
            {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[TCPServer] Successfully shutdown server");
            }
        });
    }

    private void handleAccept(final AsyncSocket socket)
    {
        System.out.println("[TCPServer] New Connection " + socket.toString());

        socket.setDataCallback((emitter, bb) ->
        {
            System.out.println("[TCPServer] Received Message " + new String(bb.getAllByteArray()));

            Util.writeAll(socket, "Hello TCPClient".getBytes(), ex ->
            {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[TCPServer] Successfully wrote message");
            });
        });

        socket.setClosedCallback(ex ->
        {
            if (ex != null) throw new RuntimeException(ex);
            System.out.println("[TCPServer] Successfully closed connection");
        });

        socket.setEndCallback(ex ->
        {
            if (ex != null) throw new RuntimeException(ex);
            System.out.println("[TCPServer] Successfully end connection");
        });
    }

}
