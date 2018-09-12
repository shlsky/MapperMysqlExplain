package action;

import com.google.common.collect.Lists;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import sql.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongling.shl on 2018/9/12.
 */
public class MapperSqlParserExplain {
	
	private List<BaseGenerator> generators = Lists.newArrayList(new InsertGenerator(), new DeleteGenerator(), new UpdateGenerator(), new SelectGenerator());
	
	private Map<String, ResultSet> tableData = new HashMap<>();
	
	private final static String SELECT_SQL_TEMPLATE = "select * from %s limit 10";
	
	public String parseToRealSql(String sqlTemplate, Connection conn) throws Exception {
		Statement statement = CCJSqlParserUtil.parse(sqlTemplate);
		BaseGenerator generator = fetchSqlGenerator(statement);
		String tableName = generator.fetchTableName(statement);
		ResultSet resultSet = getResultSet(tableName, conn);
		
		
		return generator.generateSql(statement, resultSet);
		
		
	}
	
	public String executeSqlExplain(String realSql,Connection conn) throws Exception{
		ResultSet rs = conn.createStatement().executeQuery(String.format("explain %s", realSql));
		ResultSetMetaData metaData = rs.getMetaData();
		
		StringBuilder stringBuilder = new StringBuilder();
		while (rs.next()) {
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				stringBuilder.append(String.format("%-15s", metaData.getColumnName(i))).append(" : ").append(rs.getString(i)).append("\n");
			}
		}
		return stringBuilder.toString();
	}
	
	
	public ResultSet getResultSet(String tableName, Connection conn) throws Exception {
		if (tableData.containsKey(tableName)) {
			return tableData.get(tableName);
		}
		String sql = String.format(SELECT_SQL_TEMPLATE, tableName);
		java.sql.Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		tableData.put(tableName, rs);
		
		return tableData.get(tableName);
	}
	
	private BaseGenerator fetchSqlGenerator(Statement statement) {
		for (BaseGenerator generator : generators) {
			if (generator.canHandle(statement)) {
				return generator;
			}
		}
		throw new RuntimeException("不能处理该sql");
	}
	
}
