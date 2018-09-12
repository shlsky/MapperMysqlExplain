package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import dialog.DbParamsDialog;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Objects;

public class MapperMysqlExplainAction extends AnAction {
	
	private JTextArea jTextArea;
	
	@Override
	public void actionPerformed(AnActionEvent e) {
		
		ToolWindow toolWindow = ToolWindowManager.getInstance(Objects.requireNonNull(e.getProject())).getToolWindow("Mapper mysql explain");
		if (toolWindow != null) {
			
			toolWindow.show(() -> {
			});
			
			jTextArea = (JTextArea) ((JScrollPane) Objects.requireNonNull(toolWindow.getContentManager().getContent(0)).getComponent().getComponent(0)).getViewport().getComponent(0);
		}
		
		// 获取当前选择的文件或文件夹路径
		final VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
		
		
		DbParamsDialog dbParamsDialog = new DbParamsDialog((dbUrl, dbUser, dbPassword) -> {
			if (jTextArea != null && file != null) {
				Configuration configuration = new Configuration();
				MapperSqlParserExplain parserExplain = new MapperSqlParserExplain();
				try (InputStream inputStream = file.getInputStream()) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.250:3307/growth","dev_w","6nvjq0_HW");
					XPathParser xPathParser = new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver());
					List<XNode> list = xPathParser.evalNode("/mapper").evalNodes("select|insert|update|delete");
					for (XNode xNode : list) {
						MapperXmlParser mapperXmlParser = new MapperXmlParser(configuration, xNode);
						String sqlTemplate = mapperXmlParser.parseDynamicTags(xNode).replaceAll("#\\{.*?}","?");
						String realSql = parserExplain.parseToRealSql(sqlTemplate,conn);
						String explainResult = parserExplain.executeSqlExplain(realSql,conn);
						jTextArea.append("###############################################################################################\n");
						
						jTextArea.append(realSql + "\n");
						jTextArea.append("***********************************mysql explain 结果******************************************\n");
						jTextArea.append(explainResult + "\n");
						jTextArea.append("###############################################################################################\n");
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
					jTextArea.append("sql解析失败了,"+e1.getMessage());
				}
			}
		});
		
		dbParamsDialog.setVisible(true);
	}
}
