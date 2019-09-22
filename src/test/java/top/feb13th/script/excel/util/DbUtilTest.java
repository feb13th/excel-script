package top.feb13th.script.excel.util;

import java.util.List;
import org.junit.Test;

public class DbUtilTest {

  @Test
  public void getQueryColumns() {
    String sql = "SELECT id, name as db_name FROM table where id = 10 and name in (Select id from tables where id = 1)";
    List<String> columns = DbUtil.getQueryColumns(sql);
    columns.forEach(System.out::println);
  }
}