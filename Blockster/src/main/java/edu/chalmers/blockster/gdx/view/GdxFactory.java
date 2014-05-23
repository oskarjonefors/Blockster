package edu.chalmers.blockster.gdx.view;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.blockster.core.Factory;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.BlocksterObject;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;

public class GdxFactory implements Factory {
	
	private BlockMap blockMap;
	private GdxMap gdxMap;
	private MiniMap miniMap;

	private final int width;
	private final int height;
	private final int blockWidth;
	private final int blockHeight;
	private final List<Point> playerStartingPositions;
	private final TiledMapTileLayer tileLayer;
	private AnimationFactory animFactory = new AnimationFactory();

	private PortalView bluePortalView;
	private PortalView yellowPortalView;
	
	public GdxMap getGdxMap() {
		return gdxMap;
	}

	public GdxFactory(TiledMap map) {
		playerStartingPositions = getPlayerStartingPositions(map);
		final MapLayer mapLayer = map.getLayers().get(0);
		if(mapLayer instanceof TiledMapTileLayer) {
			tileLayer = (TiledMapTileLayer) mapLayer;
		} else {
			tileLayer = new TiledMapTileLayer(0,0,0,0);
		}
		width = tileLayer.getWidth();
		height = tileLayer.getHeight();
		blockWidth = (int) tileLayer.getTileWidth();
		blockHeight = (int) tileLayer.getTileHeight();
	}
	
	@Override
	public void createMap() {
		final List<Point> tempList = new ArrayList<Point>();
		tempList.add(new Point(0, 0));
		final BlockMap tempMap = new BlockMap(1, 1, 1f, 1f, tempList);
		miniMap = new MiniMap(width, height, new Player(0, 0, tempMap, World.DAY));
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
					processCell(cell, x, y);
				}
			}
			blockMap.addActiveBlockListener(miniMap);
		}
	}
	
	private void processCell(Cell cell, int x, int y) {
		final TiledMapTile tile = cell.getTile();
		final Block block = new Block(x, y, blockMap);
		final BlockView bView = new BlockView(block, tile);
		final MapProperties tileProps = tile.getProperties();
		
		gdxMap.createBlockViewReference(block, bView);
		blockMap.insertBlock(block);
		setBlockProperties(tileProps, block);
		
		final Iterator<String> properties = tileProps.getKeys();
		while(properties.hasNext()) {
			final String property = properties.next();
			initPortalViews(property, block);
			block.setProperty(property);
		}
	}
	
	@Override
	public Player createPlayer(float startX, float startY, BlockMap blockLayer,
			World world) {
		return new Player(startX, startY, blockLayer, world);
	}
	
	private void setBlockProperties(MapProperties props, Block block) {
		final Iterator<String> properties = props.getKeys();
		while(properties.hasNext()) {
			block.setProperty(properties.next());
		}
	}
	
	private List<Point> getPlayerStartingPositions(TiledMap map) {
		
		final MapProperties mapProps = map.getProperties();
		
		int	nbrOfPlayers = Integer.parseInt((String)mapProps.get("nbrOfPlayers"));
		
		final List<Point> startingPositions = new ArrayList<Point>();
		
		for(int i = 1; i <= nbrOfPlayers ; i++) {
			final String playerStartString = (String)mapProps.get("playerStart" + i);
			final String[] playerStarts = playerStartString.split(":");
			startingPositions.add(new Point(Integer.parseInt(playerStarts[0]),
					Integer.parseInt(playerStarts[1])));
		}
		return startingPositions;
	}
	
	public void initPortalViews(String property, BlocksterObject block) {
		if ("blue".equals(property)) {
			bluePortalView = new PortalView(block.getX()*blockWidth, block.getY()*blockHeight, animFactory.getPortalAnimation(0));
		} else if ("yellow".equals(property)) {
			yellowPortalView = new PortalView(block.getX()*blockWidth, block.getY()*blockHeight, animFactory.getPortalAnimation(1));
		}
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