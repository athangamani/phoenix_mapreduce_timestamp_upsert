package org.apache.phoenix.dataload.stat;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by thangar on 1/14/16.
 */
public class StatDataCreatorTest {

    @Test
    public void testGenerateData() throws FileNotFoundException {

        int totalNumberOfRecords = 10000000;
        PrintWriter writer = new PrintWriter("/Users/thangar/phoenix_logs/stat-data-1.txt");
        //PrintWriter writer = new PrintWriter("stat-data-1.txt");

        for (int seed=0;seed<totalNumberOfRecords;seed++) {
            StringBuilder sb = new StringBuilder();
            sb.append("pk1-" + seed % 10000); //pk1
            sb.append("|");
            sb.append("pk2-" + seed % 10000); //pk2
            sb.append("|");
            sb.append(seed); //pk3
            sb.append("|");
            sb.append(Double.valueOf(100 * Math.random()).intValue());
            sb.append("|");
            sb.append(Double.valueOf(100 * Math.random()).intValue());
            sb.append("|");
            sb.append(Double.valueOf(100 * Math.random()).intValue());
            sb.append("\n");
            writer.write(sb.toString());
        }
        writer.close();
    }
}
