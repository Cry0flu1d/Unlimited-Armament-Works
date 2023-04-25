package UAW.entities.bullet;

import UAW.audiovisual.UAWFx;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;

/**
 * BasicBulletType with automatic adjusting trails
 */
public class TrailBulletType extends BasicBulletType {
	/**
	 * How long is the generated trail based on height multiplied by this
	 * <p> 0 to disable </p>
	 */
	public float trailLengthScale = 1f;
	public float trailWidthScale = 0.28f;

	public TrailBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage);
		this.sprite = bulletSprite;
		trailEffect = Fx.none;
		hitSize = width;
	}

	public TrailBulletType(float speed, float damage) {
		this(speed, damage, "bullet");
	}

	public TrailBulletType() {
		this(0f, 0f, "bullet");
	}

	@Override
	public void init(Bullet b) {
		super.init(b);
		drawSize = Math.max(drawSize, trailLength * speed * 4.5f);
		if (trailLengthScale > 0) {
			autoTrail();
		}
	}

	public void autoTrail() {
		trailRotation = true;
		trailWidth = width * trailWidthScale;
		trailLength = Mathf.round(height * trailLengthScale);
		trailColor = backColor;
	}

	@Override
	public void removed(Bullet b) {
		if (trailLength > 0 && b.trail != null && b.trail.size() > 0) {
			UAWFx.trailFade.at(b.x, b.y, trailWidth, trailColor, b.trail.copy());
		}
	}
}
