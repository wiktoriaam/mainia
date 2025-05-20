package io.mainia.model;

public interface Note {
    float getHitTime();
    HitResult hitCheck(float time);
}
