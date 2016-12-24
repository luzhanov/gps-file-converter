package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.TestUtils;
import com.waypointer.gpsfileconverter.parser.FileParser;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.data.Offset;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KML20ParserTest {

    @Test
    public void testFileParsing() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi_kml20.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        FileParser<GpsPoint> parser = new KML20Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(14);

        GpsPoint firstPoint = waypoints.get(0);
        assertThat(firstPoint.getName()).isEqualTo("PRAV-KORDON");
        assertThat(TestUtils.getDate(2012, Calendar.JANUARY, 18, 14, 15, 22))
                .isEqualTo(DateUtils.truncate(firstPoint.getCreatedDate(), Calendar.SECOND));    //>2012-01-18T14:15:22Z
    }

    @Test
    public void parsePoiPlazaCastlesKml() throws Exception {
        String path = getClass().getResource("/parsing/castles_poiplaza_kml20.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        FileParser<GpsPoint> parser = new KML20Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(508);

        GpsPoint firstPoint = waypoints.get(0);
        assertThat(firstPoint.getName()).isEqualTo("Castle (Zamek)");
        assertThat(firstPoint.getLatitude()).isCloseTo(51.21978, Offset.offset(0.001));
        assertThat(firstPoint.getLongitude()).isCloseTo(18.57219, Offset.offset(0.001));
    }

    @Test
    public void testFileDetection() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi_kml20.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser parser = new KML20Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi_kml20.kml").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        FileParser<GpsPoint> parser = new KML20Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();

        List<GpsPoint> waypoints = parser.parseFile(bis);
        assertThat(waypoints).hasSize(14);
    }

}
