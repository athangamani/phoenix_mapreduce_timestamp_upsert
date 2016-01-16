package org.apache.phoenix.dataload.stat;

import org.apache.phoenix.jdbc.PhoenixConnection;
import org.apache.phoenix.jdbc.PhoenixDriver;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by thangar on 1/14/16.
 */
public class StatUpsertLoadTest {

    public static final int TOTAL_NUMBER_OF_THREADS = 20;
    public static final int TOTAL_ITERATIONS_PER_THREAD = 200000;
    public static final int COMMIT_BATCH_SIZE = 10000;
    public static int totalUpsertedOverall = 0;

    @Test
    public void testLoad() throws SQLException, InterruptedException {
        //long timestampForConnection = 1447574400000L;
        //long timestampForConnection = 1448092800000L;
        long timestampForConnection = 1447574400000L;

        Collection<Thread> threadBagReference = new LinkedList<Thread>();
        for (int i = 0; i < TOTAL_NUMBER_OF_THREADS; i++) {
            System.out.println("kicking off thread number = " + i);
            Thread executionThread = new Thread(new PhoenixClientThread(i, timestampForConnection));
            executionThread.start();
            threadBagReference.add(executionThread);
        }
        for (Thread thread : threadBagReference) {
            thread.join();
        }
    }

    private static class PhoenixClientThread implements Runnable {
        private int threadNumber;
        private long totalTimePerThread;
        private long totalUpsertedPerThread;
        private long timestamp;

        public PhoenixClientThread(int threadNumber, long timestamp) {
            this.threadNumber = threadNumber;
            this.timestamp = timestamp;
            totalUpsertedPerThread = 0;
        }

        public void run() {
            try {
                DriverManager.registerDriver(PhoenixDriver.INSTANCE);
                Connection connection = DriverManager.getConnection("jdbc:phoenix:master01.preprod.datalake.cdk.com:2181:/hbase-unsecure;CurrentSCN=" + timestamp); //
                PhoenixConnection phoenixConnection = (PhoenixConnection)connection;
                System.out.println("phoenixConnection.getSCN() = " + phoenixConnection.getSCN());
                    updateData(connection, TOTAL_ITERATIONS_PER_THREAD);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void updateData(Connection conn, int iterationsForThread) throws SQLException {
            long start_timestamp = new java.util.Date().getTime();
            PreparedStatement pstmt = null;

            try {
                pstmt = conn.prepareStatement("upsert into STAT_TABLE values (?, ?, ?, ?, ?, ?)");
                for (int iteration = 1; iteration <= iterationsForThread; iteration++) {
                    //System.out.println("iteration = " + iteration);
                    pstmt.setString(1, "pk1-" + iteration % 10000);
                    pstmt.setString(2, "pk2-" + iteration % 10000);
                    pstmt.setLong(3, threadNumber * TOTAL_ITERATIONS_PER_THREAD + iteration + timestamp);

                    pstmt.setLong(4, Double.valueOf(10 * Math.random()).intValue());
                    pstmt.setLong(5, Double.valueOf(10 * Math.random()).intValue());
                    pstmt.setLong(6, Double.valueOf(10 * Math.random()).intValue());

                    pstmt.addBatch();
                    if ((iteration % COMMIT_BATCH_SIZE) == 0){
                        pstmt.executeBatch();
                        conn.commit();
                        long end_timestamp = new java.util.Date().getTime();
                        synchronized (PhoenixClientThread.class) {
                            long time_taken = end_timestamp - start_timestamp;
                            totalTimePerThread = totalTimePerThread + time_taken;
                            totalUpsertedPerThread = totalUpsertedPerThread + COMMIT_BATCH_SIZE;
                            totalUpsertedOverall = totalUpsertedOverall + COMMIT_BATCH_SIZE;
                            System.out.print("time_taken = " + time_taken);
                            System.out.print(" threadNumber = " + threadNumber);
                            System.out.print(" totalTimePerThread = " + totalTimePerThread);
                            System.out.print(" averageTimePerThread = " + totalTimePerThread / totalUpsertedPerThread);
                            System.out.print(" iteration = " + iteration);
                            System.out.print(" totalUpsertedOverall = " + totalUpsertedOverall);
                            System.out.println("");
                            start_timestamp = end_timestamp;
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                pstmt.close();
                conn.close();
            }
        }
    }
}
