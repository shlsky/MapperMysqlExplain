package sql.expressionResolver;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import util.ResultSetUtil;

import java.sql.ResultSet;

/**
 * Created by hongling.shl on 2018/9/13.
 */
public class BinaryExpressionResolver extends ExpressionResolver {
	/**
	 * 填充又表达式
	 *
	 * @param expression
	 * @param rs
	 * @throws Exception
	 */
	@Override
	public void fillRightExpression(Expression expression, ResultSet rs) throws Exception {
		
		BinaryExpression binaryExpression = (BinaryExpression)expression;
		if (binaryExpression == null){
			return;
		}
		if (binaryExpression.getLeftExpression() instanceof Column){
			Integer index = ResultSetUtil.getColumnIndex(binaryExpression.getLeftExpression().toString(), rs);
			rs.next();
			Object object = rs.getObject(index);
			
			if (object instanceof Number){
				binaryExpression.setRightExpression(new DoubleValue(object.toString()));
			} else {
				binaryExpression.setRightExpression(new StringValue("'"+object.toString()+"'"));
			}
			return;
		}
		super.fillRightExpression(binaryExpression.getLeftExpression(),rs);
		super.fillRightExpression(binaryExpression.getRightExpression(),rs);
		
		
	}
	
	/**
	 * 能否处理
	 *
	 * @param expression
	 * @return
	 */
	@Override
	public boolean canHandle(Expression expression) {
		return expression instanceof BinaryExpression;
	}
}
