package top.feb13th.script.excel.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 引擎接口
 *
 * @author feb13th
 * @date 2019/9/21 19:23
 */
public interface Engine {

  /**
   * 初始化
   *
   * @param url 连接字符串
   * @param host 主机名
   * @param port 端口
   * @param database 数据库名称
   * @return 连接的url
   */
  String init(String url, String host, String port, String database);

  /**
   * 获取数据库连接
   *
   * @param url 连接字符串
   * @param username 用户名
   * @param password 密码
   */
  default Connection getConnection(String url, String username, String password) {
    Connection connection = null;
    try {
      connection = DriverManager
          .getConnection(url, username, password);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return connection;
  }

}
