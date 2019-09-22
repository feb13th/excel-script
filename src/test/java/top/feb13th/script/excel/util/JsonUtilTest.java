package top.feb13th.script.excel.util;

import java.util.List;
import org.junit.Test;
import top.feb13th.script.excel.domain.Database;
import top.feb13th.script.excel.domain.Sql;

public class JsonUtilTest {

  @Test
  public void toJson() {
    Database database = new Database();
    database.setUrl("mysql://***");
    database.setUsername("root");
    System.out.println(JsonUtil.toJson(database));
  }

  @Test
  public void fromJsonObject() {
    String json = IoUtil
        .readString(getClass().getClassLoader().getResource("conf/db.json").getPath());
    Database database = JsonUtil.fromJsonObject(json, Database.class);
    assert database != null;
  }

  @Test
  public void fromJsonArray() {
    String json = IoUtil
        .readString(getClass().getClassLoader().getResource("conf/sql.json").getPath());
    List<Sql> list = JsonUtil.fromJsonArray(json, Sql.class);
    assert list != null;
  }
}