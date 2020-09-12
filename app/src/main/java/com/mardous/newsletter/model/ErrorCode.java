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

package com.mardous.newsletter.model;

import androidx.annotation.StringRes;
import com.mardous.newsletter.R;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public enum ErrorCode {

    apiKeyDisabled(R.string.error_apiKeyDisabled),
    apiKeyExhausted(R.string.error_apiKeyExhausted),
    parameterInvalid(R.string.error_parameterInvalid),
    parametersMissing(R.string.error_parametersMissing),
    rateLimited(R.string.error_rateLimited),
    unexpectedError(R.string.error_unexpectedError);

    @StringRes
    public int messageRes;

    ErrorCode(@StringRes int messageRes) {
        this.messageRes = messageRes;
    }
}
