package UAW.entities.bullet;

import UAW.audiovisual.*;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.*;
import mindustry.graphics.Layer;
import mindustry.world.blocks.environment.Floor;

import static mindustry.Vars.tilesize;

/**
 * Water based bullet that deals extra splashDamage based one enemy hitSize
 */
public class TorpedoBulletType extends BulletType {
	/**
	 * Scaling splashDamage based on enemy hitSize
	 */
	public float hitSizeDamageScl = 1.5f;
	/**
	 * Maximum enemy hitSize threshold
	 */
	public float maxEnemyHitSize = 60f;
	/**
	 * Drag in non Deep liquid terrain
	 */
	public float deepDrag = -0.005f;
	public float rippleInterval = 7f;
	public Effect rippleEffect = UAWFx.torpedoRippleTrail;
	public Color shootColor = UAWPal.waterBack;

	public TorpedoBulletType(float speed, float damage) {
		super(speed, damage);
		layer = Layer.debris;
		homingPower = 0.035f;
		homingRange = 20 * tilesize;
		hitShake = 24;
		knockback = 8f;
		hitSize = 16f;
		collideTerrain = collideFloor = true;
		keepVelocity = collidesAir = absorbable = hittable = reflectable = false;
		lightColor = hitColor;
		trailLength = 36;
		trailWidth = trailLength / 13f;
		trailInterval = 0.2f;
		trailRotation = true;
		hitEffect = new MultiEffect(
			Fx.smokeCloud,
			Fx.blastExplosion,
			UAWFx.torpedoRippleHit
		);
		despawnEffect = UAWFx.torpedoRippleHit;
		status = StatusEffects.slow;
		statusDuration = 3 * 60;
		hitSoundVolume = 4f;
		hitSound = Sounds.explosionbig;
	}

	@Override
	public void init(Bullet b) {
		super.init(b);
		Floor floor = Vars.world.floorWorld(b.x, b.y);
		shootColor = trailColor = floor.mapColor.cpy().mul(1.5f);
	}

	@Override
	public void drawTrail(Bullet b) {
		if (trailLength > 0 && b.trail != null) {
			float z = Draw.z();
			Draw.z(layer + 0.001f);
			b.trail.draw(trailColor, trailWidth);
			Draw.z(z);
		}
	}

	@Override
	public void update(Bullet b) {
		Floor floor = Vars.world.floorWorld(b.x, b.y);
		/*
		 * Taken from Betamindy - NavalBulletType
		 * @author Sk7725
		 */
		if (Time.time - b.fdata > rippleInterval) {
			b.fdata = Time.time;
			rippleEffect.at(b.x, b.y, hitSize / 3f, trailColor, layer - 10);
		}
		if (floor.isDeep()) {
			b.vel.scl(Math.max(1f - deepDrag * Time.delta, 0.01f));
		}
		super.update(b);
	}

	@Override
	public void hitEntity(Bullet b, Hitboxc entity, float health) {
		float damageReduction = 1;
		if (entity.hitSize() > maxEnemyHitSize) {
			damageReduction = ((entity.hitSize() * 10f) / 100);
		} else if (entity.hitSize() < maxEnemyHitSize / 3) {
			damageReduction = ((entity.hitSize() * 20f) / 100);
		}
		if (entity instanceof Healthc h) {
			h.damage((b.damage * ((entity.hitSize() * hitSizeDamageScl) / 100)) / damageReduction);
		}
		super.hitEntity(b, entity, health);
	}

	@Override
	public void removed(Bullet b) {
		if (trailLength > 0 && b.trail != null && b.trail.size() > 0) {
			UAWFx.torpedoTrailFade.at(b.x, b.y, trailWidth, trailColor, b.trail.copy());
		}
	}
}