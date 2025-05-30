package io.mainia.model;

public interface Note {
    float hitTime();
    HitResult hitCheck(float time);
}
