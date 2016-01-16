package org.apache.phoenix.dataload.stat;

import org.junit.Test;

/**
 * Created by thangar on 1/15/16.
 */
public class SimpleTest {

    @Test
    public void sgsfsf(){
        String tableDef = "CREATE  TABLE stat_table (\n" +
                "  pk1 VARCHAR NOT NULL,\n" +
                "  pk2 VARCHAR NOT NULL,\n" +
                "  pk3 UNSIGNED_LONG NOT NULL,\n" +
                "\n" +
                "\n" +
                "  stat1 UNSIGNED_LONG,\n" +
                "  stat2 UNSIGNED_LONG,\n" +
                "  stat3 UNSIGNED_LONG,\n" +
                "  CONSTRAINT pk PRIMARY KEY (pk1, pk2, pk3)\n" +
                ")\n" +
                "SALT_BUCKETS=32,\n" +
                "COMPRESSION='LZ4'";

        System.out.println("tableDef = " + tableDef);
    }
}
