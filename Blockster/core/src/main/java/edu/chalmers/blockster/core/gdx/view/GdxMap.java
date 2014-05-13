package edu.chalmers.blockster.core.gdx.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.BlockMapListener;

/**
 * 
 * @author Mia
 * @author Eric Bjuhr
 *
 */
public class GdxMap extends TiledMap implements BlockMapListener {

	private final TiledMapTileLayer blockLayer;
	private final Map<Block, BlockView> blocks;

	public GdxMap (BlockMap blockMap) {
		super();

		blocks = new HashMap<Block, BlockView>();
		blockLayer = new TiledMapTileLayer(blockMap.getWidth(), 
				blockMap.getHeight(), (int) blockMap.getBlockWidth(), 
				(int) blockMap.getBlockHeight());
		getLayers().add(blockLayer);
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
	public void blockInserted(Block block) {
		assert blocks.containsKey(block);
		
		BlockView blockView = getBlockView(block);
		Cell cell = new Cell();
		cell.setTile(blockView);
		blockLayer.setCell((int) block.getOriginX(), (int) block.getOriginY(), cell);
	}

	@Override
	public void blockRemoved(Block block) {
		blockLayer.setCell((int) block.getOriginX(), (int) block.getOriginY(), null);
	}
	
	public void createBlockViewReference(Block block, BlockView bView) {
		blocks.put(block, bView);
	}
}
