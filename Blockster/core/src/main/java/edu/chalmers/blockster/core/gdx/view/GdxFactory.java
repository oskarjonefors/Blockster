package edu.chalmers.blockster.core.gdx.view;


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
	
	public GdxMap getGdxMap() {
		return gdxMap;
	}

	public GdxFactory(TiledMap map) {
		this.map = map;
	}
	
	@Override
	public void createMap() {
		final int[][] playerStartingPositions = getPlayerStartingPositions(map);
		final TiledMapTileLayer tileLayer = (TiledMapTileLayer) map.getLayers().get(0);
		final int width = tileLayer.getWidth();
		final int height = tileLayer.getHeight();
		final int blockWidth = (int) tileLayer.getTileWidth();
		final int blockHeight = (int) tileLayer.getTileHeight();

		
		miniMap = new MiniMap(tileLayer.getWidth(), tileLayer.getHeight());
		blockMap = new BlockMap(width, height, blockWidth, blockHeight, playerStartingPositions);
		gdxMap = new GdxMap(blockMap);
		blockMap.addListener(gdxMap);
		blockMap.addListener(miniMap);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final Cell cell = tileLayer.getCell(x, y);
				
				if (cell != null) {
					final TiledMapTile tile = cell.getTile();
					final Block block = new Block(x, y, blockMap);
					final BlockView bView = new BlockView(block, tile);
					
					gdxMap.createBlockViewReference(block, bView);
					blockMap.insertBlock(block);
					
					MapProperties mapProps = tile.getProperties();
					
					final Iterator<String> properties = mapProps.getKeys();
					while(properties.hasNext()) {
						final String property = properties.next();
						block.setProperty(property);
					}
				}
			}
		}
		blockMap.addActiveBlockListener(miniMap);
	}
	
	@Override
	public Player createPlayer(float startX, float startY, BlockMap blockLayer) {
		return new Player(startX, startY, blockLayer);
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
}