package top.feb13th.script.excel.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据库配置
 *
 * @author feb13th
 * @date 2019/9/21 14:14
 */
@Getter
@Setter
@NoArgsConstructor
public class Database {

  // 数据库类型
  private String type;
  private String url;
  private String ip;
  private String port;
  private String database;
  private String username;
  private String password;
  // 日期格式化
  private String dateFormat;
  private String filepath;

}
