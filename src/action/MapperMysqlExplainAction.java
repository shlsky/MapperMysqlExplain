package action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import dialog.DbParamsDialog;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Objects;

public class MapperMysqlExplainAction extends AnAction {
	
	//jdbc:mysql://192.168.1.250:3307/growth
	private static final String LAST_DB_URL = "LAST_DB_URL";
	
	//dev_w
	private static final String LAST_DB_USER = "LAST_DB_USER";
	
	//6nvjq0_HW
	private static final String LAST_DB_PASSWORD = "LAST_DB_PASSWORD";
	
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
		PropertiesComponent component = PropertiesComponent.getInstance(e.getProject());
		
		
		DbParamsDialog dbParamsDialog = new DbParamsDialog((dbUrl, dbUser, dbPassword) -> {
			component.setValue(LAST_DB_URL, dbUrl);
			component.setValue(LAST_DB_USER, dbUser);
			component.setValue(LAST_DB_PASSWORD, dbPassword);
			if (jTextArea != null && file != null) {
				Configuration configuration = new Configuration();
				MapperSqlParserExplain parserExplain = new MapperSqlParserExplain();
				try (InputStream inputStream = file.getInputStream()) {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
					XPathParser xPathParser = new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver());
					List<XNode> list = xPathParser.evalNode("/mapper").evalNodes("select|insert|update|delete");
					for (XNode xNode : list) {
						MapperXmlParser mapperXmlParser = new MapperXmlParser(configuration, xNode);
						String sqlTemplate = mapperXmlParser.parseDynamicTags(xNode).replaceAll("#\\{.*?}", "?");
						String realSql = parserExplain.parseToRealSql(sqlTemplate, conn);
						String explainResult = parserExplain.executeSqlExplain(realSql, conn);
						jTextArea.append("########################################################################\n\n");
						jTextArea.append("mapper sql id : "+xNode.getStringAttribute("id") + " \n\n");
						jTextArea.append("参数代入生成sql : \n" +realSql + "\n\n");
						jTextArea.append("-----------------------------mysql explain 结果-------------------------\n\n");
						jTextArea.append(explainResult + "\n\n");
						
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
					jTextArea.append("sql解析失败了," + e1.getMessage() +"\n\n");
				}
			}
		});
		if (!StringUtils.isBlank(component.getValue(LAST_DB_URL)) && !StringUtils.isBlank(component.getValue(LAST_DB_USER)) && !StringUtils.isBlank(component.getValue(LAST_DB_PASSWORD))) {
			
			dbParamsDialog.getDb_url().setText(component.getValue(LAST_DB_URL));
			dbParamsDialog.getDb_user().setText(component.getValue(LAST_DB_USER));
			dbParamsDialog.getDb_password().setText(component.getValue(LAST_DB_PASSWORD));
		}
		
		dbParamsDialog.setVisible(true);
	}
	
	private String analyzeExplain(ResultSet rs) throws Exception{
		
		ResultSetMetaData metaData = rs.getMetaData();
		
		StringBuilder stringBuilder = new StringBuilder();
		while (rs.next()) {
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				stringBuilder.append(String.format("%-15s", metaData.getColumnName(i))).append(" : ").append(rs.getString(i)).append("\n");
			}
		}
		return stringBuilder.toString();
	}
	
	@Override
	public void update(AnActionEvent e) {
		super.update(e);
		
		try {
			boolean visible = isActionAvailable(e);
			final Presentation presentation = e.getPresentation();
			presentation.setVisible(visible);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	private boolean isActionAvailable(AnActionEvent e) throws Exception {
		final VirtualFile file = getVirtualFiles(e);
		if (getEventProject(e) != null && file != null) {
			final FileType fileType = file.getFileType();
			if (!StdFileTypes.XML.equals(fileType)) {
				return false;
			}
			try {
				InputStream inputStream = file.getInputStream();
				Configuration configuration = new Configuration();
				XPathParser xPathParser = new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver());
				XNode xNode = xPathParser.evalNode("/mapper");
				if (!Objects.isNull(xNode)) {
					return true;
				}
			} catch (Exception e1) {
				return false;
			}
		}
		
		return false;
	}
	
	private VirtualFile getVirtualFiles(AnActionEvent e) {
		return PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
	}
	
}
