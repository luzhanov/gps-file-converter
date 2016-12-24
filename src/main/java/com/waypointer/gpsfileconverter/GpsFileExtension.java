package com.waypointer.gpsfileconverter;

/**
 * @author luzhanov
 */
public enum GpsFileExtension {

    GPX("gpx"), KML("kml"), WPT("wpt");

    private String extension;


    private GpsFileExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static GpsFileExtension getFileExtension(String value) {
        for (GpsFileExtension fext : GpsFileExtension.values()) {
            if (fext.getExtension().equalsIgnoreCase(value)) {
                return fext;
            }
        }
        return null;
    }

}
