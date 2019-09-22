package top.feb13th.script.excel.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 输入输出工具类
 *
 * @author feb13th
 * @date 2019/9/21 14:21
 */
public class IoUtil {

  /**
   * 从文件路径中读取字符串
   *
   * @param path 文件路径
   * @return 字符串
   */
  public static String readString(String path) {
    // new File(path) 用于解决windows环境下:错误
    Path filepath = Paths.get(new File(path).toURI());
    if (!Files.exists(filepath)) {
      throw new IllegalArgumentException("file not exists");
    }
    try {
      return Files.lines(filepath, Charset.forName("UTF-8")).reduce("", String::concat);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
