package top.feb13th.script.excel.util;

import org.junit.Test;

public class IoUtilTest {

  @Test
  public void readString() {
    String path = getClass().getClassLoader().getResource("conf/db.json").getPath();
    String string = IoUtil.readString(path);
    System.out.println(string);
  }
}