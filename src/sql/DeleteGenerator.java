package sql;

import javafx.util.Pair;
import net.sf.jsqlparser.expression.Expression;
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
	public Pair<String,StringBuilder> generateSql(Statement statement, ResultSet rs) throws Exception{
		Delete delete = (Delete) statement;
		StringBuilder stringBuilder = new StringBuilder();
		Expression where = delete.getWhere();
		if (where == null){
			stringBuilder.append("Delete sql 没有where条件!");
		}
		expressionResolver.fillRightExpression(delete.getWhere(),rs);
		
		return new Pair<>(delete.toString(),stringBuilder);
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
		Delete delete = (Delete) statement;
		
		return delete.getTable().getName();
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
