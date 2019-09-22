package top.feb13th.script.excel.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * excel 工具类
 *
 * @author feb13th
 * @date 2019/9/21 19:11
 */
public class ExcelUtil {

  private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

  /**
   * 创建一个xlsx格式的文件
   */
  public static Workbook createFile() {
    return new XSSFWorkbook();
  }

  /**
   * 创建单元格标题
   *
   * @param workbook 工作目录
   * @param columns 标题名称列表
   * @return 工作表
   */
  public static Sheet createTitle(Workbook workbook, List<String> columns) {
    Sheet sheet = workbook.createSheet();
    // title row
    Row row = sheet.createRow(0);
    for (int i = 0; i < columns.size(); i++) {
      row.createCell(i).setCellValue(columns.get(i));
    }
    return sheet;
  }

  /**
   * 创建内容
   *
   * @param sheet 工作表
   * @param list 数据列表
   * @param dateFormat 日期格式化
   */
  public static void createBody(Sheet sheet, List<Object[]> list, DateFormat dateFormat) {
    for (int i = 1; i <= list.size(); i++) {
      Row row = sheet.createRow(i);
      Object[] data = list.get(i - 1);
      for (int j = 0; j < data.length; j++) {
        Object obj = data[j];
        if (obj instanceof Date) {
          obj = dateFormat.format((Date) obj);
        }
        if (obj instanceof java.sql.Date) {
          obj = dateFormat.format(new Date(((java.sql.Date) obj).getTime()));
        }
        if (obj instanceof Timestamp) {
          obj = dateFormat.format(new Date(((Timestamp) obj).getTime()));
        }
        row.createCell(j).setCellValue(ObjectUtil.isNull(obj) ? "" : obj.toString());
      }
    }
  }

  /**
   * 写出文件
   *
   * @param workbook 工作表
   * @param path 文件路径
   */
  public static void writeFile(Workbook workbook, String... path) {
    if (path.length == 0) {
      throw new IllegalArgumentException("excel path must not null");
    }
    String filename = path[path.length - 1];
    int dotIndex = filename.lastIndexOf(".");
    if (dotIndex != -1) {
      filename = filename.substring(0, dotIndex);
    }
    filename = filename + ".xlsx";
    path[path.length - 1] = filename;
    StringBuilder sb = new StringBuilder(path[0]);
    for (int i = 1; i < path.length; i++) {
      sb.append(File.separator).append(path[i]);
    }
    OutputStream outputStream = null;
    try {
      Path p = Paths.get(new File(sb.toString()).toURI());
      Files.deleteIfExists(p);
      if (!Files.exists(p.getParent())) {
        Files.createDirectories(p.getParent());
      }
      Files.createFile(p);
      outputStream = Files.newOutputStream(p);
      workbook.write(outputStream);
      logger.info("create excel file complete, filepath: [{}]", p.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
