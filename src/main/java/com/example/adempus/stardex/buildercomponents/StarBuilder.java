package com.example.adempus.stardex.buildercomponents;

import com.example.adempus.stardex.starproperties.Constellation;
import com.example.adempus.stardex.starproperties.Star;
import com.example.adempus.stardex.starproperties.StellarClassification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class is responsible for building a new star object from given JSON data.
 * JSON values are parsed and inserted into respective variables for initialization
 * of a star object.
 * */

public class StarBuilder {
    // values for star name, spectral class and constellation.
    private String nameVal, desigVal, spkVal, spkClassVal, lumVal;
    private String typeDescription,
            constellation;

    // values for star designation, distance (LY), apparent magnitude, declination,
    // right ascension, radius, mass and radial velocity;
    private double LYdistVal, magVal, effectiveTempKVal,
            deVal, raVal, radiusVal, massVal, radVal;

    private int starColorVal;

    public StarBuilder() { }

    /**
     * JSON attributes are subject to change as API evolves. If such is the case, simply crate (or remove)
     * a new value for the attribute in the JsonKeyConstants enum, create a new global variable of data type
     * pertaining to the JSON in this class, create a method to initialize that variable, and invoke that method
     * in the method below.
     *
     * @param starData is the JSON object containing all the data retrieved from the API call. This is the data
     *                   that will be parsed.
     * */
    public void initStarAttributesFromJSON(JSONObject starData) throws JSONException, NullPointerException
    {
        setNameVal(starData.getString(JsonKeyConstants.NAME.getJSONKey()));
        setSpectrumVal(starData.getString(JsonKeyConstants.SPK.getJSONKey()));
        setConstellation(starData.getString(JsonKeyConstants.CON.getJSONKey()));
        setDesignationVal(starData.getString(JsonKeyConstants.DESIG.getJSONKey()));
        setEffectiveTempKVal(starData.getDouble(JsonKeyConstants.TEFF.getJSONKey()));
        setLYdistanceVal(starData.getDouble(JsonKeyConstants.DIST.getJSONKey()));
        setApparentMagnitudeVal(starData.getDouble(JsonKeyConstants.MAG.getJSONKey()));
        setDeclinationVal(starData.getDouble(JsonKeyConstants.DE.getJSONKey()));
        setRightAscensionVal(starData.getDouble(JsonKeyConstants.RA.getJSONKey()));
        setRadiusVal(starData.getDouble(JsonKeyConstants.RADIUS.getJSONKey()));
        setMassVal(starData.getDouble(JsonKeyConstants.MASS.getJSONKey()));
        setRadialVelocityVal(starData.getDouble(JsonKeyConstants.RAD.getJSONKey()));
    }

    public void setNameVal(String nameVal) {
        this.nameVal = nameVal;
    }

    public void setSpectrumVal(String spkVal) {
        this.spkClassVal = spkVal.substring(0, 1);
        List<String> lumConstants = StellarClassification.getLuminosityConstants();
        boolean match = false;

        for (int i = 0 ; i < lumConstants.size() && !match ; i++) {
            String constant = lumConstants.get(i);
            switch (constant) {
                case "III":
                    match = ((spkVal.length() -
                            spkVal.replace("I", "").length()) == 3);
                    break;
                case "II":
                    match = ((spkVal.length() -
                            spkVal.replace("I", "").length()) == 2);
                    break;
                default :
                    match = Pattern.compile(constant).matcher(spkVal).find();
                    break;
            }
            if (match) {
                this.lumVal = lumConstants.get(i);
            }
        }
        setTypeDescription(spkClassVal, lumVal);
        setStarColor(spkClassVal);
    }

    public void setConstellation(String constellation) {
        try {
            this.constellation = Constellation
                    .getConstellation(constellation).toString();
        } catch (IllegalArgumentException IAEx) {
            this.constellation = null;
        }
    }

    public void setStarColor(String spkClassVal) {
        this.starColorVal = StellarClassification.valueOf(spkClassVal).getColorValue();
    }

    public void setTypeDescription(String spkClassVal, String lumVal)
    {
        String genericStarColor = StellarClassification.valueOf(spkClassVal).getGenericColor();
        typeDescription = (lumVal != null ?
                genericStarColor + " " + StellarClassification.valueOf(lumVal).getLuminosityDescription()
                : "Type-"+spkClassVal);
    }

    public void setDesignationVal(String desigVal) {
        this.desigVal = desigVal;
    }

    public void setLYdistanceVal(double LYdistVal) {
        this.LYdistVal = LYdistVal;
    }

    public void setApparentMagnitudeVal(double magVal) {
        this.magVal = magVal;
    }

    public void setEffectiveTempKVal(double effectiveTempKVal) {
        this.effectiveTempKVal = effectiveTempKVal;
    }

    public void setDeclinationVal(double deVal) {
        this.deVal = deVal;
    }

    public void setRightAscensionVal(double raVal) {
        this.raVal = raVal;
    }

    public void setRadiusVal(double radiusVal) {
        this.radiusVal = radiusVal;
    }

    public void setMassVal(double massVal) {
        this.massVal = massVal;
    }

    public void setRadialVelocityVal(double radVal) {
        this.radVal = radVal;
    }

    public Star createStar() {
        return new Star(nameVal, constellation, desigVal, "", radiusVal,
                massVal, magVal, 0.0, 0.0, effectiveTempKVal, raVal, deVal, LYdistVal,
                0.0, radVal, starColorVal, spkVal, spkClassVal, lumVal, typeDescription);
    }
}