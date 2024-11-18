package swinghy360;
import java.util.Date;

public class Pelatis {
    private int pid;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String dieuthinsiSpitiou;
    private Date imerominiaGenisis;
    private int age;
    private int adiaOdigisis;
    private int cardNumber;
    private boolean isAdmin;

    public Pelatis(int pid, String firstName, String lastName, String password, String email,
            String dieuthinsiSpitiou, Date imerominiaGenisis, int age,
            int adiaOdigisis, int cardNumber, boolean isAdmin) {
    	 this.pid = pid;
		 this.firstName = firstName;
		 this.lastName = lastName;
		 this.password = password;
		 this.email = email;
		 this.dieuthinsiSpitiou = dieuthinsiSpitiou;
		 this.imerominiaGenisis = imerominiaGenisis;
		 this.age = age;
		 this.adiaOdigisis = adiaOdigisis;
		 this.cardNumber = cardNumber;
		 this.isAdmin = isAdmin;
	}
    
    public Pelatis(int pid, String firstName, String lastName, String password, String email,
                   String dieuthinsiSpitiou, Date imerominiaGenisis, int age,
                   int adiaOdigisis, int cardNumber) {
    	this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.dieuthinsiSpitiou = dieuthinsiSpitiou;
        this.imerominiaGenisis = imerominiaGenisis;
        this.age = age;
        this.adiaOdigisis = adiaOdigisis;
        this.cardNumber = cardNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getDieuthinsiSpitiou() {
        return dieuthinsiSpitiou;
    }

    public Date getImerominiaGenisis() {
        return imerominiaGenisis;
    }

    public int getAge() {
        return age;
    }

    public int getAdiaOdigisis() {
        return adiaOdigisis;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getPid() {
        return pid;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }

}