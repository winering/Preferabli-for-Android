//
//  Object_SessionData.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/6/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

/**
 * Internal class used to record sessions.
 */
class Object_SessionData extends Object_BaseObject {
    private long user_id;
    private long customer_id;
    private String token_access;
    private String token_refresh;
    private String expires_at;
    private String created_at;
    private String updated_at;
    private String intercom_hmac;

    long getUserId() {
        return user_id;
    }

    String getAccessToken() {
        return token_access;
    }

    String getRefreshToken() {
        return token_refresh;
    }

    String getExpiresAt() {
        return expires_at;
    }

    String getCreatedAt() {
        return created_at;
    }

    String getUpdatedAt() {
        return updated_at;
    }

    String getIntercomHmac() {
        return intercom_hmac;
    }

    long getCustomerId() {
        return customer_id;
    }
}
