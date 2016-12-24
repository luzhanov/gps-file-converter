package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.parser.FileParser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KML22ParserTest {

    @Test
    public void testFileParsing2() throws Exception {
        String path = getClass().getResource("/parsing/monuments_ukr.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        FileParser<GpsPoint> parser = new KML22Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(2483);
    }

    @Test
    public void testFileParsing() throws Exception {
        String path = getClass().getResource("/parsing/sample_ge_kml22_1.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        FileParser<GpsPoint> parser = new KML22Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(14);
    }

    @Test
    public void testFileDetection() throws Exception {
        String path = getClass().getResource("/parsing/sample_ge_kml22_1.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsPoint> parser = new KML22Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testFileDetection2() throws Exception {
        String path = getClass().getResource("/parsing/monuments_ukr.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsPoint> parser = new KML22Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        String path = getClass().getResource("/parsing/sample_ge_kml22_1.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsPoint> parser = new KML22Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();

        List<GpsPoint> waypoints = parser.parseFile(bis);
        assertThat(waypoints).hasSize(14);
    }

}
