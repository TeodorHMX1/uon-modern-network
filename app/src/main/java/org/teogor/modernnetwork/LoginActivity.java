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

import org.teogor.modernnetwork.databinding.ActivityLoginBinding;
import org.teogor.modernnetwork.user.UserBean;

import java.util.Objects;

public class LoginActivity extends Activity
{

    private ActivityLoginBinding loginBinding;

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
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);

        MaterialButtonLoading mblJoin = loginBinding.mblJoin;

        mblJoin.setOnClickListener(v ->
        {
            mblJoin.setLoading(true);
            if (isValidUsername())
            {
                UserBean user = UserBean.create();
                user.username = Objects.requireNonNull(loginBinding.tietUsername.getText()).toString();
                user.password = Objects.requireNonNull(loginBinding.tietPassword.getText()).toString();
                configureNewActivity(MainActivity.class)
                        .withParam("user_bean", user)
                        .start();
            }
            mblJoin.setLoading(false);
        });
    }

    private boolean isValidUsername()
    {
        if (loginBinding.tietUsername.getText() == null || loginBinding.tietPassword.getText() == null)
        {
            if (loginBinding.tietUsername.getText() == null)
            {
                loginBinding.tilUsername.setErrorEnabled(true);
                loginBinding.tilUsername.setError("Empty username");
            }
            if (loginBinding.tietPassword.getText() == null)
            {
                loginBinding.tilPassword.setErrorEnabled(true);
                loginBinding.tilPassword.setError("Empty password");
            }
            return false;
        }
        String username = loginBinding.tietUsername.getText().toString();
        String password = loginBinding.tietPassword.getText().toString();
        if (username.isEmpty() && password.isEmpty())
        {
            loginBinding.tilUsername.setErrorEnabled(true);
            loginBinding.tilUsername.setError("Empty username");

            loginBinding.tilPassword.setErrorEnabled(true);
            loginBinding.tilPassword.setError("Empty password");
            return false;
        } else if (username.isEmpty())
        {
            loginBinding.tilUsername.setErrorEnabled(true);
            loginBinding.tilUsername.setError("Empty username");

            loginBinding.tilPassword.setErrorEnabled(false);
            return false;
        } else if (password.isEmpty())
        {
            loginBinding.tilPassword.setErrorEnabled(true);
            loginBinding.tilPassword.setError("Empty password");

            loginBinding.tilUsername.setErrorEnabled(false);
            return false;
        } else
        {
            loginBinding.tilUsername.setErrorEnabled(false);
            loginBinding.tilPassword.setErrorEnabled(false);
            return true;
        }
    }

}