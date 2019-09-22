package top.feb13th.script.excel.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据源工具类
 *
 * @author feb13th
 * @date 2019/9/21 20:12
 */
public class DbUtil {

  /**
   * 执行查询操作<br/> connection在此方法中不会关闭
   *
   * @param connection 数据库连接
   * @param sql sql语句
   * @return 查询到的结果
   */
  public static List<Object[]> executeQuery(Connection connection, String sql) {
    List<Object[]> list = new ArrayList<>();
    Statement statement = null;
    ResultSet resultSet = null;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(sql);
      // 设置获取到的列名
      List<String> queryColumns = getQueryColumns(sql);
      ResultSetMetaData metaData = resultSet.getMetaData();
      if (queryColumns.size() == 0
          || queryColumns.size() != metaData.getColumnCount()
          || queryColumns.size() == 1 && "*".equals(queryColumns.get(0))) {
        queryColumns.clear();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          String columnName = metaData.getColumnName(i);
          queryColumns.add(columnName);
        }
      }
      int size = queryColumns.size();
      while (resultSet.next()) {
        Object[] objs = new Object[size];
        for (int i = 0; i < size; i++) {
          objs[i] = resultSet.getObject(i + 1);
        }
        list.add(objs);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      close(resultSet);
      close(statement);
    }
    return list;
  }

  /**
   * 关闭流
   */
  public static void close(AutoCloseable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // 对sql进行缓存
  private static final ConcurrentMap<String, List<String>> queryColumns = new ConcurrentHashMap<>();

  /**
   * 获取sql中所有的查询参数
   */
  public static List<String> getQueryColumns(String sql) {
    List<String> list;
    String lowerCaseSql = sql.toLowerCase();
    if (ObjectUtil.nonNull(list = queryColumns.get(lowerCaseSql))) {
      return list;
    }
    synchronized (DbUtil.class) {
      list = new ArrayList<>();
      Pattern pattern = Pattern.compile("select ([\\w\\s,*]+?) from");
      Matcher matcher = pattern.matcher(lowerCaseSql);
      if (matcher.find()) {
        String queryArgs = matcher.group(1);
        String[] args = queryArgs.split(",");
        for (String arg : args) {
          // 判断是否存在 as 字段, 存在则取as后面的字段作为真实字段
          if (arg.contains("as")) {
            list.add(arg.split("as")[1].trim());
          } else {
            list.add(arg.trim());
          }
        }
      }
      queryColumns.put(lowerCaseSql, list);
    }
    return queryColumns.get(lowerCaseSql);
  }
}
