package com.incito.base.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * IDUtil单元测试
 * @author zhaigt
 *
 */
public class IDUtilTest {

  @Test
  public void testGetID() {
    String id = IDUtil.getID();
    Assert.assertNotNull(id);
  }

}
