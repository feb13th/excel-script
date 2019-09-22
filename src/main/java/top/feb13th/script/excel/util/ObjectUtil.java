package top.feb13th.script.excel.util;

/**
 * 对象工具类
 *
 * @author feb13th
 * @date 2019/9/21 19:25
 */
public class ObjectUtil {

  public static boolean isNull(Object obj) {
    return null == obj;
  }

  public static boolean nonNull(Object obj) {
    return !isNull(obj);
  }
}
