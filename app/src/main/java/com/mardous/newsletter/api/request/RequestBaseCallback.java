/*
 * Copyright (c) 2020  Christians Martínez Alvarado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mardous.newsletter.api.request;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mardous.newsletter.model.ErrorCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is my custom implementation of {@link retrofit2.Callback}.
 *
 * You don't need to override both onResponse and onFailure methods,
 * instead you just need to override the onLoadingFinished method and
 * check the {@link Code} object that is sent with he call which contains
 * information about your request and its final state. If you receive an
 * {@link Code#ERROR} result, you can consult the {@link RequestBaseCallback#errorCode}
 * variable for more details about the error.
 *
 * See {@link RequestBase}, {@link Call}, {@link Callback} and {@link Response}.
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class RequestBaseCallback<M extends RequestBase> implements Callback<M> {

    /**
     * Local variable that contains important information in case the result
     * of your Request is of type {@link Code#ERROR}. Generally you should
     * not use it with requests that were successful since in this case its
     * value will be always null.
     * 
     * See {@link Code#ERROR}, and {@link #isSuccessful(Response)}.
     */
    @Nullable
    protected ErrorCode errorCode;

    @CallSuper
    @Override
    public void onResponse(Call<M> call, Response<M> response) {
        callOnFinished(response, response == null || call.isCanceled() ? Code.NONE : isSuccessful(response) ? Code.SUCCESS : Code.ERROR);
    }

    @CallSuper
    @Override
    public void onFailure(@NonNull Call<M> call, Throwable t) {
        callOnFinished(null, call.isCanceled() ? Code.NONE : Code.NETWORK_FAILURE);
    }

    private boolean isSuccessful(@NonNull Response<M> response) {
        return response.isSuccessful() && isNotNullBody(response.body()) && !isErrorBody(response.body());
    }

    private boolean isNotNullBody(@Nullable M body) {
        return body != null;
    }

    private boolean isErrorBody(@NonNull M body) {
        return body.getStatus().equals("error");
    }

    private void callOnFinished(Response<M> response, Code code) {
        M body = response != null ? response.body() : null;
        if (isNotNullBody(body) && isErrorBody(body)) {
            try {
                errorCode = ErrorCode.valueOf(body.getCode());
            } catch (Exception ignored) {}
        }
        onFinished(body, code);
    }

    protected abstract void onFinished(@Nullable M result, Code code);

    public enum Code {
        /**
         * Received when your Request was successful.
         *
         * See {@link #isSuccessful(Response)}.
         */
        SUCCESS,

        /**
         * Received when your Request was not successful. The
         * variable {@link RequestBaseCallback#errorCode} is
         * usable when this result is received;
         *
         * See {@link #isSuccessful(Response)} and {@link RequestBaseCallback#errorCode}.
         */
        ERROR,

        /**
         * Received when there is a network failure.
         *
         * See {@link Callback#onFailure(Call, Throwable)}.
         */
        NETWORK_FAILURE,

        /**
         * Received when you call {@link Call#cancel()} before
         * your Request is complete.
         *
         * See {@link Call#cancel()}, {@link Call#isCanceled()}.
         */
        NONE
    }
}
