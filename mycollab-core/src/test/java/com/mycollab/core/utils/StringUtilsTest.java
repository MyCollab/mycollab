package com.mycollab.core.utils;

import org.junit.Assert;
import org.junit.rules.ExpectedException;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.diffblue.deeptestutils.CompareWithFieldList;
import com.diffblue.deeptestutils.FieldList;
import com.diffblue.deeptestutils.IterAnswer;
import com.diffblue.deeptestutils.Reflector;

public class StringUtilsTest {

  @org.junit.Rule
  public ExpectedException thrown = ExpectedException.none();

  /* testedClasses: com/mycollab/core/utils/StringUtils.kt */
  /*
   * Test generated by Diffblue Deeptest.
   * This test case covers:
   * conditional line 66 branch to line 70
   * conditional line 70 branch to line 73
   * conditional line 73 branch to line 73
   */

  @org.junit.Test
  public void com_mycollab_core_utils_StringUtils_trim_001_d85fae3c6ce0d621() throws Throwable {

    String retval;
    {
      /* Arrange */
      String param_2 = "!!!!!";
      String input = param_2;
      int length = 0;
      boolean withEllipsis = true;

      /* Act */
      retval = com.mycollab.core.utils.StringUtils.trim(input, length, withEllipsis);
    }
    {
      /* Assert result */
      Assert.assertNotNull(retval);
      Assert.assertEquals("...", retval);
    }
  }

  /* testedClasses: com/mycollab/core/utils/StringUtils.kt */
  /*
   * Test generated by Diffblue Deeptest.
   * This test case covers:
   * conditional line 66 branch to line 70
   * conditional line 70 branch to line 71
   */

  @org.junit.Test
  public void com_mycollab_core_utils_StringUtils_trim_002_89818a2278f5eefc() throws Throwable {

    String retval;
    {
      /* Arrange */
      String param_2 = "!!!!";
      String input = param_2;
      int length = 4;
      boolean withEllipsis = true;

      /* Act */
      retval = com.mycollab.core.utils.StringUtils.trim(input, length, withEllipsis);
    }
    {
      /* Assert result */
      Assert.assertNotNull(retval);
      Assert.assertEquals("!!!!", retval);
    }
  }

  /* testedClasses: com/mycollab/core/utils/StringUtils.kt */
  /*
   * Test generated by Diffblue Deeptest.
   * This test case covers:
   * conditional line 66 branch to line 70
   * conditional line 70 branch to line 73
   * conditional line 73 branch to line 73
   */

  @org.junit.Test
  public void com_mycollab_core_utils_StringUtils_trim_003_b33f1ab961599f() throws Throwable {

    String retval;
    {
      /* Arrange */
      String param_2 = "!!!!!";
      String input = param_2;
      int length = 4;
      boolean withEllipsis = false;

      /* Act */
      retval = com.mycollab.core.utils.StringUtils.trim(input, length, withEllipsis);
    }
    {
      /* Assert result */
      Assert.assertNotNull(retval);
      Assert.assertEquals("!!!!", retval);
    }
  }
}
