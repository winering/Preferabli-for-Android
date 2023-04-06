//
//  API_ResultHandler.java
//  Preferabli
//
//  Created by Nicholas Bortolussi on 7/7/16.
//  Copyright © 2023 RingIT, Inc. All rights reserved.
//

package classes;

/**
 * Pass an instance with all of our methods to receive results. {@link API_ResultHandler#onSuccess(Object)} will be executed in the event of a successful call. {@link API_ResultHandler#onFailure(API_PreferabliException)} will be executed in the event of a failed call.
 * @param <T> changes depending on the return type of the method.
 */
public interface API_ResultHandler<T> {
    /**
     * Executed in the event of a successful call.
     * @param data the data returned by the method.
     */
    void onSuccess(T data);

    /**
     * Executed in the event of a failed call.
     * @param e the error that caused the failure.
     */
    void onFailure(API_PreferabliException e);
}
