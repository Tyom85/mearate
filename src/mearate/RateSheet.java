package mearate;

import java.time.LocalDate;
import java.util.Objects;

public class RateSheet {
    private int code;
    private String dest;
    private double rate;
    private String status;
    private LocalDate date;
    private LocalDate pend_date;
    private double old_rate;
    private String old_new;

    public RateSheet() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getPend_date() {
        return pend_date;
    }

    public void setPend_date(LocalDate pend_date) {
        this.pend_date = pend_date;
    }

    public double getOld_rate() {
        return old_rate;
    }

    public void setOld_rate(double old_rate) {
        this.old_rate = old_rate;
    }


    public void setOld_new(String old_new) {
        this.old_new = old_new;
    }

    @Override
    public String toString() {
        return
                code +
                        "," + dest +
                        "," + rate +
                        "," + status +
                        "," + date +
                        "," + pend_date +
                        "," + old_rate;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateSheet rateSheet = (RateSheet) o;
        return code == rateSheet.code && Double.compare(rateSheet.rate, rate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, rate);
    }
}
