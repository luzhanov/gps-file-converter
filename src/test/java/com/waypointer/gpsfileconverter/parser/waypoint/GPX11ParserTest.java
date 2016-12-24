package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.TestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GPX11ParserTest {

    @Test
    public void testFileParsing() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi_gpx11.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        GPX11Parser parser = new GPX11Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(14);

        GpsPoint firstPoint = waypoints.get(0);
        assertThat(firstPoint.getName()).isEqualTo("PRAV-KORDON");
        assertThat(TestUtils.getDate(2012, Calendar.JANUARY, 18, 17, 15, 22))
                .isEqualTo(DateUtils.truncate(firstPoint.getCreatedDate(), Calendar.SECOND));
    }

    @Ignore
    @Test
    public void testError04FileParsing() throws Exception {
        String path = getClass().getResource("/parsing/error_04.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        GPX11Parser parser = new GPX11Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(14);

        GpsPoint firstPoint = waypoints.get(0);
        assertThat(firstPoint.getName()).isEqualTo("PRAV-KORDON");
        assertThat(TestUtils.getDate(2012, Calendar.JANUARY, 18, 17, 15, 22))
                .isEqualTo(DateUtils.truncate(firstPoint.getCreatedDate(), Calendar.SECOND));
    }

    @Test
    public void testNamingFieldsUsed() throws Exception {
        String path = getClass().getResource("/parsing/sample_gpx11_cmt.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        GPX11Parser parser = new GPX11Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(5);

        assertThat(waypoints.get(0).getName()).isEqualTo("NAME1");
        assertThat(waypoints.get(0).getDescription()).isEqualTo("DESC1 / CMT1");
        assertThat(waypoints.get(1).getName()).isEqualTo("DESC2");
        assertThat(waypoints.get(1).getDescription()).isEqualTo("DESC2 / CMT2");
        assertThat(waypoints.get(2).getName()).isEqualTo("DESC3");
        assertThat(waypoints.get(2).getDescription()).isEqualTo("DESC3");
        assertThat(waypoints.get(3).getName()).isEqualTo("NAME4");
        assertThat(waypoints.get(3).getDescription()).isEqualTo("CMT4");
        assertThat(waypoints.get(4).getName()).isEqualTo("NAME5");
        assertThat(waypoints.get(4).getDescription()).isEqualTo("CMT5");
    }

    @Test
    public void testFileDetection() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi_gpx11.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        GPX11Parser parser = new GPX11Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testFileDetectionError04() throws Exception {
        String path = getClass().getResource("/parsing/error_04.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        GPX11Parser parser = new GPX11Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        String path = getClass().getResource("/parsing/sample_ozi_gpx11.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        GPX11Parser parser = new GPX11Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();

        List<GpsPoint> waypoints = parser.parseFile(bis);
        assertThat(waypoints).hasSize(14);
    }

}
