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

import android.os.Bundle;
import android.view.View;

import com.zeoflow.app.Activity;
import com.zeoflow.app.StatusBarUtil;
import com.zeoflow.material.elements.button.MaterialButtonLoading;
import com.zeoflow.material.elements.textfield.TextInputEditText;
import com.zeoflow.material.elements.textfield.TextInputLayout;

import org.teogor.modernnetwork.databinding.ActivityLoginBinding;
import org.teogor.modernnetwork.user.UserBean;

public class LoginActivity extends Activity
{

    private TextInputLayout tilUsername;
    private TextInputEditText tietUsername;

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
        StatusBarUtil.setTranslucent(this);
        ActivityLoginBinding loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);

        tilUsername = loginBinding.tilUsername;
        tietUsername = loginBinding.tietUsername;
        MaterialButtonLoading mblJoin = loginBinding.mblJoin;

        mblJoin.setOnClickListener(v ->
        {
            mblJoin.setLoading(true);
            if(isValidUsername())
            {
                UserBean user = UserBean.create();
                //noinspection ConstantConditions
                user.username = tietUsername.getText().toString();
                configureNewActivity(MainActivity.class)
                        .withParam("user_bean", user)
                        .start();
            }
            mblJoin.setLoading(false);
        });
    }

    private boolean isValidUsername()
    {
        boolean valid = false;
        if (tietUsername == null || tietUsername.getText() == null)
        {
            tilUsername.setErrorEnabled(true);
            tilUsername.setError("Empty username");
            return false;
        }
        String username = tietUsername.getText().toString();
        if (username.isEmpty())
        {
            tilUsername.setErrorEnabled(true);
            tilUsername.setError("Empty username");
        } else if (username.length() < 3 || username.length() > 30) {
            tilUsername.setErrorEnabled(true);
            tilUsername.setError("Username must be between 3 and 30 character");
        } else {
            valid = true;
            tilUsername.setErrorEnabled(false);
        }
        return valid;
    }

}