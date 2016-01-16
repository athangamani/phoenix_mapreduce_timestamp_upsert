package org.apache.phoenix.dataload.stat;

import org.apache.phoenix.jdbc.PhoenixConnection;
import org.apache.phoenix.jdbc.PhoenixDriver;

import java.sql.*;

/**
 * Created by thangar
 */
public class StatPhoenixTableCreationTest {

    public static void main(String[] args) throws SQLException {
        DriverManager.registerDriver(PhoenixDriver.INSTANCE);
        //Connection connection = DriverManager.getConnection("jdbc:phoenix:tukpdmdlake03.tuk.cobaltgroup.com:/hbase-unsecure;CurrentSCN=100"); //
        Connection connection = DriverManager.getConnection("jdbc:phoenix:master01.preprod.datalake.cdk.com:2181:/hbase-unsecure;CurrentSCN=100"); //
        PhoenixConnection phoenixConnection = (PhoenixConnection)connection;
        System.out.println("phoenixConnection.getSCN() = " + phoenixConnection.getSCN());

        createTable(connection);
    }

    private static void createTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("\n" +
                "CREATE  TABLE stat_table (\n" +
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
                "COMPRESSION='LZ4'"
                //"TTL=5184000"
        );
        connection.commit();
    }
}