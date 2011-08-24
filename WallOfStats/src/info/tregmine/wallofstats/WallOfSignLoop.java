package info.tregmine.wallofstats;

import java.sql.ResultSet;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class WallOfSignLoop  implements Runnable{
	World world;

	public WallOfSignLoop(World world) {
		this.world = world;
	}

	public void run() {
		info.tregmine.database.Mysql mysql = new info.tregmine.database.Mysql();
		mysql.connect();
		if (getBlockState(528, 20, -184) instanceof Sign) {
			Sign sign = (Sign)getBlockState(528, 20, -184);
			String SQL = "SELECT COUNT(status) as count FROM stats_blocks WHERE status = 1";
			try {
				mysql.statement.executeQuery(SQL);
				ResultSet rs = mysql.statement.getResultSet();
				rs.first();
				sign.setLine(0, "\u00A74PLACED BLOCKS");
				sign.setLine(1, "\u00A71" + rs.getInt("count"));
				rs.close();

				String SQL1 = "SELECT COUNT(status) as count FROM stats_blocks WHERE status = 0";
				mysql.statement.executeQuery(SQL1);
				ResultSet rs1 = mysql.statement.getResultSet();
				rs1.first();
				sign.setLine(2, "\u00A74DELETED BLOCKS");
				sign.setLine(3, "\u00A71" + rs1.getInt("count"));
				sign.update();
				rs.close();


			} catch (Exception e) {
				sign.setLine(0, "\u00A74PLACED BLOCKS");
				sign.setLine(1, "- ERROR -");
				sign.setLine(2, "\u00A74DELETED BLOCKS");
				sign.setLine(3, "- ERROR -");
				sign.update();
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (getBlockState(528, 21, -184) instanceof Sign) {
			Sign sign = (Sign)getBlockState(528, 21, -184);
			String SQL = "SELECT COUNT(status) as count FROM stats_blocks";
			try {
				mysql.statement.executeQuery(SQL);
				ResultSet rs = mysql.statement.getResultSet();
				rs.first();
				sign.setLine(0, "\u00A74TOTAL BLOCKS");
				sign.setLine(1, "\u00A71" + rs.getInt("count"));
				sign.setLine(2, "\u00A74WARPS");

				String SQL1 = "SELECT COUNT(name) as count FROM warps";
				mysql.statement.executeQuery(SQL1);
				ResultSet rs1 = mysql.statement.getResultSet();
				rs1.first();
				sign.setLine(3, "\u00A71" + rs1.getInt("count"));
				rs1.close();
				sign.update();
				rs.close();
			} catch (Exception e) {
				sign.setLine(0, "TOTAL BLOCKS");
				sign.setLine(1, "- ERROR -");
				sign.setLine(2, "");
				sign.setLine(3, "");
				sign.update();
			}
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (getBlockState(529, 21, -183) instanceof Sign) {
			Sign sign = (Sign)getBlockState(529, 21, -183);
			try {
				String SQLplayers = "SELECT COUNT(player) as count FROM user";
				mysql.statement.executeQuery(SQLplayers);
				ResultSet rsplayer = mysql.statement.getResultSet();
				rsplayer.first();
				sign.setLine(0, "\u00A74UNIQE VISITORS");
				sign.setLine(1, "\u00A71" + rsplayer.getInt("count"));
				rsplayer.close();

				String SQLBanned = "SELECT COUNT(value) as count FROM user_settings WHERE `key`='banned' AND `value`='true'";
				mysql.statement.executeQuery(SQLBanned);
				ResultSet rsbanned = mysql.statement.getResultSet();
				rsbanned.first();
				sign.setLine(2, "\u00A74BANNED");
				sign.setLine(3, "\u00A71" + rsbanned.getInt("count"));
				sign.update();
				rsbanned.close();
			} catch (Exception e) {
				e.printStackTrace();
				sign.setLine(0, "\u00A74UNIQE VISITORS");
				sign.setLine(1, "- ERROR -");
				sign.setLine(2, "BANNED");
				sign.setLine(2, "\u00A74BANNED");
				sign.update();
			}
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		if (getBlockState(529, 20, -183) instanceof Sign) {
			Sign sign = (Sign)getBlockState(529, 20, -183);
			try {
				sign.setLine(0, "\u00A74TRUSTED");

				String SQL1 = "SELECT COUNT(value) as count FROM user_settings WHERE `key`='trusted' AND `value`='true'";
				mysql.statement.executeQuery(SQL1);
				ResultSet rs1 = mysql.statement.getResultSet();
				rs1.first();
				sign.setLine(1, "\u00A71" + rs1.getInt("count"));
				rs1.close();

				String SQL2 = "SELECT COUNT(value) as count FROM user_settings WHERE `key`='donator' AND `value`='true'";
				mysql.statement.executeQuery(SQL2);
				ResultSet rs2 = mysql.statement.getResultSet();
				rs2.first();
				sign.setLine(2, "\u00A74DONATORS");
				sign.setLine(3, "\u00A71" + rs2.getInt("count"));
				rs2.close();

				sign.update();
			} catch (Exception e) {
				e.printStackTrace();
				sign.setLine(0, "\u00A74TRUSTED");
				sign.setLine(1, "- ERROR -");
				sign.setLine(3, "\u00A74DONATORS");
				sign.setLine(3, "- ERROR -");
				sign.update();
			}
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mysql.close();		
	}


	private BlockState getBlockState(int x, int y, int z) {
		Block block = world.getBlockAt(x,y,z);
		return block.getState();
	}

}
