package top.feb13th.script.excel.engine;

import top.feb13th.script.excel.util.StringUtil;

/**
 * sqlserver 引擎
 *
 * @author feb13th
 * @date 2019/9/21 21:41
 */
public class SqlServerEngine implements Engine {

  @Override
  public String init(String url, String host, String port, String database) {
    try {
      if (StringUtil.isBlank(url)) {
        if (StringUtil.isBlank(host) || StringUtil.isBlank(port) || StringUtil.isBlank(database)) {
          throw new IllegalArgumentException(
              "database config error, ip:[" + host + "] port:[" + port + "] database:[" + database
                  + "]");
        }
        url = String.format("jdbc:sqlserver://%s:%s;DataBaseName=%s", host,
            port, database);
      }
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      return url;
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
