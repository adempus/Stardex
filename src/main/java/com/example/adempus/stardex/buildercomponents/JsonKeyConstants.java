package com.example.adempus.stardex.buildercomponents;

/**
 * This enum contains a set of constant strings that are mapped to data values,
 * to parse from the json response.
 *
 * JSON Data Keys    starproperties.Star Attributes        data types
 * (Located in     (Located in starproperties.Star)
 * buildercomponents.StarBuilder)
 *  Key               Description          Type
 * "name" :          name                - string
 * "spk":            spectral class      - string
 * "con":            constellation       - string
 * "desig":          designation         - double
 * "dist" :          distance            - double
 * "mag":            apparent magnitude  - double
 * "de":             declination         - double
 * "ra":             right ascension     - double
 * "radius":         radius              - double
 * "mass"            mass                - double
 * "rad":            radial velocity     - double
 */

public enum JsonKeyConstants {
    // some characteristics of stars
    NAME("name", "name"), SPK("spk", "Spectral Class"), CON("con", "starproperties.Constellation"),
    DESIG("desig", "Designation"), DIST("dist", "Distance"), MAG("mag", "Apparent Magnitude"),
    DE("de", "Declination"), RA("ra", "Right Ascension"), RADIUS("radius", "Radius"),
    TEFF("teff", "Effective Temperature"), MASS("mass", "Solar Mass"),
    RAD("rad", "Radial Velocity");

    private String JSONKey, description;

    /** @param JSONKey the object key in the JSON file to parse from JSON.
     * @param description the mapped values of corresponding object keys parsed from JSON.
     **/
    JsonKeyConstants(String JSONKey, String description) {
        this.JSONKey = JSONKey;
        this.description = description;
    }

    public String getJSONKey() {
        return JSONKey;
    }

    public String getDescription() {
        return description;
    }
}