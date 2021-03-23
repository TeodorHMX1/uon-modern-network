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

    public static UserBean create() {
        return new Parcelled_UserBean();
    }

}
