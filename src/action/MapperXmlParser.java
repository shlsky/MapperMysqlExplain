package action;

import com.google.common.base.Joiner;
import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongling.shl on 2018/9/10.
 */
public class MapperXmlParser extends BaseBuilder {
	
	private final XNode context;
	private boolean isDynamic;
	private final Class<?> parameterType;
	private final Map<String, NodeHandler> nodeHandlerMap = new HashMap<>();
	
	public MapperXmlParser(Configuration configuration, XNode context) {
		this(configuration, context, null);
	}
	
	public MapperXmlParser(Configuration configuration, XNode context, Class<?> parameterType) {
		super(configuration);
		this.context = context;
		this.parameterType = parameterType;
		initNodeHandlerMap();
	}
	
	
	private void initNodeHandlerMap() {
		nodeHandlerMap.put("where", new WhereHandler());
		nodeHandlerMap.put("set", new SetHandler());
		nodeHandlerMap.put("if", new IfHandler());
		nodeHandlerMap.put("foreach", new ForEachHandler());
		nodeHandlerMap.put("when", new IfHandler());
		nodeHandlerMap.put("otherwise", new OtherwiseHandler());
		nodeHandlerMap.put("bind", new BindHandler());
	}
	
	
	public String parseDynamicTags(XNode node) {
		List<String> contents = new ArrayList<>();
		NodeList children = node.getNode().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			XNode child = node.newXNode(children.item(i));
			if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE || child.getNode().getNodeType() == Node.TEXT_NODE) {
				String data = child.getStringBody("").trim();
				contents.add(data);
			} else if (child.getNode().getNodeType() == Node.ELEMENT_NODE) { // issue #628
				String nodeName = child.getNode().getNodeName();
				NodeHandler handler = nodeHandlerMap.get(nodeName);
				if (handler == null) {
					throw new BuilderException("Unknown element <" + nodeName + "> in SQL statement.");
				}
				handler.handleNode(child, contents);
				if (nodeName.equals("set"))
				isDynamic = true;
			}
		}
		return Joiner.on(" ").join(contents);
	}
	
	private class ForEachHandler implements NodeHandler {
		public ForEachHandler() {
			// Prevent Synthetic Access
		}
		
		@Override
		public void handleNode(XNode nodeToHandle, List<String> targetContents) {
			targetContents.add("(#{in})");
		}
	}
	
	private interface NodeHandler {
		void handleNode(XNode nodeToHandle, List<String> targetContents);
	}
	
	private class BindHandler implements NodeHandler {
		public BindHandler() {
			// Prevent Synthetic Access
		}
		
		@Override
		public void handleNode(XNode nodeToHandle, List<String> targetContents) {
			final String name = nodeToHandle.getStringAttribute("name");
			final String expression = nodeToHandle.getStringAttribute("value");
			targetContents.add(name+"="+expression);
		}
	}
	
	
	private class WhereHandler implements NodeHandler {
		public WhereHandler() {
			// Prevent Synthetic Access
		}
		
		@Override
		public void handleNode(XNode nodeToHandle, List<String> targetContents) {
			targetContents.add(parseDynamicTags(nodeToHandle));
		}
	}
	
	private class SetHandler implements NodeHandler {
		public SetHandler() {
			// Prevent Synthetic Access
		}
		
		@Override
		public void handleNode(XNode nodeToHandle, List<String> targetContents) {
			targetContents.add("set ");
			targetContents.add(parseDynamicTags(nodeToHandle));
		}
	}
	
	
	private class IfHandler implements NodeHandler {
		public IfHandler() {
			// Prevent Synthetic Access
		}
		
		@Override
		public void handleNode(XNode nodeToHandle, List<String> targetContents) {
			targetContents.add(parseDynamicTags(nodeToHandle));
		}
	}
	
	private class OtherwiseHandler implements NodeHandler {
		public OtherwiseHandler() {
			// Prevent Synthetic Access
		}
		
		@Override
		public void handleNode(XNode nodeToHandle, List<String> targetContents) {
			
			targetContents.add(parseDynamicTags(nodeToHandle));
		}
	}
	
	
	public boolean isDynamic() {
		return isDynamic;
	}
	
	public void setDynamic(boolean dynamic) {
		isDynamic = dynamic;
	}
}
