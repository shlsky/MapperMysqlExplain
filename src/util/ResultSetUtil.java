package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Created by hongling.shl on 2018/9/13.
 */
public class ResultSetUtil {
	
	/**
	 * 获取列在行中的索引
	 * @param columnName
	 * @param rs
	 * @return
	 */
	public static Integer getColumnIndex(String columnName,ResultSet rs) throws Exception {
		rs.first();
		ResultSetMetaData metaData = rs.getMetaData();
		for (int i=1;i<=metaData.getColumnCount();i++){
			if (columnName.equals(metaData.getColumnName(i))){
				return i;
			}
		}
		
		throw new RuntimeException("无法找到列名对应的位置");
	}
}
