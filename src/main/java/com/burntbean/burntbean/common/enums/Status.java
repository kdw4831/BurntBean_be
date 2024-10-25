package com.burntbean.burntbean.common.enums;

import com.nimbusds.openid.connect.sdk.claims.Gender;

public enum Status {
    online,
    offline;




    public static Status fromString(String status) {
        if ("ONLINE".equals(status)) {
            return online;
        } else if ("inactive".equals(status)) {
            return offline;
        } else {
            throw new RuntimeException("Invalid status value: " + status);
        }
    }
}
