package com.ankrya.doomsgreats.client.particle.base.advanced;

import com.ankrya.doomsgreats.client.particle.base.AdvancedParticleBase;
import com.ankrya.doomsgreats.client.particle.base.ParticleRibbon;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;

public class RibbonComponent extends ParticleComponent {
    int length;
    ParticleType<? extends RibbonParticleData> ribbon;
    double yaw;
    double pitch;
    double roll;
    double scale;
    double r;
    double g;
    double b;
    double a;
    boolean faceCamera;
    boolean emissive;
    ParticleComponent[] components;

    public RibbonComponent(ParticleType<? extends RibbonParticleData> particle, int length, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, boolean faceCamera, boolean emissive, ParticleComponent[] components) {
        this.length = length;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.emissive = emissive;
        this.faceCamera = faceCamera;
        this.components = components;
        this.ribbon = particle;
    }

    public void init(AdvancedParticleBase particle) {
        super.init(particle);
        if (particle != null) {
            ParticleComponent[] newComponents = new ParticleComponent[this.components.length + 2];
            System.arraycopy(this.components, 0, newComponents, 0, this.components.length);
            newComponents[this.components.length] = new AttachToParticle(particle);
            newComponents[this.components.length + 1] = new Trail();
            ParticleRibbon.spawnRibbon(particle.getWorld(), this.ribbon, this.length, particle.getPosX(), particle.getPosY(), particle.getPosZ(), 0.0, 0.0, 0.0, this.faceCamera, this.yaw, this.pitch, this.roll, this.scale, this.r, this.g, this.b, this.a, 0.0, (double)(particle.getLifetime() + this.length), this.emissive, newComponents);
        }

    }

    private static class AttachToParticle extends ParticleComponent {
        AdvancedParticleBase attachedParticle;

        public AttachToParticle(AdvancedParticleBase attachedParticle) {
            this.attachedParticle = attachedParticle;
        }

        public void init(AdvancedParticleBase particle) {
            super.init(particle);
            this.attachedParticle.ribbon = (ParticleRibbon) particle;
        }
    }

    public static class Trail extends ParticleComponent {
        public Trail() {
        }

        public void postUpdate(AdvancedParticleBase particle) {
            if (particle instanceof ParticleRibbon ribbon) {
                for(int i = ribbon.positions.length - 1; i > 0; --i) {
                    ribbon.positions[i] = ribbon.positions[i - 1];
                    ribbon.prevPositions[i] = ribbon.prevPositions[i - 1];
                }

                ribbon.positions[0] = new Vec3(ribbon.getPosX(), ribbon.getPosY(), ribbon.getPosZ());
                ribbon.prevPositions[0] = ribbon.getPrevPos();
            }

        }
    }

    public static class PanTexture extends ParticleComponent {
        float startOffset = 0.0F;
        float speed = 1.0F;

        public PanTexture(float startOffset, float speed) {
            this.startOffset = startOffset;
            this.speed = speed;
        }

        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            if (particle instanceof ParticleRibbon ribbon) {
                float time = (ribbon.getAge() - 1.0F + partialTicks) / (float)ribbon.getLifetime();
                float t = (this.startOffset + time * this.speed) % 1.0F;
                ribbon.texPanOffset = (ribbon.getMaxUPublic() - ribbon.getMinUPublic()) / 2.0F * t;
            }

        }
    }

    public static class BeamPinning extends ParticleComponent {
        private final Vec3[] startLocation;
        private final Vec3[] endLocation;

        public BeamPinning(Vec3[] startLocation, Vec3[] endLocation) {
            this.startLocation = startLocation;
            this.endLocation = endLocation;
        }

        public void postUpdate(AdvancedParticleBase particle) {
            if (particle instanceof ParticleRibbon ribbon && this.validateLocation(this.startLocation) && this.validateLocation(this.endLocation)) {
                ribbon.setPos(this.startLocation[0].x(), this.startLocation[0].y(), this.startLocation[0].z());
                Vec3 increment = this.endLocation[0].subtract(this.startLocation[0]).scale((double)(1.0F / (float)(ribbon.positions.length - 1)));

                for(int i = 0; i < ribbon.positions.length; ++i) {
                    Vec3 newPos = this.startLocation[0].add(increment.scale((double)i));
                    ribbon.prevPositions[i] = ribbon.positions[i] == null ? newPos : ribbon.positions[i];
                    ribbon.positions[i] = newPos;
                }
            }

        }

        private boolean validateLocation(Vec3[] location) {
            return location != null && location.length >= 1 && location[0] != null;
        }
    }

    public static class PropertyOverLength extends ParticleComponent {
        private final ParticleComponent.AnimData animData;
        private final EnumRibbonProperty property;

        public PropertyOverLength(EnumRibbonProperty property, ParticleComponent.AnimData animData) {
            this.animData = animData;
            this.property = property;
        }

        public float evaluate(float t) {
            return this.animData.evaluate(t);
        }

        public EnumRibbonProperty getProperty() {
            return this.property;
        }

        public static enum EnumRibbonProperty {
            RED,
            GREEN,
            BLUE,
            ALPHA,
            SCALE;

            private EnumRibbonProperty() {
            }
        }
    }
}
