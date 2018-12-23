package com.cijianyouqing.traveler.eventbus;

import java.io.Serializable;

/**
 * Created by xiangpengfei on 2018/11/7.
 */
public class LoginEvent implements Serializable {
    private boolean loginin;

    public LoginEvent(boolean loginin) {
        this.loginin = loginin;
    }

    public boolean isLoginin() {
        return loginin;
    }

    public void setLoginin(boolean loginin) {
        this.loginin = loginin;
    }
}
