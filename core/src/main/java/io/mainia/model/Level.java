package io.mainia.model;

import java.util.List;

public record Level(float speed,
                    List<List<Note>> notes,
                    short columnCount,
                    float length,
                    String musicFilename,
                    float startTime,
                    float healthAmount,
                    boolean isHealthStatic,
                    String resultLocation) {

}
