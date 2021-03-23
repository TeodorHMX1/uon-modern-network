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
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zeoflow.app.Activity;
import com.zeoflow.material.elements.textview.MaterialTextView;
import com.zeoflow.utils.ContentCompat;

import org.teogor.modernnetwork.databinding.ActivityMainBinding;
import org.teogor.modernnetwork.tcp.TCP;
import org.teogor.modernnetwork.tcp.TCPClient;
import org.teogor.modernnetwork.udp.UDP;
import org.teogor.modernnetwork.udp.UDPClient;
import org.teogor.modernnetwork.user.UserBean;

public class MainActivity extends Activity
{

    private ActivityMainBinding mainBinding;

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
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        if (intent == null)
        {
            finish();
            return;
        }
        UserBean user = intent.getParcelableExtra("user_bean");
        if (user.username == null)
        {
            user.username = "empty";
        }

        MaterialTextView mtvUsernameTip = mainBinding.mtvUsernameTip;
        mtvUsernameTip.setText(user.username.toUpperCase());
        mtvUsernameTip.setOnClickListener(v -> Toast.makeText(
                zContext,
                "Your username is: \"" + user.username + "\".",
                Toast.LENGTH_SHORT
        ).show());

        setBottomBar();

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

    private void setBottomBar()
    {
        mainBinding.mtvUdp.setTextColor(ColorStateList.valueOf(ContentCompat.getColor(R.color.text_lvl1)));
        mainBinding.mtvTcp.setTextColor(ColorStateList.valueOf(ContentCompat.getColor(R.color.text_lvl2)));
        mainBinding.titleToolbar.setText("UDP Builder");
        mainBinding.mtvUdp.setOnClickListener(v ->
        {
            mainBinding.titleToolbar.setText("UDP Builder");
            mainBinding.mtvUdp.setTextColor(ContentCompat.getColor(R.color.text_lvl1));
            mainBinding.mtvTcp.setTextColor(ContentCompat.getColor(R.color.text_lvl2));
            resetBuilder();
        });
        mainBinding.mtvTcp.setOnClickListener(v ->
        {
            mainBinding.titleToolbar.setText("TCP Builder");
            mainBinding.mtvUdp.setTextColor(ContentCompat.getColor(R.color.text_lvl2));
            mainBinding.mtvTcp.setTextColor(ContentCompat.getColor(R.color.text_lvl1));
            resetBuilder();
        });
    }

    public void resetBuilder()
    {
        mainBinding.tietHost.setText(null);
        mainBinding.tietPort.setText(null);
    }

}