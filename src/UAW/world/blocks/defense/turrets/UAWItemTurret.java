package UAW.world.blocks.defense.turrets;

import UAW.world.meta.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.meta.Stat;

import static mindustry.Vars.tilesize;

/**
 * Modified version of the vanilla item turret
 * <p>
 * Displays minimum range when selected
 * </p>
 */
public class UAWItemTurret extends ItemTurret {

	public UAWItemTurret(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();
		stats.remove(Stat.ammo);
		stats.add(Stat.ammo, UAWStatValues.ammo(ammoTypes));
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);
		if (minRange > 0) {
			Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, minRange, Pal.lightishOrange);
		}
	}
}
