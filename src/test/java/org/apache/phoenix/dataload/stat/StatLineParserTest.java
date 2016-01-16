package org.apache.phoenix.dataload.stat;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by thangar on 5/30/15.
 */
public class StatLineParserTest {

    public static String line = "pk1|pk2|977|2|3|25238";
    @Test
    public void testParseStatSingleLine() throws IOException {
        StatLineParser parser = new StatLineParser();
        Stat stat = parser.parse(line);

        Assert.assertNotNull(stat.getPk1());

        Assert.assertEquals("pk1", stat.getPk1());
        Assert.assertEquals("pk2", stat.getPk2());
        Assert.assertEquals(977, stat.getPk3());

        Assert.assertEquals(2, stat.getStat1());
        Assert.assertEquals(3, stat.getStat2());
        Assert.assertEquals(25238, stat.getStat3());
    }
}
