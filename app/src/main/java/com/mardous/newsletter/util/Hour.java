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

package com.mardous.newsletter.util;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public enum Hour {
    ONE(3600000),
    TWO(7200000),
    FIVE(18000000),
    EIGHT(28800000),
    TWELVE(43200000),
    TWENTY_FOUR(86400000);

    public long hourInMillis;

    Hour(long hourInMillis) {
        this.hourInMillis = hourInMillis;
    }
}
