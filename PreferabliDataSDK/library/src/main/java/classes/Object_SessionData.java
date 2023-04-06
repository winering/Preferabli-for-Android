//
//  Object_SessionData.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/6/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

public class Object_SessionData extends Object_BaseObject {
    private long user_id;
    private long customer_id;
    private String token_access;
    private String token_refresh;
    private String expires_at;
    private String created_at;
    private String updated_at;
    private String intercom_hmac;

    public long getUserId() {
        return user_id;
    }

    public String getAccessToken() {
        return token_access;
    }

    public String getRefreshToken() {
        return token_refresh;
    }

    public String getExpiresAt() {
        return expires_at;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public String getIntercomHmac() {
        return intercom_hmac;
    }

    public long getCustomerId() {
        return customer_id;
    }
}
