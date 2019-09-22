package top.feb13th.script.excel.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * sql与excel对应关系
 *
 * @author feb13th
 * @date 2019/9/21 14:15
 */
@Getter
@Setter
@NoArgsConstructor
public class Sql {

  private String sql;
  private String filename;

}
