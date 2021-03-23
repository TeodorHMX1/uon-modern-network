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

import android.content.Intent;
import android.os.Bundle;

import com.zeoflow.app.Activity;

import org.teogor.modernnetwork.tcp.TCP;
import org.teogor.modernnetwork.tcp.TCPClient;
import org.teogor.modernnetwork.udp.UDPClient;
import org.teogor.modernnetwork.udp.UDP;
import org.teogor.modernnetwork.user.UserBean;

public class MainActivity extends Activity
{

    @Override
    protected void onStart()
    {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(0, R.anim.fade_out);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent == null)
        {
            finish();
            return;
        }
        UserBean user = intent.getParcelableExtra("user_bean");

        // UDP Builder
        // UDP Server
        UDP.Builder()
                .port(7000)
                .host("localhost")
                .createServer();

        //UDP Client
        UDPClient udpClient = UDP.Builder()
                .port(7000)
                .host("localhost")
                .joinServer();
        // send message as a client to the udp server
        udpClient.send("Hello, World! by UDP Client");

        // TCP Builder
        // TCP Server
        TCP.Builder()
                .port(7001)
                .host("localhost")
                .createServer();

        //TCP Client
        TCPClient tcpClient = TCP.Builder()
                .port(7001)
                .host("localhost")
                .joinServer();
        // send message as a client to the tcp server
        tcpClient.sendMessage("Hello, World! by TCP Client");

    }

}