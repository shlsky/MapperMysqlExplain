package sql;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

import java.sql.ResultSet;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public class UpdateGenerator extends BaseGenerator {
	/**
	 * 生成sql
	 *
	 * @param statement sql模板
	 * @param rs        初始化数据
	 * @return
	 */
	@Override
	public String generateSql(Statement statement, ResultSet rs) throws Exception {
		return null;
	}
	
	/**
	 * 获取表名称
	 *
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	@Override
	public String fetchTableName(Statement statement) throws Exception {
		
		Update update = (Update) statement;
		
		return update.getTable().getName();
	}
	
	/**
	 * 能否处理
	 *
	 * @param statement
	 * @return
	 */
	@Override
	public boolean canHandle(Statement statement) {
		return statement instanceof Update;
	}
}
