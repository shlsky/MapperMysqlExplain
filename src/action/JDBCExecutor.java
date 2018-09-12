package action;

import com.mysql.jdbc.ResultSetImpl;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.ReplaceDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;
import sql.DeleteGenerator;

import java.io.StringReader;
import java.sql.*;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public class JDBCExecutor {
	
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.250:3307/growth","dev_w","6nvjq0_HW");
			Statement statement = conn.createStatement();
			ResultSet rs= statement.executeQuery("SELECT * FROM dada_grade.supplier_privilege_type");
			ResultSetMetaData metaData = rs.getMetaData();
			ResultSet rs1= rs;
			while (rs.next()){
				
				for (int i=1;i<=metaData.getColumnCount();i++){
					System.out.println(String.format("%-15s",metaData.getColumnName(i)) + " : " + rs.getString(i));
				}
				
			}
			rs.first();
			Delete delete = (Delete) CCJSqlParserUtil.parse("delete from supplier_privilege_type\n" + " where id = ? and privilege_name=?");
			
			DeleteGenerator deleteGenerator = new DeleteGenerator();
			
			System.out.println(deleteGenerator.generateSql(delete,rs));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
