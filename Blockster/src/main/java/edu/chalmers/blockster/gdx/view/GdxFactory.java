package edu.chalmers.blockster.gdx.view;


import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.blockster.core.Factory;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;

public class GdxFactory implements Factory {

	protected TiledMap map;
	
	private BlockMap blockMap;
	private GdxMap gdxMap;
	private MiniMap miniMap;

	private final int width;
	private final int height;
	private final int blockWidth;
	private final int blockHeight;
	private final int[][] playerStartingPositions;
	private final TiledMapTileLayer tileLayer;
	private PortalView portalView;
	private HashMap<Integer, PortalView> portalViews;
	private AnimationFactory animFactory = new AnimationFactory();

	private PortalView bluePortalView;
	private PortalView yellowPortalView;
	
	public GdxMap getGdxMap() {
		return gdxMap;
	}

	public GdxFactory(TiledMap map) {
		this.map = map;
		playerStartingPositions = getPlayerStartingPositions(map);
		
		tileLayer = (TiledMapTileLayer) map.getLayers().get(0);
		width = tileLayer.getWidth();
		height = tileLayer.getHeight();
		blockWidth = (int) tileLayer.getTileWidth();
		blockHeight = (int) tileLayer.getTileHeight();
	}
	
	@Override
	public void createMap() {
		miniMap = new MiniMap(width, height);
		blockMap = new BlockMap(width, height, blockWidth, blockHeight, playerStartingPositions);
		gdxMap = new GdxMap(blockMap);
		blockMap.addListener(gdxMap);
		blockMap.addListener(miniMap);
		blockMap.addActiveBlockListener(miniMap);
		insertBlocks();
	}
	
	private void insertBlocks() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final Cell cell = tileLayer.getCell(x, y);
				
				if (cell != null) {
					final TiledMapTile tile = cell.getTile();
					final Block block = new Block(x, y, blockMap);
					final BlockView bView = new BlockView(block, tile);
					
					gdxMap.createBlockViewReference(block, bView);
					blockMap.insertBlock(block);
					setBlockProperties(tile.getProperties(), block);

					MapProperties mapProps = tile.getProperties();
					
					final Iterator<String> properties = mapProps.getKeys();
					while(properties.hasNext()) {
						final String property = properties.next();
						if (property.equals("blue")) {
							bluePortalView = new PortalView(block.getX()*blockWidth, block.getY()*blockHeight, animFactory.getPortalAnimation(0));
						} else if (property.equals("yellow")) {
							yellowPortalView = new PortalView(block.getX()*blockWidth, block.getY()*blockHeight, animFactory.getPortalAnimation(1));
						}
						block.setProperty(property);
					}
				}
			}
			blockMap.addActiveBlockListener(miniMap);
		}
	}
	
	@Override
	public Player createPlayer(float startX, float startY, BlockMap blockLayer) {
		return new Player(startX, startY, blockLayer);
	}
	
	private void setBlockProperties(MapProperties props, Block block) {
		final Iterator<String> properties = props.getKeys();
		while(properties.hasNext()) {
			block.setProperty(properties.next());
		}
	}
	
	private int[][] getPlayerStartingPositions(TiledMap map) {
		
		final MapProperties mapProps = map.getProperties();
		
		int nbrOfPlayers;
		
		try {
			nbrOfPlayers = Integer.parseInt((String)mapProps.get("nbrOfPlayers"));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Given map's nbrOfPlayers value "
					+ mapProps.get("nbrOfPlayers") + " is incorrect. Should be a number 0 <= 10");
		}
			
		int[][] startingPositions = new int[nbrOfPlayers][2];
		
		for(int i = 1; i <= nbrOfPlayers ; i++) {
			
			final String playerStart = (String)mapProps.get("playerStart" + i);
			final String[] playerStarts = playerStart.split(":");
			int[] playerStartFloats = new int[playerStarts.length];
			
			for(int j = 0; j < playerStarts.length; j++) {
				try {
					playerStartFloats[j] = Integer.parseInt(playerStarts[j]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Field player start has wrong"
							+ " format: " + playerStart + ". Should be an float position"
									+ " with the following format: x:y");
				}
			}
			startingPositions[i - 1] = playerStartFloats;
		}
		return startingPositions;
	}

	@Override
	public BlockMap getMap() {
		return blockMap;
	}
	
	public MiniMap getMiniMap() {
		return miniMap;
	}
	public PortalView getPortalView(int color) {
		return color == 0 ? bluePortalView : yellowPortalView;
	}
}