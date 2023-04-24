//
//  API_PreferabliException.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/7/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

import android.util.Log;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import okhttp3.ResponseBody;

/**
 * Some kind of error has occurred.
 * @see API_PreferabliException#getMessage() getMessage() for more information on what happened.
 */
public class API_PreferabliException extends Exception {

    private PreferabliExceptionType type;
    private int code;
    private String message;

    public API_PreferabliException(ResponseBody responseBody) {
        try {
            API_Error apiError = Tools_Preferabli.convertJsonToObject(responseBody.string(), API_Error.class);
            this.type = PreferabliExceptionType.APIError;
            this.message = apiError.getMessage();
            this.code = apiError.getCode();
        } catch (JsonSyntaxException | IOException | NullPointerException e) {
            this.type = PreferabliExceptionType.APIError;
            this.message = "Unknown issue. Contact support.";
            this.code = 0;
        }
    }

    public API_PreferabliException(PreferabliExceptionType type) {
        this(type, null, 0);
    }

    public API_PreferabliException(PreferabliExceptionType type, String message) {
        this.type = type;
        this.message = message;
    }

    public API_PreferabliException(PreferabliExceptionType type, String message, int code) {
        this.type = type;
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    /**
     * Get a detailed description of what went wrong.
     * @return a string description.
     */
    @Override
    public String getMessage() {
        String mesageToReturn = message;
        if (Tools_Preferabli.isNullOrWhitespace(message)) {
            mesageToReturn = type.getMessage();
        }

        if (code != 0) {
            mesageToReturn = code + " " + mesageToReturn;
        }

        return mesageToReturn;
    }

    @Override
    public void printStackTrace() {
        Log.e(getClass().getSimpleName(), getMessage());
    }

    /**
     * Type of error that occurred.
     * @see API_PreferabliException the container class.
     */
    public enum PreferabliExceptionType {
        /**
         * An error from the API.
         */
        APIError,
        /**
         * A network error.
         */
        NetworkError,
        /**
         * An unknown / other error.
         */
        OtherError,
        /**
         * An error decoding JSON.
         */
        JSONError,
        /**
         * The data you requested is already loaded.
         */
        AlreadyLoaded,
        /** An error in the data that came back from the API */
        BadData,
        /**
         * User / customer not logged in.
         */
        InvalidAccessToken,
        /**
         * SDK not initialized properly.
         */
        InvalidClientInterface,
        /**
         * SDK not initialized properly.
         */
        InvalidIntegrationId,
        /**
         * A database error.
         */
        DatabaseError,
        /**
         * A mapping error.
         */
        MappingNotFound;

        /**
         * Get a general description of this type of exception.
         * @return a string description of the type.
         */
        public String getMessage() {
            switch(this) {
                case APIError:
                return "API error.";
                case NetworkError:
                return "Network issue.";
                case OtherError:
                return "Other / unknown issue. Contact support.";
                case JSONError:
                return "JSON error. Contact support.";
                case AlreadyLoaded:
                return "Already loaded this.";
                case BadData:
                return "API returned bad data. Contact support.";
                case InvalidAccessToken:
                return "You need to login a customer / user first.";
                case InvalidClientInterface:
                return "Invalid CLIENT_INTERFACE used to initialize the SDK.";
                case InvalidIntegrationId:
                return "Invalid INTEGRATION_ID used to initialize the SDK.";
                case DatabaseError:
                return "Database error. Try clearing the SDK database cache.";
                case MappingNotFound:
                return "Could not match your supplied ids to a Preferabli product. Are you sure this product is mapped?";
            }

            return "Other / unknown issue. Contact support.";
        }
    }
}
