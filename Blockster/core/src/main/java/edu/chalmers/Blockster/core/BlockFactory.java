package edu.chalmers.Blockster.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BlockFactory {

	private final static File relBlockDirectory = new File("");
	
	/* Bypassed for the time being, we only need one block for the test.
	 * 
	 * private final static HashMap<Integer, BlockProperties> blockDefinitions 
		= new HashMap<Integer, BlockProperties>();
	
	static {
		try {
			defineBlocks();
		} catch (IOException |ParserConfigurationException | SAXException e) {
		}
	}*/
	
	
	public static Block createBlock(int RGB) {
		return null;
		//return new Block(); Use this one as soon as Block is a proper class with set default values;
		//return createBlock(blockDefinitions.get(RGB)); Use this one for the final product
	}
	
	public static Block createBlock(BlockProperties p) {
		return null;
	}
	
	@SuppressWarnings("unused")
	private static void defineBlocks() throws IOException, ParserConfigurationException, SAXException {
		File blockFile = new File(relBlockDirectory,"blocks.xml");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document document = db.parse(blockFile);
        Element docElement = document.getDocumentElement();
        NodeList blocks = docElement.getElementsByTagName("Blocks");
        
        if (blocks != null && blocks.getLength() > 0) {
        	for (int i = 0; i < blocks.getLength(); i++) {
        		BlockProperties bp = new BlockProperties();
        		Node block =  blocks.item(i);
        		
        		if (block != null && block.getNodeType() == Node.ELEMENT_NODE) {
        			String name = getAttribute(block, "name");
        			String destructible = getAttribute(block, "name");
        			String movable = getAttribute(block, "name");
        			
        			if (name != null) {
        				bp.setName(name);
        				if (destructible != null && (destructible.equals("true")
        						|| destructible.equals("false"))) {
        					bp.setDestructible(destructible);
        				}
        				
        				if (movable != null && (movable.equals("true")
        						|| movable.equals("false"))) {
        					bp.setMovable(movable);
        				}
        				
        			}
        		}
        	}
        }
		
		
	}
	
	private static String getAttribute(Node node, String tag) {
		try {
			return node.getAttributes().getNamedItem(tag).getNodeValue();
		} catch (Exception e) {
			return null;
		}
	}
}
