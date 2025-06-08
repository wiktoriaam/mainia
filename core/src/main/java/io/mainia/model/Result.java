package io.mainia.model;

public record Result(int score,
                     int noOfPerfects,
                     int noOfGreats,
                     int noOfOk,
                     int noOfMisses) {

}