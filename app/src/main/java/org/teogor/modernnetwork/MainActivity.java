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

package org.teogor.modernnetwork;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zeoflow.app.Activity;
import com.zeoflow.memo.Memo;
import com.zeoflow.utils.ContentCompat;

import org.teogor.modernnetwork.databinding.ActivityMainBinding;
import org.teogor.modernnetwork.tcp.TCP;
import org.teogor.modernnetwork.tcp.TCPClient;
import org.teogor.modernnetwork.types.BuilderTypes;
import org.teogor.modernnetwork.udp.UDP;
import org.teogor.modernnetwork.udp.UDPClient;
import org.teogor.modernnetwork.user.UserBean;

import java.util.Objects;

public class MainActivity extends Activity
{

    private ActivityMainBinding mainBinding;
    private BuilderTypes currentBuilder = BuilderTypes.UDP;

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

        // check if the user logged in previously
        if (!Memo.get("loggedIn", false))
        {
            finish();
            startActivity(LoginActivity.class);
            return;
        }
        // check if the user stored is null
        if (Memo.get("userData", null) == null)
        {
            finish();
            Memo.put("loggedIn", false);
            startActivity(LoginActivity.class);
            return;
        }

        // Set the layout for the main activity
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        // get user data from memory
        UserBean user = Memo.get("userData");
        if (user.username == null)
        {
            user.username = "empty";
        }

        mainBinding.mtvUsernameTip.setText(user.username.toUpperCase());
        mainBinding.mtvUsernameTip.setOnClickListener(v -> Toast.makeText(
                zContext,
                "You joined as: \"" + user.username + "\".",
                Toast.LENGTH_SHORT
        ).show());

        setBottomBar();
        setBuilderListener();

    }

    private void setBottomBar()
    {
        mainBinding.mtvUdp.setTextColor(ColorStateList.valueOf(ContentCompat.getColor(R.color.text_lvl1)));
        mainBinding.mtvTcp.setTextColor(ColorStateList.valueOf(ContentCompat.getColor(R.color.text_lvl2)));
        mainBinding.titleToolbar.setText("UDP Builder");
        mainBinding.mtvUdp.setOnClickListener(v ->
        {
            if (currentBuilder == BuilderTypes.UDP)
            {
                return;
            }
            currentBuilder = BuilderTypes.UDP;
            mainBinding.titleToolbar.setText("UDP Builder");
            mainBinding.mtvUdp.setTextColor(ContentCompat.getColor(R.color.text_lvl1));
            mainBinding.mtvTcp.setTextColor(ContentCompat.getColor(R.color.text_lvl2));
            resetBuilder();
        });
        mainBinding.mtvTcp.setOnClickListener(v ->
        {
            if (currentBuilder == BuilderTypes.TCP)
            {
                return;
            }
            currentBuilder = BuilderTypes.TCP;
            mainBinding.titleToolbar.setText("TCP Builder");
            mainBinding.mtvUdp.setTextColor(ContentCompat.getColor(R.color.text_lvl2));
            mainBinding.mtvTcp.setTextColor(ContentCompat.getColor(R.color.text_lvl1));
            resetBuilder();
        });
    }

    private void setBuilderListener()
    {
        mainBinding.mblJoinServer.setOnClickListener(v ->
        {
            mainBinding.mblJoinServer.setLoading(true);
            if(isValidBuilder())
            {
                String port = Objects.requireNonNull(mainBinding.tietPort.getText()).toString();
                String host = Objects.requireNonNull(mainBinding.tietHost.getText()).toString();
                switch (currentBuilder)
                {
                    case UDP:
                        UDPClient udpClient = UDP.Builder()
                                .port(Integer.parseInt(port))
                                .host(host)
                                .joinServer();
                        break;
                    case TCP:
                        TCPClient tcpClient = TCP.Builder()
                                .port(Integer.parseInt(port))
                                .host(host)
                                .joinServer();
                        break;
                }
            }
            mainBinding.mblJoinServer.setLoading(false);
        });
        mainBinding.mblCreateServer.setOnClickListener(v ->
        {
            mainBinding.mblCreateServer.setLoading(true);
            if(isValidBuilder())
            {
                String port = Objects.requireNonNull(mainBinding.tietPort.getText()).toString();
                String host = Objects.requireNonNull(mainBinding.tietHost.getText()).toString();
                switch (currentBuilder)
                {
                    case UDP:
                        UDP.Builder()
                                .port(Integer.parseInt(port))
                                .host(host)
                                .createServer();
                        break;
                    case TCP:
                        TCP.Builder()
                                .port(Integer.parseInt(port))
                                .host(host)
                                .createServer();
                        break;
                }
            }
            mainBinding.mblCreateServer.setLoading(false);
        });
    }

    private boolean isValidBuilder()
    {
        if (mainBinding.tietPort.getText() == null || mainBinding.tietHost.getText() == null)
        {
            if(mainBinding.tietHost.getText() == null)
            {
                mainBinding.tilHost.setErrorEnabled(true);
                mainBinding.tilHost.setError("Empty host");
            }
            if(mainBinding.tietPort.getText() == null)
            {
                mainBinding.tilPort.setErrorEnabled(true);
                mainBinding.tilPort.setError("Empty port");
            }
            return false;
        }
        String port = mainBinding.tietPort.getText().toString();
        String host = mainBinding.tietHost.getText().toString();
        if (port.isEmpty() && host.isEmpty())
        {
            mainBinding.tilPort.setErrorEnabled(true);
            mainBinding.tilPort.setError("Empty port");

            mainBinding.tilHost.setErrorEnabled(true);
            mainBinding.tilHost.setError("Empty host");
            return false;
        } else if (port.isEmpty())
        {
            mainBinding.tilPort.setErrorEnabled(true);
            mainBinding.tilPort.setError("Empty port");

            mainBinding.tilHost.setErrorEnabled(false);
            return false;
        } else if (host.isEmpty())
        {
            mainBinding.tilHost.setErrorEnabled(true);
            mainBinding.tilHost.setError("Empty host");

            mainBinding.tilPort.setErrorEnabled(false);
            return false;
        } else
        {
            mainBinding.tilPort.setErrorEnabled(false);
            mainBinding.tilHost.setErrorEnabled(false);
            return true;
        }
    }

    private void resetBuilder()
    {
        mainBinding.tietHost.setText(null);
        mainBinding.tilHost.setErrorEnabled(false);
        mainBinding.tietPort.setText(null);
        mainBinding.tilPort.setErrorEnabled(false);
    }

}