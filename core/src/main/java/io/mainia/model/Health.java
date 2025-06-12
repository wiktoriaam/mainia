package io.mainia.model;

public interface Health {
    float currentHealth();
    void updateHealth(HitResult hitResult, boolean nomiss, boolean perfect);

}
