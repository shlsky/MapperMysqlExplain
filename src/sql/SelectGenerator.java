package sql;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

import java.sql.ResultSet;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public class SelectGenerator extends BaseGenerator {
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
		
		Select select = (Select) statement;
		SelectBody selectBody = select.getSelectBody();
		if (!(selectBody instanceof PlainSelect)){
			throw new RuntimeException("select语句无法处理");
		}
		PlainSelect plainSelect = (PlainSelect)selectBody;
		FromItem fromItem = plainSelect.getFromItem();
		if (!(fromItem instanceof Table)){
			throw new RuntimeException("select语句无法处理");
		}
		return ((Table) fromItem).getName();
	}
	
	/**
	 * 能否处理
	 *
	 * @param statement
	 * @return
	 */
	@Override
	public boolean canHandle(Statement statement) {
		return statement instanceof Select;
	}
}
