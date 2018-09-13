package sql;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;
import util.ResultSetUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		Update update = (Update) statement;
		//处理set
		fillUpdateSetValue(update,rs);
		
		//处理where
        expressionResolver.fillRightExpression(update.getWhere(),rs);

		return update.toString();
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

	/**
	 * 填充update set表達式值
	 * @param update
	 * @param rs
	 * @throws Exception
	 */
	public void fillUpdateSetValue(Update update, ResultSet rs) throws Exception{
		List<Column> columns = update.getColumns();
		if(Objects.isNull(columns)){
			return;
		}

		List<Expression> expressions = new ArrayList<>();
		for(int i=0;i<columns.size();i++){
			Integer index = ResultSetUtil.getColumnIndex(columns.get(i).getColumnName(),rs);
			rs.next();
			Object obj = rs.getObject(index);
			if(obj instanceof Number) {
				expressions.add(new DoubleValue(obj.toString()));
			}else {
				expressions.add(new StringValue("'"+obj.toString()+"'"));
			}
		}
		update.setExpressions(expressions);
	}
}
