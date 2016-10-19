package com.gellert.guessthenumber.model;

/**
 * Created by gellert on 2016. 10. 16..
 */

public class SmartModel implements Model {
    int min = 1;
    int max;

    public SmartModel(int max) {
        this.max = max;
    }

    @Override
    public int isSolution(int i) {
        if(max - min <= 1) {
            if (max == i) {
                return i;
            }
            else{
                return 0;
            }
        } else {
            return -1;
        }
    }

    @Override
    public boolean question(int i) {
        if(i >= max) {return true;}
        else if(i <= min) {return false;}

        else if(i - min > max - i){
            max = i;
            return true;
        } else {
            min = i;
            return false;
        }
    }
}
