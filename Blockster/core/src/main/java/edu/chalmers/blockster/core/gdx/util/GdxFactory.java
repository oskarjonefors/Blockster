package edu.chalmers.blockster.core.gdx.util;


import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.blockster.core.Factory;
import edu.chalmers.blockster.core.gdx.view.BlockView;
import edu.chalmers.blockster.core.gdx.view.GdxMap;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;

public class GdxFactory implements Factory {

	protected TiledMap map;
	
	private BlockMap blockMap;
	private GdxMap gdxMap;
	private Pixmap miniMap;
	
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
		
		blockMap = new BlockMap(width, height, blockWidth, blockHeight, playerStartingPositions);
		gdxMap = new GdxMap(blockMap);
		blockMap.setListener(gdxMap);
		miniMap = new Pixmap(tileLayer.getWidth(), tileLayer.getHeight(), Format.RGBA8888);
		
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
					
					int color = 0;
					
					if (mapProps.containsKey("Liftable")) {
						color = Color.toIntBits(0, 0, 255, 255);
					} else if(mapProps.containsKey("Solid")) {
						color = Color.rgba8888(0.7f, 0.7f, 0.7f, 1f);
					}
					
					if(color != 0) {
						miniMap.drawPixel(x, height - y - 1, color);
					}
						
					final Iterator<String> properties = mapProps.getKeys();
					while(properties.hasNext()) {
						final String property = properties.next();
						block.setProperty(property);
					}
				}
			}
		}
	}
	
	@Override
	public Player createPlayer(float startX, float startY, BlockMap blockLayer) {
		return new Player(startX, startY, blockLayer);
	}

	private int[][] getPlayerStartingPositions(TiledMap map) {
		
		final MapProperties mapProps = map.getProperties();
		
		if(!mapProps.containsKey("nbrOfPlayers")) {
			throw new IllegalArgumentException("Given map does not contain"
					+ "\"nbrOfPlayers\" property.");
		}
		
		int nbrOfPlayers;
		
		try {
			nbrOfPlayers = Integer.parseInt((String)mapProps.get("nbrOfPlayers"));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Given map's nbrOfPlayers value "
					+ mapProps.get("nbrOfPlayers") + " is incorrect. Should be a number 0 <= 10");
		}
			
		if(nbrOfPlayers < 0 || nbrOfPlayers > 10) {
			throw new IllegalArgumentException("Given nbrOfPlayers value " +
					+ nbrOfPlayers + " is incorrect. Should be a number 0 <= 10");
		}
		
		int[][] startingPositions = new int[nbrOfPlayers][2];
		
		for(int i = 1; i <= nbrOfPlayers ; i++) {
			if(!mapProps.containsKey("playerStart" + i)) {
				throw new IllegalArgumentException("Given map does not contain"
						+ " starting position playerStart" + i + " for player " + i);
			}
			
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
	
	public Pixmap getMiniMap() {
		return miniMap;
	}
}