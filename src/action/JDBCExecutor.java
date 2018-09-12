package action;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import sql.InsertGenerator;
import sql.SelectGenerator;
import sql.UpdateGenerator;

import java.sql.*;

/**
 * Created by hongling.shl on 2018/9/11.
 */
public class JDBCExecutor {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url;
	private static String username;
	private static String password;

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

			Update update = (Update) CCJSqlParserUtil.parse("update supplier_privilege_type\n" +"set privilege_name=?,privilege_desc=? where id=? and privilege_code=?");
			UpdateGenerator updateGenerator = new UpdateGenerator();
			System.out.println(updateGenerator.generateSql(update,rs));
//
			Insert insert = (Insert) CCJSqlParserUtil.parse("insert into supplier_privilege_type(privilege_name,privilege_code,privilege_desc,sequence)\n"+"values(?,?,?,?),(?,?,?,?),(?,?,?,?)");
			InsertGenerator insertGenerator = new InsertGenerator();
			System.out.println(insertGenerator.generateSql(insert,rs));

			Select select = (Select) CCJSqlParserUtil.parse("select * from supplier_privilege_type\n" + " where id = ? and privilege_name=?");
			SelectGenerator selectGenerator = new SelectGenerator();
			System.out.println(selectGenerator.generateSql(select,rs));

//			Delete delete = (Delete) CCJSqlParserUtil.parse("delete from supplier_privilege_type\n" + " where id = ? and privilege_name=?");
//
//			DeleteGenerator deleteGenerator = new DeleteGenerator();
//
//			System.out.println(deleteGenerator.generateSql(delete,rs));
//
//			BinaryExpression binaryExpression = (BinaryExpression)(delete.getWhere().getClass().cast(delete.getWhere()));
//
//			if (binaryExpression.getRightExpression() instanceof JdbcParameter){
//				binaryExpression.setRightExpression(new StringValue("{{"+binaryExpression.getLeftExpression().toString()+"}}"));
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init(String url,String username,String password){
		JDBCExecutor.url = url;
		JDBCExecutor.username = username;
		JDBCExecutor.password = password;
	}

	public static Connection getConnection() throws Exception{
		try{
			Class.forName(driver).newInstance();
			return DriverManager.getConnection(url,username,password);
		}catch (Exception e){
			throw new Exception();
		}
	}

	public static ResultSet getData(String tableName) throws Exception{

		String sql = "select * from "+tableName+" limit 10";

		Connection conn = getConnection();
		Statement statement = conn.createStatement();

		ResultSet rs = statement.executeQuery(sql);
		return rs;

	}

}
