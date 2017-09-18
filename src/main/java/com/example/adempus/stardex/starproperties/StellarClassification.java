package com.example.adempus.stardex.starproperties;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.example.adempus.stardex.R;
import com.example.adempus.stardex.SearchActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StellarClassification
{
    O(1000000, 1000000, ColorConstants.blue , "Blue"),
    B(10000, 30000, ColorConstants.blue_white, "Blue-White"),
    A(7500, 10000, ColorConstants.white, "White"),
    F(6000, 7500, ColorConstants.yellow_white, "Yellow-White"),
    G(5200, 6000, ColorConstants.yellow, "Yellow"),
    K(3700, 5200, ColorConstants.orange, "Orange"),
    M(2400, 3700, ColorConstants.red, "Red"),
    L(2400, 0, ColorConstants.brown, "Brown"),

    // Luminosity constants
    Ia("High Luminosity Supergiant"), Ib("Supergiant"), II("Luminous Giant"), III("Giant"),
    IV("Subgiant"), V("Main Sequence"), sd("Subdwarf"), D("White Dwarf");

    private double minimumTemp, maximumTemp;
    private String genericColor, luminosityScale;
    private int colorValue;

    StellarClassification(double minTemp, double maxTemp,
            int colorValue, String genericColor)
    {
        this.minimumTemp = minTemp;
        this.maximumTemp = maxTemp;
        this.genericColor = genericColor;
        this.colorValue = colorValue;
    }

    private static class ColorConstants {
        private static Context appContext = SearchActivity.getMainAppContext();
        private final static int blue = ContextCompat.getColor(appContext, R.color.Blue);
        private final static int blue_white = ContextCompat.getColor(appContext, R.color.LightBlue);
        private final static int white = ContextCompat.getColor(appContext, R.color.White);
        private final static int yellow_white = ContextCompat.getColor(appContext, R.color.LightYellow);
        private final static int yellow = ContextCompat.getColor(appContext, R.color.Yellow);
        private final static int orange = ContextCompat.getColor(appContext, R.color.Orange);
        private final static int red = ContextCompat.getColor(appContext, R.color.Red);
        private final static int brown = ContextCompat.getColor(appContext, R.color.Brown);
    }

    StellarClassification(String lumScale) {
        this.luminosityScale = lumScale;
    }

    public String getGenericColor () {
        return genericColor;
    }

    public String getLuminosityScale () {
        return luminosityScale;
    }

    public static List<String> getLuminosityConstants () {
        StellarClassification[] lumConstValues = Arrays.copyOfRange(values(), 8 , 16);
        List<String> luminosityConstants = new ArrayList<>();
        for (StellarClassification sc : lumConstValues) {
            luminosityConstants.add(sc.toString());
        }
        return luminosityConstants;
    }

    public String getLuminosityDescription() {
        return luminosityScale;
    }

    public double getMinimumTemp() {
        return minimumTemp;
    }

    public double getMaximumTemp() {
        return maximumTemp;
    }

    public int getColorValue() {
        return colorValue;
    }
}