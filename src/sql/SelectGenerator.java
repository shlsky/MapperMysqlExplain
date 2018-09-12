package sql;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;

import java.sql.ResultSet;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public class SelectGenerator implements SQLGenerator {
	/**
	 * 生成sql
	 *
	 * @param statement sql模板
	 * @param rs        初始化数据
	 * @return
	 */
	@Override
	public String generatSql(Statement statement, ResultSet rs) {
		return null;
	}
	
	/**
	 * 能否处理
	 *
	 * @param statement
	 * @return
	 */
	@Override
	public boolean canHandle(Statement statement) {
		return statement instanceof Select;
	}
}
