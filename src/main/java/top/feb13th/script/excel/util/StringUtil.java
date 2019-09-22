package top.feb13th.script.excel.util;

/**
 * 字符串工具类
 *
 * @author feb13th
 * @date 2019/9/21 17:34
 */
public class StringUtil {

  public static boolean isBlank(String str) {
    return null == str || "".equals(str.trim());
  }

  public static boolean nonBlank(String str) {
    return !isBlank(str);
  }
}
