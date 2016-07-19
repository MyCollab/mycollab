/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.common;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class UrlTokenizer {
    private String internalVal;
    private String remainStrVal;
    private String query;

    public UrlTokenizer(String url) {
        internalVal = (url.startsWith("/")) ? url.substring(1) : url;
        int queryIndex = internalVal.indexOf("?");
        if (queryIndex != -1) {
            query = internalVal.substring(queryIndex + 1);
            internalVal = internalVal.substring(0, queryIndex);
        }
        internalVal = UrlEncodeDecoder.decode(internalVal);
        remainStrVal = internalVal;
    }

    public Integer getInt() throws InvalidTokenException {
        if (hasMoreTokens()) {
            try {
                return Integer.parseInt(getNextToken());
            } catch (NumberFormatException e) {
                throw new InvalidTokenException("Invalid token " + internalVal);
            }
        } else {
            throw new InvalidTokenException("Invalid token " + internalVal);
        }
    }

    public String getString() throws InvalidTokenException {
        if (hasMoreTokens()) {
            return getNextToken();
        } else {
            throw new InvalidTokenException("Invalid token " + internalVal);
        }
    }

    public boolean hasMoreTokens() {
        return !remainStrVal.equals("");
    }

    private String getNextToken() {
        int index = remainStrVal.indexOf("/");
        if (index < 0) {
            String result = remainStrVal + "";
            remainStrVal = "";
            return result;
        } else {
            String result = remainStrVal.substring(0, index);
            remainStrVal = remainStrVal.substring(index + 1);
            return result;
        }
    }

    public String getRemainValue() {
        return remainStrVal;
    }

    public String getQuery() {
        return query;
    }

    public static void main(String[] args) {
        System.out.println(UrlEncodeDecoder.encode("hainguyen@esofthead.com"));
    }
}
