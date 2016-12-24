package com.waypointer.gpsfileconverter.generator.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.generator.FileGenerator;
import com.waypointer.gpsfileconverter.parser.waypoint.KML22Parser;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KML22GeneratorTest {

    @Test
    public void testRoundingCoordinates() throws Exception {
        FileGenerator<GpsPoint> generator = new KML22Generator();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<GpsPoint> wptList = new ArrayList<>();
        GpsPoint wpt1 = new GpsPoint(1L, "Name1", 0.12345678912345678, 0.22345678912345678, null);
        GpsPoint wpt2 = new GpsPoint(2L, "Name2", 0.12345, 0.22345, null);

        wptList.add(wpt1);
        wptList.add(wpt2);

        generator.generateFile(baos, wptList);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        KML22Parser parser = new KML22Parser();
        List<GpsPoint> resultList = parser.parseFile(new BufferedInputStream(bais));
        assertThat(resultList).hasSize(2);
        assertThat(resultList.get(0).getLatitude()).isEqualTo(0.123457);
        assertThat(resultList.get(0).getLongitude()).isEqualTo(0.223457);
        assertThat(resultList.get(1).getLatitude()).isEqualTo(0.12345);
        assertThat(resultList.get(1).getLongitude()).isEqualTo(0.22345);
    }

}
