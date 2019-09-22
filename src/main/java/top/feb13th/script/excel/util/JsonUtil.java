package top.feb13th.script.excel.util;

import com.alibaba.fastjson.JSON;
import java.util.List;

/**
 * 字符串转json对象工具类
 *
 * @author feb13th
 * @date 2019/9/21 14:43
 */
public class JsonUtil {

  /**
   * 将对象转换为json字符串
   *
   * @param obj 对象
   * @return json字符串
   */
  public static String toJson(Object obj) {
    return JSON.toJSONString(obj);
  }

  /**
   * 将json字符串转换为json对象
   *
   * @param json 字符串
   * @param clazz 对象类型
   * @param <T> 对象类型
   * @return 对象
   */
  public static <T> T fromJsonObject(String json, Class<T> clazz) {
    return JSON.parseObject(json, clazz);
  }

  /**
   * 将json字符串转换为json数组
   *
   * @param json 字符串
   * @param clazz 数组泛型
   * @param <T> 数组元素类型
   * @return 数组
   */
  public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
    return JSON.parseArray(json, clazz);
  }

}
