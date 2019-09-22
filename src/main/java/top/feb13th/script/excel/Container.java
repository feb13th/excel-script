package top.feb13th.script.excel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.feb13th.script.excel.domain.Database;
import top.feb13th.script.excel.domain.Sql;
import top.feb13th.script.excel.engine.EngineType;
import top.feb13th.script.excel.util.IoUtil;
import top.feb13th.script.excel.util.JsonUtil;
import top.feb13th.script.excel.util.ObjectUtil;
import top.feb13th.script.excel.util.StringUtil;

/**
 * 容器
 *
 * @author feb13th
 * @date 2019/9/21 17:26
 */
public class Container {

  private static final Logger logger = LoggerFactory.getLogger(Container.class);

  private static final ConcurrentMap<Class, Object> container = new ConcurrentHashMap<>();

  static {
    loadConfig();
  }

  private static void loadConfig() {
    String dbPath = System.getProperty(Consts.ENV_DATABASE);
    String sqlPath = System.getProperty(Consts.ENV_SQL);
    if (StringUtil.isBlank(dbPath) || StringUtil.isBlank(sqlPath)) {
      // 检查当前环境是否使用了jar环境
      String jarPath = Container.class.getProtectionDomain().getCodeSource().getLocation()
          .getPath();
      if (jarPath.endsWith(".jar")) {
        throw new IllegalArgumentException(
            "env.db or env.sql must not null, please set -Denv.db=[path to db.json] and -Denv.sql=[path to sql.json]");
      }
      String defaultConfigPath = Container.class.getClassLoader().getResource("conf/").getPath();
      logger.info("use default config file path:[{}]", defaultConfigPath);
      dbPath = defaultConfigPath + "db.json";
      sqlPath = defaultConfigPath + "sql.json";
    }

    logger.info("load resource from db.json:[{}], sql.json:[{}]", dbPath, sqlPath);
    String dbJson = null;
    String sqlJson = null;
    try {
      dbJson = IoUtil.readString(dbPath);
      sqlJson = IoUtil.readString(sqlPath);
    } catch (Exception e) {
      throw new RuntimeException("db.json or sql.json not exists, please check input path");
    }

    Database database = null;
    List<Sql> sqlList = new ArrayList<>();
    try {
      database = JsonUtil.fromJsonObject(dbJson, Database.class);
      sqlList = JsonUtil.fromJsonArray(sqlJson, Sql.class);
    } catch (Exception e) {
      throw new RuntimeException("db.json or sql.json config error, please check it");
    }

    EngineType engineType = EngineType.get(database.getType());
    if (ObjectUtil.isNull(engineType)) {
      throw new IllegalArgumentException(
          "the database type:[" + database.getType() + "] not support");
    }

    if (StringUtil.isBlank(database.getUsername())) {
      throw new IllegalArgumentException("username in db.json must not null");
    }

    if (StringUtil.isBlank(database.getUrl()) && (StringUtil.isBlank(database.getIp())
        || StringUtil.isBlank(database.getPort()) || StringUtil.isBlank(database.getDatabase()))) {
      throw new IllegalArgumentException("in db.json, if url is null, please set ip、port、database");
    }

    if (StringUtil.isBlank(database.getFilepath())) {
      String location = Container.class.getProtectionDomain().getCodeSource().getLocation()
          .getPath();
      Path path = Paths.get(new File(location).toURI());
      if (!Files.isDirectory(path)) {
        location = path.getParent().toString();
      }
      location = location + File.separator + "excel";
      logger.info("use default excel store path:[{}]", location);
      database.setFilepath(location);
    }

    if (sqlList.size() == 0) {
      throw new IllegalArgumentException("sql config is empty in sql.json, please check it");
    }

    put(Database.class, database);
    put(Sql.class, sqlList);
  }

  public static <T> void put(Class<?> clazz, T data) {
    container.put(clazz, data);
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(Class<?> clazz) {
    return (T) container.get(clazz);
  }

}
