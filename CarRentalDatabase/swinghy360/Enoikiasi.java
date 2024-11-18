package swinghy360;

public class Enoikiasi {
    private int eid;
    private String rentalDate;
    private int days;
    private String license;
    private int cost;
    private Boolean insurance;
    private int cid;
    private int pid;

    public Enoikiasi(int eid, String rentalDate, int days, String license, int cost, Boolean insurance,
    			int cid, int pid) {
    	this.cid = cid;
    	this.rentalDate = rentalDate;
    	this.days = days;
    	this.license = license;
    	this.cost = cost;
    	this.insurance = insurance;
    	this.cid = cid;
    	this.pid = pid;
    }

    
    public int getEid() {
        return eid;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public int getDays() {
        return days;
    }

    public String getLicense() {
        return license;
    }

    public int getCost() {
        return cost;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public int getCid() {
        return cid;
    }

    public int getPid() {
        return pid;
    }
}