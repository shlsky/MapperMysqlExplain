package sql;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

import java.sql.ResultSet;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public class DeleteGenerator extends BaseGenerator {
	/**
	 * 生成sql
	 *
	 * @param statement sql模板
	 * @param rs        初始化数据
	 * @return
	 */
	@Override
	public String generateSql(Statement statement, ResultSet rs) throws Exception{
		Delete delete = (Delete) statement;
		BinaryExpression binaryExpression = castToBinaryExpression(delete.getWhere());
		fillRightExpression(binaryExpression,rs);
		
		return delete.toString();
	}
	
	
	
	/**
	 * 能否处理
	 *
	 * @param statement
	 * @return
	 */
	@Override
	public boolean canHandle(Statement statement) {
		return statement instanceof Delete;
	}
}
