package com.redfischer.rainy.ui;

import android.graphics.Color;

import com.redfischer.rainy.R;

import java.util.Random;

/**
 * Created by Adecrown on 23/06/15.
 */
public class ColorPicker {

    public String[] mColors = {"#39add1",
            "#3079ab",
            "#f9845b",
            "#FFD800",
            "#587058",
            "#587498",
            "#E86850"};

    public int getColor()
    {


        String Colors ="";

        //Randomly select a fact
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);



        Colors = mColors[randomNumber];
        int colorAsInt = Color.parseColor(Colors);

        return colorAsInt;
    }
}
