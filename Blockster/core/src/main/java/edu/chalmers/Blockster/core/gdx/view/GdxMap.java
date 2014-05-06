package edu.chalmers.Blockster.core.gdx.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.Blockster.core.objects.Block;
import edu.chalmers.Blockster.core.objects.BlockMap;

/**
 * 
 * @author Mia
 * @author Eric Bjuhr
 *
 */
public class GdxMap extends TiledMap implements BlockMap {

	private TiledMapTileLayer blockLayer;
	private Map<Block, BlockView> blocks;

	public GdxMap (TiledMap map) {
		super();

		blockLayer = (TiledMapTileLayer) 
				map.getLayers().get(0);
		blocks = new HashMap<Block, BlockView>();

		getLayers().add(blockLayer);

		for (int x = 0; x < blockLayer.getWidth(); x++) {
			for (int y = 0; y < blockLayer.getHeight(); y++) {
				Cell cell = blockLayer.getCell(x, y);
				if (cell != null) {
					TiledMapTile tile = cell.getTile();
					Block block = new Block(x, y, this);
					BlockView bView = new BlockView(block, tile);
					
					cell.setTile(bView);
					blockLayer.setCell(x, y, cell);
					blocks.put(block, bView);
					
					
					Iterator<String> properties = tile.getProperties().getKeys();
					while(properties.hasNext()) {
						String property = properties.next();
						block.setProperty(property);
					}
				}
			}
		}
	}

	@Override
	public float getBlockWidth(){
		return blockLayer.getTileWidth();
	}

	@Override
	public float getBlockHeight(){
		return blockLayer.getTileHeight();
	}
	
	@Override
	public Block getBlock(int x, int y){
		for (Block block : blocks.keySet()) {
			if ((int) block.getX() == x && (int) block.getY() == y) {
				return block;
			}
		}
		return null;
	}
	
	@Override
	public void setBlock(int x, int y, Block block){
		Cell cell = new Cell();
		cell.setTile(blocks.get(block));
		
		blockLayer.setCell(x, y, cell);
	}

	@Override
	public int getHeight() {
		return blockLayer.getHeight();
	}

	@Override
	public int getWidth() {
		return blockLayer.getWidth();
	}
	
	/**
	 * Removes block from layer.
	 * @param block the block to remove
	 */
	public void removeBlock(BlockView block) {
		blockLayer.setCell((int)block.getX(), (int)block.getY(), null);
	}
	
	/**
	 * Inserts block into layer.
	 * @param block the block to insert
	 */
	public void insertBlock(Block block) {
		try {
			int x = Math.round(block.getX());
			int y = Math.round(block.getY());
			
			if (blockLayer.getCell(x, y) == null) {
				blockLayer.setCell(x, y, new Cell());
			}
			
			if (blockLayer.getCell(x, y).getTile() == null) {
				setBlock(x, y, block);
			}
		} catch (NullPointerException e) {
			Gdx.app.log("Layer", "NullPointer");
		}
	}

	@Override
	public boolean hasBlock(int x, int y) {
		return blockLayer.getCell(x, y) != null 
				&& blockLayer.getCell(x, y).getTile() != null
				&& blockLayer.getCell(x, y).getTile().getTextureRegion() != null;
	}
	
	public Set<Block> getBlocks() {
		return blocks.keySet();
	}
	
	public Collection<BlockView> getBlockViews() {
		return blocks.values();
	}
}
