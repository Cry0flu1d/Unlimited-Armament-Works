package UAW.type;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.Time;
import mindustry.gen.Unit;

public class Rotor {
    public final String name;
    public TextureRegion bladeRegion, topRegion;

    public float x = 0f;
    public float y = 0f;
    public float rotationSpeed = 12;

    public int bladeCount = 4;

    public Rotor(String name) {
        this.name = name;
    }

    public void load() {
        bladeRegion = Core.atlas.find(name);
        topRegion = Core.atlas.find(name + "-top");
    }

    public void draw(Unit unit) {
        float deathRotorSpeedScl = 1;
        float rx = unit.x + Angles.trnsx(unit.rotation - 90, x, y);
        float ry = unit.y + Angles.trnsy(unit.rotation - 90, x, y);

        if (unit.health() < 0 || unit.dead) {
            deathRotorSpeedScl = Mathf.lerpDelta(0.9f, 0.5f, 0.001f);
        }

        for (int i = 0; i < bladeCount; i++) {
            float angle = ((i * 360f / bladeCount + ((Time.time * rotationSpeed) * deathRotorSpeedScl)) % 360);
            Draw.rect(bladeRegion, rx, ry, bladeRegion.width * Draw.scl, bladeRegion.height * Draw.scl, angle);
        }
        Draw.rect(topRegion, rx, ry, topRegion.width * Draw.scl, topRegion.height * Draw.scl, unit.rotation - 90);
    }
}
