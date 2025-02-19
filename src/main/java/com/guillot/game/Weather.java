package com.guillot.game;


public enum Weather {
    SUNNY(0), SNOW(1);

    private int value;

    private Weather(int value) {
        this.value = value;
    }

    public static Weather findByValue(int value) {
        for (Weather weather : Weather.values()) {
            if (weather.value == value) {
                return weather;
            }
        }

        return null;
    }

    public int getValue() {
        return value;
    }
}
