package com.example.catbreed.network;

import static com.example.catbreed.network.Status.ERROR;
import static com.example.catbreed.network.Status.SUCCESS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NetworkResponse {

        public final Status status;

        @Nullable
        public final Object data;

        @Nullable
        public final Throwable error;

        private NetworkResponse(Status status, @Nullable Object data, @Nullable Throwable error) {
            this.status = status;
            this.data = data;
            this.error = error;
        }

        public static NetworkResponse loading() {
            return new NetworkResponse(Status.LOADING, null, null);
        }

        public static NetworkResponse success(@NonNull Object data) {
            return new NetworkResponse(SUCCESS, data, null);
        }

        public static NetworkResponse error(@NonNull Throwable error) {
            return new NetworkResponse(ERROR, null, error);
        }
}
