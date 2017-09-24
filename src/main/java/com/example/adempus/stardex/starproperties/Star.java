package com.example.adempus.stardex.starproperties;

public class Star {
    private String name, constellationName, constellationAbbr, designation,
            hemisphere, spectralClass, luminosityClass, typeDescription;

    private double solarRadius, solarMass, distanceInLY, distanceInPC, appearentMag, absoluteMag, luminosity, effectiveTempKelvin,
                   rightAscension, declination, radialVelocity;
    private int colorValue;
    private String constellation;
    private StellarClassification stellarClass;

    public Star() {
        this("");
    }

    public Star(String name) {
        this.name = name;
    }

    // missing values: absolute magnitude, luminosity, temperature in K, distance PC,
    public Star(String name, String constellation, String designation,
                String hemisphere, double solarRadius, double solarMass,
                double apparentMag, double absoluteMag, double luminosity,
                double effectiveTempKelvin, double rightAscension, double declination,
                double distanceInLY , double distanceInPC, double radialVelocity,
                int colorValue, String stellarClass, String spectralClass,
                String luminosityClass, String typeDescription)
    {
        this.name = name;
        this.constellation = constellation;
        this.designation = designation;
        this.hemisphere = hemisphere;
        this.solarRadius = solarRadius;
        this.solarMass = solarMass;
        this.distanceInLY = distanceInLY;
        this.distanceInPC = distanceInPC;
        this.radialVelocity = radialVelocity;
        this.appearentMag = apparentMag;
        this.absoluteMag = absoluteMag;
        this.luminosity = luminosity;
        this.effectiveTempKelvin = effectiveTempKelvin;
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.spectralClass = spectralClass;
        this.luminosityClass = luminosityClass;
        this.typeDescription = typeDescription;
        this.colorValue = colorValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConstellationName() {
        return constellationName;
    }

    public double getSolarRadius() {
        return solarRadius;
    }

    public void setSolarRadius(double solarRadius) {
        this.solarRadius = solarRadius;
    }

    public double getSolarMass() {
        return solarMass;
    }

    public void setSolarMass(double solarMass) {
        this.solarMass = solarMass;
    }

    public double getDistanceInLY() {
        return distanceInLY;
    }

    public void setDistanceInLY(double distanceInParsec) {
        this.distanceInLY = distanceInParsec;
    }

    public double getEffectiveTempK() {
        return effectiveTempKelvin;
    }

    public void setEffectiveTempK(double effectiveTempKelvin) {
        this.effectiveTempKelvin= effectiveTempKelvin;
    }

    public String getTypeDescription() {
        return this.typeDescription + " Star";
    }

    public String getData() {
        return  "\nDesignation: " + designation
                +"\nConstellation: " + constellation
                +"\nDistance: " + distanceInLY + " ly"
                +"\nApparent Magnitude: " + appearentMag
                +"\nEffective Temperature: " + effectiveTempKelvin + " K"
                +"\nMass: " + solarMass + " sol"
                +"\nRadius: " + solarRadius + " sol"
                +"\nCoordinates: ra " + rightAscension + " de " + declination
                +"\nRadial Velocity: " + radialVelocity
                +"\nClass: " + spectralClass
                +"\nLum Class: " + luminosityClass;
    }

    public int getColorValue() {
        return this.colorValue;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    @Override
    public String toString() {
        return  "\n"+ getName()
                +"\n --"
                +"\n"+  getTypeDescription()
                +"\n" + getData();


    }
}
