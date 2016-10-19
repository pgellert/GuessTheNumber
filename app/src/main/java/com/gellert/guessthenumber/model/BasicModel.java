package com.gellert.guessthenumber.model;

import java.util.Random;

/**
 * Created by gellert on 2016. 10. 16..
 */

public class BasicModel implements Model {
    private int number;

    public BasicModel(int max) {
        number = (new Random()).nextInt(max) + 1;
    }

    @Override
    public int isSolution(int i) {
        return i == number ? i : 0;
    }

    @Override
    public boolean question(int i) {
        return number <= i;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
