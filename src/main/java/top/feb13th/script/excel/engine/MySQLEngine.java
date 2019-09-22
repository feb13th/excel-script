package top.feb13th.script.excel.engine;

import lombok.NoArgsConstructor;
import top.feb13th.script.excel.util.StringUtil;

/**
 * mysql引擎
 *
 * @author feb13th
 * @date 2019/9/21 19:40
 */
@NoArgsConstructor
public class MySQLEngine implements Engine {

  @Override
  public String init(String url, String host, String port, String database) {
    try {
      if (StringUtil.isBlank(url)) {
        if (StringUtil.isBlank(host) || StringUtil.isBlank(port) || StringUtil.isBlank(database)) {
          throw new IllegalArgumentException(
              "database config error, ip:[" + host + "] port:[" + port + "] database:[" + database
                  + "]");
        }
        url = String.format("jdbc:mysql://%s:%s/%s?characterEncoding=utf-8&useTimezone=true&serverTimezone=Asia/Shanghai", host,
            port, database);
      }
      Class.forName("com.mysql.cj.jdbc.Driver");
      return url;
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
