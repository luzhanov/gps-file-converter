package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.TestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GPX10ParserTest {

    @Test
    public void testFileParsing() throws Exception {
        String path = getClass().getResource("/parsing/sample_gpx10.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        GPX10Parser parser = new GPX10Parser();
        List<GpsPoint> waypoints = parser.parseFile(new BufferedInputStream(fis));
        assertThat(waypoints).hasSize(2);

        GpsPoint point = waypoints.get(1);
        assertThat(point.getName()).isEqualTo("POINT2");
        assertThat(TestUtils.getDate(2007, Calendar.OCTOBER, 14, 10, 9, 57))
                .isEqualTo(DateUtils.truncate(point.getCreatedDate(), Calendar.SECOND));
    }

    @Test
    public void testCommentFieldUsed() throws Exception {
        String path = getClass().getResource("/parsing/sample_gpx10_cmt.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        GPX10Parser parser = new GPX10Parser();
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
        String path = getClass().getResource("/parsing/sample_gpx10.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        GPX10Parser parser = new GPX10Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();
    }

    @Test
    public void testIntegrity() throws Exception {
        String path = getClass().getResource("/parsing/sample_gpx10.gpx").getFile();
        FileInputStream fis = new FileInputStream(path);

        BufferedInputStream bis = new BufferedInputStream(fis);
        GPX10Parser parser = new GPX10Parser();
        assertThat(parser.isParsableFile(bis)).isTrue();

        List<GpsPoint> waypoints = parser.parseFile(bis);
        assertThat(waypoints).hasSize(2);
    }

}
