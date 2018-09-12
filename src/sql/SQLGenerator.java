package sql;

import net.sf.jsqlparser.statement.Statement;

import java.sql.ResultSet;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public interface SQLGenerator {
	
	/**
	 * 生成sql
	 * @param statement sql模板
	 * @param rs        初始化数据
	 * @return
	 */
	String generatSql(Statement statement,ResultSet rs);
	
	/**
	 * 能否处理
	 * @param statement
	 * @return
	 */
	boolean canHandle(Statement statement);
}
