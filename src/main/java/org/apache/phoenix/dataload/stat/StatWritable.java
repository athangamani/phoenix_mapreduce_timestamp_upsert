package org.apache.phoenix.dataload.stat;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by thangar on 9/3/15.
 */
public class StatWritable implements DBWritable, Writable {

    private Stat stat;

    public void readFields(DataInput input) throws IOException {
        stat.setPk1(input.readLine());
        stat.setPk2(input.readLine());
        stat.setPk3(input.readLong());

        stat.setStat1(input.readLong());
        stat.setStat2(input.readLong());
        stat.setStat3(input.readLong());
    }

    public void write(DataOutput output) throws IOException {
        output.writeBytes(stat.getPk1());
        output.writeBytes(stat.getPk2());
        output.writeLong(stat.getPk3());

        output.writeLong(stat.getStat1());
        output.writeLong(stat.getStat2());
        output.writeLong(stat.getStat3());
    }

    public void readFields(ResultSet rs) throws SQLException {
        stat.setPk1(rs.getString("PK1"));
        stat.setPk2(rs.getString("PK2"));
        stat.setPk3(rs.getLong("PK3"));

        stat.setStat1(rs.getLong("STAT1"));
        stat.setStat2(rs.getLong("STAT2"));
        stat.setStat3(rs.getLong("STAT3"));
    }

    public void write(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, stat.getPk1());
        pstmt.setString(2, stat.getPk2());
        pstmt.setLong(3, stat.getPk3());

        pstmt.setLong(4, stat.getStat1());
        pstmt.setLong(5, stat.getStat2());
        pstmt.setLong(6, stat.getStat3());
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }
}