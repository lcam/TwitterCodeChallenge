package com.twitter.challenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    @Expose
    private Integer _3h;

    public Integer get_3h() {
        return _3h;
    }

    public void set_3h(Integer _3h) {
        this._3h = _3h;
    }
}
