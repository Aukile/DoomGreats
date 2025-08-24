package com.ankrya.doomsgreats.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class KatanaSlashParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public KatanaSlashParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        this.sprites = sprites;
        this.lifetime = 8; // 粒子存在时间(ticks)
        this.quadSize = 1.2F; // 粒子大小
        this.setSpriteFromAge(sprites);

        // 设置颜色 (青蓝色调)
        this.rCol = 0.6F;
        this.gCol = 0.8F;
        this.bCol = 1.0F;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        // 随时间缩小和变透明
        this.quadSize *= 0.9F;
        this.alpha = 1.0F - ((float)this.age / (float)this.lifetime);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        // 使粒子发光
        return 15728880;
    }
}
