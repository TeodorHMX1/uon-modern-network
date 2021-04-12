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
import com.zeoflow.memo.Memo;
import com.zeoflow.utils.StatusBarUtil;

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

        // check if the user logged in previously
        if (Memo.get("loggedIn", false))
        {
            finish();
            startActivity(MainActivity.class);
            return;
        }

        // set the status bar to be based on the root view from the layout
        StatusBarUtil.setTranslucent(this);

        // Set the layout for the login activity
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);

        // Set click listener for the join button
        loginBinding.mblJoin.setOnClickListener(v ->
        {
            // When clicked show the loading bar on the button
            loginBinding.mblJoin.setLoading(true);
            // validate the user's content such as the username and password
            if (isValidLogInInput())
            {
                finish();
                // create parcelable user bean
                UserBean user = UserBean.create();
                // set the username based on the input
                user.username = Objects.requireNonNull(loginBinding.tietUsername.getText()).toString();
                // set the password based on the input
                user.password = Objects.requireNonNull(loginBinding.tietPassword.getText()).toString();
                // store user details in memory
                Memo.put("userData", user);
                // set loggedIn to true
                Memo.put("loggedIn", true);
                // start the MainActivity with the user data as parameter
                // the user is of type Parcelable when passed through intent
                configureNewActivity(MainActivity.class)
                        .withParam("userData", user)
                        .start();
            }
            // hide the loading bar from the join button
            loginBinding.mblJoin.setLoading(false);
        });
    }

    /*
     * Check if the input is valid
     *
     * @return boolean validInput
     */
    private boolean isValidLogInInput()
    {
        // if the username or password is empty return false and
        // show the relevant message inside the input
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
        // get the username from input
        String username = loginBinding.tietUsername.getText().toString();
        // get the password from input
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