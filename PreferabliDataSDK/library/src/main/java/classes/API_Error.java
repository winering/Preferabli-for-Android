//
//  API_Error.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/7/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

/**
 * Represents an error returned from our API.
 */
class API_Error {
    private int code;
    private String message;

    protected API_Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    protected int getCode() {
        return code;
    }

    protected String getMessage() {
        return message;
    }
}