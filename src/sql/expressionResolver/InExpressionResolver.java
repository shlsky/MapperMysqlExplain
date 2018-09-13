package sql.expressionResolver;

import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import util.ResultSetUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongling.shl on 2018/9/13.
 */
public class InExpressionResolver extends ExpressionResolver {
	/**
	 * 填充又表达式
	 *
	 * @param expression
	 * @param rs
	 * @throws Exception
	 */
	@Override
	public void fillRightExpression(Expression expression, ResultSet rs) throws Exception {
		rs.first();
		InExpression inExpression = (InExpression)expression;
		if (inExpression.getLeftExpression() instanceof Column){
			ExpressionList expressionList = (ExpressionList)inExpression.getRightItemsList();
			Integer index = ResultSetUtil.getColumnIndex(inExpression.getLeftExpression().toString(), rs);
			
			
			rs.next();
			Object object = rs.getObject(index);
			
			
			if (object instanceof Number){
				expressionList.setExpressions(Lists.newArrayList(new DoubleValue(object.toString())));
			} else {
				expressionList.setExpressions(Lists.newArrayList(new StringValue("'"+object.toString()+"'")));
			}
		}
	}
	
	/**
	 * 能否处理
	 *
	 * @param expression
	 * @return
	 */
	@Override
	public boolean canHandle(Expression expression) {
		return expression instanceof InExpression;
	}
}
