package org.apache.phoenix.dataload.stat;


import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by thangar on 5/30/15.
 */
public class StatLineParser {

    public static final String FIELD_SEPERATOR = "|";
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd";

    public Stat parse (String line) throws NumberFormatException{
        Stat stat = null;

        String[] values = split(line, FIELD_SEPERATOR);
        if (values.length == 6) {
            stat = new Stat();

            stat.setPk1(values[0]);
            stat.setPk2(values[1]);
            stat.setPk3(new Long(values[2]));

                stat.setStat1(new Long(values[3]));
                stat.setStat2(new Long(values[4]));
            stat.setStat3(new Long(values[5]));
        }
        return stat;
    }

    private String[] split(String line, String separtor) {
        return line.split("\\" + separtor, -1);
    }

    public static Long parseDateString(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        try {
            return simpleDateFormat.parse(dateString).getTime();
        } catch (ParseException e) {
            //if we cannot parse correctly, simply return null no need to print the stack trace at high volumes
            //e.printStackTrace();
        }
        return null;
    }
}
