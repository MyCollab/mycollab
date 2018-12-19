/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.validator.constraints;


import com.mycollab.core.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneNumberValidatorTest {

    @Test
    public void testPhoneNumber() {
        assertTrue(StringUtils.isValidPhoneNumber("0918734068"));
        assertFalse(StringUtils.isValidPhoneNumber("a"));
        assertFalse(StringUtils.isValidPhoneNumber("091a"));
        assertTrue(StringUtils.isValidPhoneNumber("1111111111"));
        assertFalse(StringUtils.isValidPhoneNumber("1111"));
        assertTrue(StringUtils.isValidPhoneNumber("(111)-111-1111"));
    }
}
