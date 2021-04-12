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

package org.teogor.modernnetwork.user;

import android.os.Parcelable;

import com.zeoflow.parcelled.Default;
import com.zeoflow.parcelled.Parcelled;

import javax.annotation.Nullable;

@Parcelled(version = 1)
public abstract class UserBean implements Parcelable, IParcelled_UserBean
{

    @Nullable
    @Default(code = "null")
    public String username;

    @Nullable
    @Default(code = "null")
    public String password;

    public static UserBean create()
    {
        return new Parcelled_UserBean();
    }

}
