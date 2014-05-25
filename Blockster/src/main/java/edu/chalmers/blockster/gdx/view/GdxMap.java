package edu.chalmers.blockster.gdx.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMapListener;
import edu.chalmers.blockster.core.util.GridMap;

/**
 * 
 * @author Mia
 * @author Eric Bjuhr
 *
 */
public class GdxMap extends TiledMap implements BlockMapListener {

	private final TiledMapTileLayer blockLayer;
	private final Map<Block, BlockView> blocks;

	public GdxMap (GridMap gridMap) {
		super();

		blocks = new HashMap<Block, BlockView>();
		blockLayer = new TiledMapTileLayer(gridMap.getWidth(), 
				gridMap.getHeight(), (int) gridMap.getBlockWidth(), 
				(int) gridMap.getBlockHeight());
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
		
		final BlockView blockView = getBlockView(block);
		final Cell cell = new Cell();
		cell.setTile(blockView);
		cell.setRotation(blockView.getOrthogonalDegrees() / 90);
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
