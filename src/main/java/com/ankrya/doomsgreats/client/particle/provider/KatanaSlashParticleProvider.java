package com.ankrya.doomsgreats.client.particle.provider;

import com.ankrya.doomsgreats.client.particle.KatanaSlashParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class KatanaSlashParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public KatanaSlashParticleProvider(SpriteSet spriteSet) {
        this.sprites = spriteSet;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                   double x, double y, double z,
                                   double xSpeed, double ySpeed, double zSpeed) {
        return new KatanaSlashParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
    }
}
