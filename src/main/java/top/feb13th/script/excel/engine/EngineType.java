package top.feb13th.script.excel.engine;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import top.feb13th.script.excel.util.StringUtil;

/**
 * 引擎类型
 *
 * @author feb13th
 * @date 2019/9/21 19:18
 */
@Getter
public enum EngineType {
  MYSQL(MySQLEngine.class),
  SQLSERVER(SqlServerEngine.class);

  private Class<? extends Engine> engine;

  EngineType(Class<? extends Engine> engine) {
    this.engine = engine;
  }

  private static final Map<String, EngineType> staticMap = new HashMap<>();

  static {
    for (EngineType engineType : values()) {
      staticMap.put(engineType.name().toLowerCase(), engineType);
    }
  }

  public static EngineType get(String type) {
    if (StringUtil.isBlank(type)) {
      return null;
    }
    return staticMap.get(type.toLowerCase());
  }
}
