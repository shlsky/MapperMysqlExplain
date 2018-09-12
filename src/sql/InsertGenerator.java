package sql;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public class InsertGenerator extends BaseGenerator {
	/**
	 * 生成sql
	 *
	 * @param statement sql模板
	 * @param rs        初始化数据
	 * @return
	 */
	@Override
	public String generateSql(Statement statement, ResultSet rs) throws Exception {
		Insert insert = (Insert) statement;
		fillInsertValue(insert,rs);

		return insert.toString();
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
		Insert insert = (Insert) statement;
		return insert.getTable().getName();
	}
	
	/**
	 * 能否处理
	 *
	 * @param statement
	 * @return
	 */
	@Override
	public boolean canHandle(Statement statement) {
		return statement instanceof Insert;
	}

	/**
	 *
	 * @param insert
	 * @param rs
	 * @throws Exception
	 */
	public void fillInsertValue(Insert insert, ResultSet rs) throws Exception{
		List<Column> columns = insert.getColumns();
		MultiExpressionList multiExpressions = (MultiExpressionList)insert.getItemsList();
		List<ExpressionList> expressionsExprList =  multiExpressions.getExprList();//2


		MultiExpressionList multiExpressionList = new MultiExpressionList();
		for(int j=0;j<expressionsExprList.size();j++) {

			List<Expression> expressions = new ArrayList<>();
			for(int i=0;i<columns.size();i++) {
				Integer index = getColumnIndex(columns.get(i).getColumnName(),rs);
				rs.next();
				Object obj = rs.getObject(index);
				if(obj instanceof Number) {
					expressions.add(new DoubleValue(obj.toString()));
				}else {
					expressions.add(new StringValue("'"+obj.toString()+"'"));
				}
			}

			ExpressionList expressionList = new ExpressionList();
			expressionList.setExpressions(expressions);

			multiExpressionList.addExpressionList(expressionList);
		}

		insert.setItemsList(multiExpressionList);
	}
}
