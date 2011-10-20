package info.tregmine.boxfill;

import info.tregmine.timemachine.TimeMachine;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class TimeMachineFiller extends AbstractFiller
{
	private String param;
	
	public TimeMachineFiller(Block block1, Block block2, int workSize, String param)
	{
		super(block1, block2, workSize);
		
		this.param = param;
	}
	
	@Override
	public void changeBlock(Block block)
	{
		Material item = TimeMachine.Restore(param, block);
		if(item != null) {
			block.setType(item);
		}
	}
	
	@Override
	public void finished()
	{
	}
}
