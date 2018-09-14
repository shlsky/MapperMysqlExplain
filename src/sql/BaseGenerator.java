package sql;

import javafx.util.Pair;
import net.sf.jsqlparser.statement.Statement;
import sql.expressionResolver.BinaryExpressionResolver;
import sql.expressionResolver.ExpressionResolver;
import sql.expressionResolver.InExpressionResolver;

import java.sql.ResultSet;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public abstract class BaseGenerator {
	
	protected ExpressionResolver expressionResolver = new ExpressionResolver();
	
	{
		expressionResolver.addSubResolver(new BinaryExpressionResolver()).addSubResolver(new InExpressionResolver());
	}
	
	/**
	 * 生成sql
	 * @param statement sql模板
	 * @param rs        初始化数据
	 * @return
	 */
	public abstract Pair<String,StringBuilder> generateSql(Statement statement, ResultSet rs) throws Exception;
	
	/**
	 * 获取表名称
	 * @param statement
	 * @return
	 * @throws Exception
	 */
	public abstract String fetchTableName(Statement statement) throws Exception;
	
//	/**
//	 * 转换成BinaryExpression队形
//	 * @param expression
//	 * @return
//	 * @throws Exception
//	 */
//	public BinaryExpression castToBinaryExpression(Expression expression)throws Exception {
//		if (expression == null){
//			return null;
//		}
//		if (expression instanceof BinaryExpression){
//			return (BinaryExpression)(expression.getClass().cast(expression));
//		}
//		throw new RuntimeException("无法解析成BinaryExpression");
//	}
	
	/**
	 * 能否处理
	 * @param statement
	 * @return
	 */
	public abstract boolean canHandle(Statement statement);
}
