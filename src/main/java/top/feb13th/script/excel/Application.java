package top.feb13th.script.excel;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.feb13th.script.excel.domain.Database;
import top.feb13th.script.excel.domain.Sql;
import top.feb13th.script.excel.engine.EngineHelper;
import top.feb13th.script.excel.util.DbUtil;
import top.feb13th.script.excel.util.ExcelUtil;
import top.feb13th.script.excel.util.StringUtil;

/**
 * 程序入口
 *
 * @author feb13th
 * @date 2019/9/21 14:06
 */
public class Application {

  private static Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    try {
      Application application = new Application();
      application.run();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private void run() {
    // 基础数据
    Database database = Container.get(Database.class);
    List<Sql> sqlList = Container.get(Sql.class);
    Connection connection = EngineHelper.getConnection(database);

    try {
      String filepath = database.getFilepath();
      String dateFormat = database.getDateFormat();
      if (StringUtil.isBlank(dateFormat)) {
        dateFormat = Consts.DEFAULT_DATEFORMAT;
      }
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

      for (Sql sql : sqlList) {
        String querySql = sql.getSql();
        String filename = sql.getFilename();
        // 查询数据
        List<Object[]> result = DbUtil.executeQuery(connection, querySql);
        List<String> queryColumns = DbUtil.getQueryColumns(querySql);
        // 生成excel
        Workbook workbook = ExcelUtil.createFile();
        Sheet sheet = ExcelUtil.createTitle(workbook, queryColumns);
        ExcelUtil.createBody(sheet, result, simpleDateFormat);
        ExcelUtil.writeFile(workbook, filepath, filename);
      }
    } finally {
      DbUtil.close(connection);
    }
  }

}
