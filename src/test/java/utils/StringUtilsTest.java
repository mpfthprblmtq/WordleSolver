package java.utils;

import utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

    @Test
    public void testNumberize() {
        int num1 = 111;
        int num2 = 2222;
        int num3 = 33333;
        int num4 = 444444;
        int num5 = 5555555;
        int num6 = 66666666;

        Assertions.assertEquals("111", StringUtils.numberize(num1));
        Assertions.assertEquals("2,222", StringUtils.numberize(num2));
        Assertions.assertEquals("33,333", StringUtils.numberize(num3));
        Assertions.assertEquals("444,444", StringUtils.numberize(num4));
        Assertions.assertEquals("5,555,555", StringUtils.numberize(num5));
        Assertions.assertEquals("66,666,666", StringUtils.numberize(num6));
    }

    @Test
    public void testIsNotEmpty() {
        String str1 = "";
        String str2 = "x";
        String str3 = null;

        Assertions.assertFalse(StringUtils.isNotEmpty(str1));
        Assertions.assertTrue(StringUtils.isNotEmpty(str2));
        Assertions.assertFalse(StringUtils.isNotEmpty(str3));
    }
}