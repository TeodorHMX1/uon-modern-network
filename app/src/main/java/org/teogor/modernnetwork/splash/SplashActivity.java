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

package org.teogor.modernnetwork.splash;

import android.os.Bundle;

import com.zeoflow.app.Activity;
import com.zeoflow.memo.ConcealEncryption;
import com.zeoflow.memo.Memo;

import org.teogor.modernnetwork.LoginActivity;
import org.teogor.modernnetwork.MainActivity;
import org.teogor.modernnetwork.R;

public class SplashActivity extends Activity
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

        /*
         * TODO move to the Application class when it will
         *  when it will be created
         */
        // initialize memo library
        Memo.init().setEncryption(new ConcealEncryption("d363d3tdJ"))
                .build();

        // check if the user logged in previously
        if (Memo.get("loggedIn", false))
        {
            finish();
            startActivity(MainActivity.class);
        } else {
            finish();
            startActivity(LoginActivity.class);
        }
    }

}
