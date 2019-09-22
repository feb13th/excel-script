package top.feb13th.script.excel.engine;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.feb13th.script.excel.domain.Database;
import top.feb13th.script.excel.util.ObjectUtil;
import top.feb13th.script.excel.util.StringUtil;

/**
 * 数据库引擎帮助类
 *
 * @author feb13th
 * @date 2019/9/21 19:16
 */
public class EngineHelper {

  // 用于缓存数据源
  private static final Map<String, Engine> engineMap = new ConcurrentHashMap<>();
  private static final Map<Database, DatabaseInstance> instanceMap = new ConcurrentHashMap<>();

  /**
   * 获取数据库连接
   *
   * @param database 数据源配置信息
   */
  public static Connection getConnection(Database database) {
    Engine engine = null;
    DatabaseInstance instance = null;
    if (ObjectUtil.nonNull(instance = instanceMap.get(database))
        && ObjectUtil.nonNull(engine = engineMap.get(instance.getUrl()))) {
      return engine
          .getConnection(instance.getUrl(), instance.getUsername(), instance.getPassword());
    }
    synchronized (EngineHelper.class) {
      EngineType engineType = EngineType.get(database.getType());
      if (ObjectUtil.isNull(engineType)) {
        throw new IllegalArgumentException(
            "the database type:[" + database.getType() + "] not support");
      }
      Class<? extends Engine> engineClass = engineType.getEngine();
      try {
        Constructor<? extends Engine> constructor = engineClass.getDeclaredConstructor();
        engine = constructor.newInstance();
        String newUrl = engine
            .init(database.getUrl(), database.getIp(), database.getPort(), database.getDatabase());
        if (StringUtil.isBlank(newUrl)) {
          throw new IllegalStateException("engine implement return url must not null");
        }
        engineMap.put(newUrl, engine);
        instance = new DatabaseInstance();
        instance.setUrl(newUrl);
        instance.setUsername(database.getUsername());
        instance.setPassword(database.getPassword());
        instanceMap.put(database, instance);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    if (ObjectUtil.isNull(engine)) {
      throw new IllegalStateException(
          "the database type:[" + database.getType() + "] not support");
    }

    return engine
        .getConnection(instance.getUrl(), instance.getUsername(), instance.getPassword());
  }

  @Getter
  @Setter
  @NoArgsConstructor
  private static class DatabaseInstance {

    private String url;
    private String username;
    private String password;
  }
}
