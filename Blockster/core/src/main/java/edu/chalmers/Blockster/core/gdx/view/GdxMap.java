package edu.chalmers.Blockster.core.gdx.view;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import edu.chalmers.Blockster.core.util.AnimationState;

/**
 * 
 * @author Mia
 * @author Eric Bjuhr
 *
 */
public class GdxMap extends TiledMap implements BlockMap {

	private TiledMapTileLayer blockLayer;
	private Map<Block, BlockView> blocks;
	private Set<Block> activeBlocks;

	public GdxMap (TiledMap map) {
		super();

		blockLayer = (TiledMapTileLayer) 
				map.getLayers().get(0);
		blocks = new HashMap<Block, BlockView>();
		activeBlocks = new HashSet<Block>();

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
		Cell cell = blockLayer.getCell(x, y);
		if (cell != null) {
			BlockView bView = (BlockView) cell.getTile();
			return bView.getBlock();
		}
		return null;
	}
	
	@Override
	public void setBlock(int x, int y, Block block){
		if (block != null) {
			Cell cell = new Cell();
			cell.setTile(blocks.get(block));
			blockLayer.setCell(x, y, cell);
		} else {
			System.out.println("Removing cell at ("+x+", "+y+")");
			blockLayer.setCell(x, y, null);
		}
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
	 * Inserts block into layer.
	 * @param block the block to insert
	 */
	public void insertBlock(Block block) {
		try {
			int x = Math.round(block.getOriginX());
			int y = Math.round(block.getOriginY());
			
			setBlock(x, y, block);
			System.out.println("Inserted " + block);
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
	
	public BlockView getBlockView(Block block) {
		if (blocks.containsKey(block)) {
			return blocks.get(block);
		} else {
			return null;
		}
	}
	
	public Collection<BlockView> getBlockViews() {
		return blocks.values();
	}

	@Override
	public Set<Block> getActiveBlocks() {
		return activeBlocks;
	}

	@Override
	public void insertFinishedBlocks() {
		Set<Block> doneBlocks = new HashSet<Block>();
		for (Block block : activeBlocks) {
			if (block.getAnimationState().isDone()) {
				insertBlock(block);
				doneBlocks.add(block);
				block.setAnimationState(AnimationState.NONE);
			}
		}
		activeBlocks.removeAll(doneBlocks);
		if (activeBlocks.size() != 0)
			System.out.println(activeBlocks.size());
	}

	@Override
	public void addActiveBlock(Block block) {
		activeBlocks.add(block);
		System.out.println("Adding active "+block);
	}

	@Override
	public void updateActiveBlocks(float deltaTime) {
		for (Block block : activeBlocks) {
			block.getAnimationState().updatePosition(deltaTime);
			System.out.println("Updating "+block);
			if(block.getAnimationState().isDone()) {
				System.out.println("Animation on "+block+" is done");
				block.moveToNextPosition();
			}
		}
	}
}
