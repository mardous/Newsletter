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

package com.mardous.newsletter.api.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mardous.newsletter.api.request.RequestBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class Sources implements RequestBase {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("sources")
    @Expose
    private List<Source> sources;

    @NonNull
    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Nullable
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @NonNull
    public List<Source> getSources() {
        if (sources == null) {
            return new ArrayList<>();
        }
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
