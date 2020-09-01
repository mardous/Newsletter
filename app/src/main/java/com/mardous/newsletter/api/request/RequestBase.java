package com.mardous.newsletter.api.request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Christians Martínez Alvarado
 */
public interface RequestBase {

    @NonNull
    String getStatus();

    void setStatus(String status);

    @Nullable
    String getCode();

    void setCode(String code);
}
