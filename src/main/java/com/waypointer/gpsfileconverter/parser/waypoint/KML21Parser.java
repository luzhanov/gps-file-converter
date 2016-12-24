package com.waypointer.gpsfileconverter.parser.waypoint;


import com.waypointer.gpsfileconverter.parser.FileParser;

import java.io.BufferedInputStream;
import java.io.IOException;

public class KML21Parser extends KML22Parser {

    @Override
    public boolean isParsableFile(BufferedInputStream is) throws IOException {
        return FileParser.isParsableFileCheck(is, "http://earth.google.com/kml/2.1");
    }
}
