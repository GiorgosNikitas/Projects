package swinghy360;

public class Oxima {
    private int cid;
    private String marka;
    private String montelo;
    private String xrwma;
    private int autonomia;
    private String arithmos_kykloforias;
    private String eidos;
    private String typos;
    private int epibates;
    private int kostos_en;
    private int kostos_asf;
    private Boolean vlavi;
    private Boolean available;


    public Oxima(int cid, String marka, String montelo, String xrwma, int autonomia,
    			String arithmos_kykloforias, String eidos, String typos, int epibates,
    			int kostos_en, int kostos_asf, Boolean vlavi, Boolean available) {
    	this.cid = cid;
    	this.marka = marka;
    	this.montelo = montelo;
    	this.xrwma = xrwma;
    	this.autonomia = autonomia;
    	this.arithmos_kykloforias = arithmos_kykloforias;
    	this.eidos = eidos;
    	this.typos = typos;
    	this.epibates = epibates;
    	this.kostos_en = kostos_en;
    	this.kostos_asf = kostos_asf;
    	this.vlavi = vlavi;
    	this.available = available;
    }

    
    public int getCid() {
        return cid;
    }

    public String getMarka() {
        return marka;
    }

    public String getMontelo() {
        return montelo;
    }

    public String getXrwma() {
        return xrwma;
    }

    public int getAutonomia() {
        return autonomia;
    }

    public String getAr_kykloforias() {
        return arithmos_kykloforias;
    }

    public String getEidos() {
        return eidos;
    }

    public String getTypos() {
        return typos;
    }

    public int getEpivates() {
        return epibates;
    }

    public int getKostos_en() {
        return kostos_en;
    }
    
    public int getKostos_asf() {
        return kostos_asf;
    }
    
    public Boolean getVlavi() {
        return vlavi;
    }
    
    public Boolean getAvailable() {
        return available;
    }
}
