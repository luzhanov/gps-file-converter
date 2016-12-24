package com.waypointer.gpsfileconverter.parser.waypoint;

import com.waypointer.gpsfileconverter.GpsPoint;
import com.waypointer.gpsfileconverter.parser.FileParser;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.waypointer.gpsfileconverter.FileConverterUtils.detectNullString;

/**
 * Format description: http://www.rus-roads.ru/gps/help_ozi/fileformats.html
 */
@Slf4j
public class WPTParser implements FileParser<GpsPoint> {

    private static final String DEFAULT_CHARSET = "Cp1251";
    private static final Double UNIX_START_DATE_FOR_DELPHI = 25569.0;

    @Override
    public List<GpsPoint> parseFile(BufferedInputStream is) {
        List<GpsPoint> pointlist = new ArrayList<>();
        String line = null;
        int lineNumber = 0;
        try {
       //     String encoding = detectEncoding(is);

            //todo: correctly detect encoding - https://code.google.com/p/juniversalchardet/
            InputStreamReader streamReader = new InputStreamReader(is, DEFAULT_CHARSET);
            BufferedReader br = new BufferedReader(streamReader);
            while ((line = br.readLine()) != null) {
                if (lineNumber >= 4 && !line.trim().isEmpty()) { //skip first 4 lines
                    String[] secondSplit = line.split(",");
                    GpsPoint waypoint = new GpsPoint(
                            Long.decode(secondSplit[0].trim()),
                            detectNullString(secondSplit[1]),
                            Double.parseDouble(secondSplit[2].trim()),
                            Double.parseDouble(secondSplit[3].trim()),
                            detectNullString(secondSplit[10]));
                    String dateTimeDelphiFormat = secondSplit[4].trim();
                    waypoint.setCreatedDate(parseDateFromDelphiFormat(dateTimeDelphiFormat));

                    pointlist.add(waypoint);
                }
                lineNumber++;
            }
            br.close();
            return pointlist;
        } catch (IOException | NumberFormatException e) {
            String msg = "Error during parsing of WPT file, line=[" + line + "], linenumber=" + lineNumber;
            logger.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }

//    private String detectEncoding(BufferedInputStream is) throws IOException {
//        final int bufLength = 4096;
//
//        byte[] buf = new byte[bufLength];
//        is.mark(0);
//
//        UniversalDetector detector = new UniversalDetector(null);
//
//        int readInt;
//        while ((readInt = is.read(buf)) > 0 && !detector.isDone()) {
//            detector.handleData(buf, 0, readInt);
//        }
//        detector.dataEnd();
//
//        String encoding = detector.getDetectedCharset();
//
//        is.mark(0);
//        is.reset(); //restore stream position
//
//        if (encoding != null) {
//            return encoding;
//        } else {
//            return DEFAULT_CHARSET;
//        }
//    }

    private static Date parseDateFromDelphiFormat(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return new Date();
        }

        try {
            Double parsedDate = new Double(dateString.trim());
            return parseDelphiDateTime(parsedDate);
        } catch (NumberFormatException e) {
            logger.error("Problem parsing WPT datetime = " + dateString);
            return new Date();
        }
    }

    @Override
    public boolean isParsableFile(BufferedInputStream is) throws IOException {
        return FileParser.isParsableFileCheck(is, "OziExplorer Waypoint File Version 1.1");
    }

    public static Date parseDelphiDateTime(Double delphiTime) {
        if (delphiTime >= 0) {
            long timestamp = Math.round((delphiTime - UNIX_START_DATE_FOR_DELPHI) * 86400000);
            return new Date(timestamp);
        } else {
            Double dayPart = (double) Math.round(delphiTime);

            String doubleAsText = delphiTime.toString();
            String stringNumber = doubleAsText.split("\\.")[1];
            Double timePart = Double.parseDouble("0." + stringNumber);

            long timestampDay = Math.round((dayPart - UNIX_START_DATE_FOR_DELPHI) * 86400000);
            long timestampTime = Math.round(timePart * 86400000);

            return new Date(timestampDay + timestampTime);
        }
    }

}
