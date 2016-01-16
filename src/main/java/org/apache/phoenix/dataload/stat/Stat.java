package org.apache.phoenix.dataload.stat;

/**
 * Created by thangar on 9/3/15.
 */
public final class Stat {

    private String pk1;
    private String pk2;
    private long pk3;

    private long stat1;
    private long stat2;
    private long stat3;

    public String getPk1() {
        return pk1;
    }

    public void setPk1(String pk1) {
        this.pk1 = pk1;
    }

    public String getPk2() {
        return pk2;
    }

    public void setPk2(String pk2) {
        this.pk2 = pk2;
    }

    public long getPk3() {
        return pk3;
    }

    public void setPk3(long pk3) {
        this.pk3 = pk3;
    }

    public long getStat1() {
        return stat1;
    }

    public void setStat1(long stat1) {
        this.stat1 = stat1;
    }

    public long getStat2() {
        return stat2;
    }

    public void setStat2(long stat2) {
        this.stat2 = stat2;
    }

    public long getStat3() {
        return stat3;
    }

    public void setStat3(long stat3) {
        this.stat3 = stat3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stat that = (Stat) o;

        if (pk1 != null ? !pk1.equals(that.pk1) : that.pk1 != null) return false;
        if (pk2 != null ? !pk2.equals(that.pk2) : that.pk2 != null) return false;
        if (pk3 != that.pk3) return false;
        if (stat3 != that.stat3) return false;
        if (stat2 != that.stat2) return false;
        if (stat1 != that.stat1) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pk1 != null ? pk1.hashCode() : 0;
        result = 31 * result + (pk2 != null ? pk2.hashCode() : 0);
        result = 31 * result + (int) (pk3 ^ (pk3 >>> 32));
        result = 31 * result + (int) (stat1 ^ (stat1 >>> 32));
        result = 31 * result + (int) (stat2 ^ (stat2 >>> 32));
        result = 31 * result + (int) (stat3 ^ (stat3 >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "pk1='" + pk1 + '\'' +
                ", pk2='" + pk2 + '\'' +
                ", pk3=" + pk3 +
                ", stat1=" + stat1 +
                ", stat2=" + stat2 +
                ", stat3=" + stat3 +
                '}';
    }
}
