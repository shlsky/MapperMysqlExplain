package sql;

import a.f.E;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public abstract class BaseGenerator {
	
	/**
	 * 生成sql
	 * @param statement sql模板
	 * @param rs        初始化数据
	 * @return
	 */
	public abstract String generateSql(Statement statement,ResultSet rs) throws Exception;
	
	
	/**
	 * 获取列在行中的索引
	 * @param columnName
	 * @param rs
	 * @return
	 */
	private Integer getColumnIndex(String columnName,ResultSet rs) throws Exception {
		rs.first();
		ResultSetMetaData metaData = rs.getMetaData();
		for (int i=1;i<=metaData.getColumnCount();i++){
			if (columnName.equals(metaData.getColumnName(i))){
				return i;
			}
		}
		
		throw new RuntimeException("无法找到列名对应的位置");
	}
	
	/**
	 * 填充表达式右边的值
	 * @param binaryExpression
	 * @param rs
	 * @throws Exception
	 */
	public void fillRightExpression(BinaryExpression binaryExpression , ResultSet rs) throws Exception{
		
		if (binaryExpression.getLeftExpression() instanceof BinaryExpression){
			fillRightExpression((BinaryExpression)binaryExpression.getLeftExpression(),rs);
		}
		
		if (binaryExpression.getRightExpression() instanceof BinaryExpression){
			fillRightExpression((BinaryExpression)binaryExpression.getRightExpression(),rs);
		}
		
		if (binaryExpression.getLeftExpression() instanceof Column){
			Integer index = getColumnIndex(binaryExpression.getLeftExpression().toString(),rs);
			Object object = rs.getObject(index);
			rs.next();
			if (object instanceof Number){
				binaryExpression.setRightExpression(new DoubleValue(object.toString()));
			} else {
				binaryExpression.setRightExpression(new StringValue("'"+object.toString()+"'"));
			}
		}
		
	}
	
	/**
	 * 能否处理
	 * @param statement
	 * @return
	 */
	public abstract boolean canHandle(Statement statement);
}
