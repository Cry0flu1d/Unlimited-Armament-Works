package UAW.world.blocks.production;

import arc.math.Mathf;
import arc.util.Nullable;
import mindustry.entities.Effect;
import mindustry.world.blocks.production.Pump;

public class UAWPump extends Pump {

	@Nullable
	public Effect updateEffect;
	public float updateEffectChance = 1f;

	public UAWPump(String name) {
		super(name);
		placeableLiquid = true;
		squareSprite = false;
		hasPower = false;
	}

	public class UAWPumpBuild extends PumpBuild {

		@Override
		public void updateTile() {
			super.updateTile();
			if (efficiency > 0 && liquidDrop != null) {
				if (Mathf.chance(efficiency * updateEffectChance) && updateEffect != null) {
					updateEffect.at(x + Mathf.range(size / 3f * 4f), y + Mathf.range(size / 3f * 4f));
				}
			}
		}
	}
}
