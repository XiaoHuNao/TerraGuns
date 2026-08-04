package org.confluence.terra_guns.api.client.animation;

/** Interpolation functions used between two hand-animation keyframes. */
public enum Easing {
    LINEAR {
        @Override
        public float apply(float progress) {
            return progress;
        }
    },
    EASE_IN {
        @Override
        public float apply(float progress) {
            return progress * progress;
        }
    },
    EASE_OUT {
        @Override
        public float apply(float progress) {
            float inverse = 1.0F - progress;
            return 1.0F - inverse * inverse;
        }
    },
    EASE_IN_OUT {
        @Override
        public float apply(float progress) {
            return progress < 0.5F
                    ? 2.0F * progress * progress
                    : 1.0F - (float) Math.pow(-2.0F * progress + 2.0F, 2.0D) / 2.0F;
        }
    },
    SMOOTH_STEP {
        @Override
        public float apply(float progress) {
            return progress * progress * (3.0F - 2.0F * progress);
        }
    };

    public abstract float apply(float progress);
}
