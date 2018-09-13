package sql.expressionResolver;

import net.sf.jsqlparser.expression.Expression;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongling.shl on 2018/9/13.
 */
public class ExpressionResolver {
	
	public final static List<ExpressionResolver> resolvers = new ArrayList<>();
	
	public ExpressionResolver addSubResolver(ExpressionResolver resolver){
		resolvers.add(resolver);
		return this;
	}
	
	/**
	 * 填充又表达式
	 * @param expression
	 * @param rs
	 * @throws Exception
	 */
	public void fillRightExpression(Expression expression , ResultSet rs) throws Exception{
		if (expression == null){
			return;
		}
		for (ExpressionResolver resolver : resolvers){
			if (resolver.canHandle(expression)){
				resolver.fillRightExpression(expression,rs);
				return;
			}
		}
		throw new RuntimeException("无法处理Expression");
	}
	
	/**
	 * 能否处理
	 * @param expression
	 * @return
	 */
	public boolean canHandle(Expression expression){
		return false;
	}
	
	
}
