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

package org.teogor.modernnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.teogor.modernnetwork.tcp.TCPClient;
import org.teogor.modernnetwork.tcp.TCPServer;
import org.teogor.modernnetwork.udp.UDPClient;
import org.teogor.modernnetwork.udp.UDPServer;
import org.teogor.modernnetwork.udp.UDP;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UDP.Builder()
                .port(7000)
                .host("localhost")
                .createServer();

        UDPClient client = UDP.Builder()
                .port(7000)
                .host("localhost")
                .joinServer();

        client.send("Hello, World!");

//        Thread thread = new Thread(() ->
//        {
//            try
//            {
//                //TCP client and server (TCPClient will automatically send welcome message after setup and server will respond)
//                new TCPServer("localhost", 7000);
//                new TCPClient("localhost", 7000);
//
//                //UDP client and server (Here the client explicitly sends a message)
//                new UDPServer("localhost", 7001);
//                new UDPClient("localhost", 7001).send("Hello World");
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        });

//        thread.start();

    }

}