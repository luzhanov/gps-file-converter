package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.TestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WPTParserTest {

    @Test
    public void testFileParsing() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi.wpt").getFile();
        FileInputStream fis = new FileInputStream(path);

        WPTParser parser = new WPTParser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(14);

        GpsPoint firstPoint = waypoints.get(0);
        assertThat(firstPoint.getName()).isEqualTo("PRAV-KORDON");
        assertThat(TestUtils.getDate(2012, Calendar.JANUARY, 18, 17, 15, 22)).isEqualTo(
                DateUtils.truncate(firstPoint.getCreatedDate(), Calendar.SECOND));
    }

    @Test
    public void testFileParsingFileWithSpaces() throws Exception {
        String path = getClass().getResource("/parsing/kosmach.wpt").getFile();
        FileInputStream fis = new FileInputStream(path);

        WPTParser parser = new WPTParser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(26);

        GpsPoint firstPoint = waypoints.get(0);
        assertThat(firstPoint.getName()).isEqualTo("001");
        assertThat(TestUtils.getDate(2009, Calendar.MAY, 9, 12, 21, 0)).isEqualTo(
                DateUtils.truncate(firstPoint.getCreatedDate(), Calendar.SECOND));
    }

    @Ignore
    @Test
    public void testFileParsingRus() throws Exception {
        InputStream fis = getClass().getResourceAsStream("/parsing/1.wpt");

        WPTParser parser = new WPTParser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(4);

        GpsPoint firstPoint = waypoints.get(0);
        assertThat(firstPoint.getName()).isEqualTo("PRAV-KORDON");
        assertThat(TestUtils.getDate(2012, Calendar.JANUARY, 18, 17, 15, 22)).isEqualTo(
                DateUtils.truncate(firstPoint.getCreatedDate(), Calendar.SECOND));
    }

    @Test
    public void testFileDetection() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi.wpt").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        WPTParser parser = new WPTParser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi.wpt").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        WPTParser parser = new WPTParser();
        assertThat(parser.isParsableFile(bis)).isTrue();

        List<GpsPoint> waypoints = parser.parseFile(bis);
        assertThat(waypoints).hasSize(14);
    }

    /**
     * 0 -> 12/30/1899 00:00
     * 2.75 -> 1/1/1900 18:00
     * -1.25 -> 12/29/1899 6:00
     * 35065 -> 1/1/1996 0:00
     */
    @Test
    public void testParseUnixTime() {
        assertThat(WPTParser.parseDelphiDateTime(0D)).isEqualTo(TestUtils.getDate(1899, Calendar.DECEMBER, 30, 0, 0, 0));
        assertThat(WPTParser.parseDelphiDateTime(2.75D)).isEqualTo(TestUtils.getDate(1900, Calendar.JANUARY, 1, 18, 0, 0));
        assertThat(WPTParser.parseDelphiDateTime(35065D)).isEqualTo(TestUtils.getDate(1996, Calendar.JANUARY, 1, 0, 0, 0));
        assertThat(WPTParser.parseDelphiDateTime(-1.25D)).isEqualTo(TestUtils.getDate(1899, Calendar.DECEMBER, 29, 6, 0, 0));
    }

}