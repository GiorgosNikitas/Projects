package DataBaseCreateAndTests;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import swinghy360.Enoikiasi;
import swinghy360.Oxima;
import swinghy360.ShowCars;

public class DataBase {

    /**
     * @param args the command line arguments
     */
    static String url = new String("jdbc:mysql://localhost");
    static String databaseName = new String("project1");
    static int port = 3306;
    static String username = new String("root");
    static String password = new String("");


    
    public static void create_oxima() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);

        String create_oxima = "CREATE TABLE oxima"
                + "(cid INTEGER(255) not NULL AUTO_INCREMENT,"
                + "marka VARCHAR(255) not NULL,"
                + "montelo VARCHAR(255) not NULL," //username:64 , @ : 1 , domain : 255
                + "xrwma VARCHAR(255) not NULL,"
                + "autonomia INTEGER(255) not NULL,"
                + "arithmo_kukloforias VARCHAR(255) not NULL,"
                + "idos_oximatos VARCHAR(255)not NULL,"
                + "tupos_oximatos VARCHAR(255),"
                + "arithmos_epivatwn INTEGER(255) not NULL,"
                + "kostos_enoikiasis INTEGER(255) not NULL,"
                + "kostos_asfalias INTEGER(255) not NULL,"
                + "vlavi BOOL not NULL,"
                + "available BOOL not NULL,"
                + "UNIQUE (arithmo_kukloforias),"
                + "PRIMARY KEY( cid ))";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(create_oxima);
        con.close();
    }
    
    
    public static void create_Pelatis() throws SQLException, ClassNotFoundException {
    	Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection (
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);

        String createPelatis = new String("CREATE TABLE pelatis"
                + "(pid INTEGER(255) not NULL AUTO_INCREMENT,"
                + "firstName VARCHAR(255) not NULL,"
                + "lastName VARCHAR(255) not NULL," //username:64 , @ : 1 , domain : 255
                + "password VARCHAR(255) not NULL,"
                + "email VARCHAR(320) not NULL,"
                + "dieuthinsi_spitiou VARCHAR(255),"
                + "imerominia_genisis DATE not NULL,"
                + "age INTEGER(3) not NULL,"
                + "adia_odigisis INTEGER(255),"
                + "card_number INTEGER(255) not NULL,"
                + "is_admin BOOLEAN NULL DEFAULT false,"
                + "UNIQUE (card_number),"
                + "UNIQUE (adia_odigisis),"
                + "UNIQUE (email),"
                + "PRIMARY KEY( pid ))");
        Statement stmt = con.createStatement();
        stmt.executeUpdate(createPelatis);
        con.close();
    }

    public static void create_enoikiasi() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
        String createEnoikiaseis = new String("CREATE TABLE enoikiasi"
                + "(eid INTEGER(255) not NULL AUTO_INCREMENT,"
                + "imerominia_enoikiasis DATE not NULL,"
                + "diarkia_enoikiasis INTEGER(255) NOT NULL,"
                + "aithmos_diplwmatos VARCHAR(255),"
                + "poso INTEGER(255) not NULL,"
                + "asfaleia  BOOL,"
                + "cid INTEGER(255) not NULL,"
                + "pid INTEGER(255) not NULL,"
                + "PRIMARY KEY (eid),"
                + "FOREIGN KEY(cid) REFERENCES oxima(cid),"
                + "FOREIGN KEY(pid) REFERENCES pelatis(pid))");
        Statement stmt = con.createStatement();
        stmt.executeUpdate(createEnoikiaseis);
        con.close();
    }
    
    public static void create_atyxima() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
        String createEnoikiaseis = new String("CREATE TABLE atyxima"
                + "(aid INTEGER(255) not NULL AUTO_INCREMENT,"
                + "imerominia DATE not NULL,"
                + "description VARCHAR(255),"
                + "cid INTEGER(255) not NULL,"
                + "pid INTEGER(255) not NULL,"
                + "PRIMARY KEY (aid),"
                + "FOREIGN KEY(cid) REFERENCES oxima(cid),"
                + "FOREIGN KEY(pid) REFERENCES pelatis(pid))");
        Statement stmt = con.createStatement();
        stmt.executeUpdate(createEnoikiaseis);
        con.close();
    }
    
    public static boolean isAvailable(String _dateStart, int days, int cid) {
    	LocalDate dateStart = LocalDate.parse(_dateStart);
    	LocalDate dateEnd = dateStart.plusDays(days);
    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
        try (Connection con = DriverManager.getConnection(
            url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {
        	
            String query = "SELECT * FROM enoikiasi WHERE cid=" + cid;
            try (PreparedStatement st = con.prepareStatement(query)) {
                
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                    	LocalDate dateRentStart = LocalDate.parse(rs.getString("imerominia_enoikiasis"));
                    	LocalDate dateRentEnd = dateRentStart.plusDays(rs.getInt("diarkia_enoikiasis"));
                    	boolean isBetween = ((dateStart.isEqual(dateRentStart) || dateStart.isAfter(dateRentStart))
                                && (dateStart.isEqual(dateRentEnd) || dateStart.isBefore(dateRentEnd) || dateStart.isEqual(dateRentEnd)))
                    			|| ((dateEnd.isEqual(dateRentStart) || dateEnd.isAfter(dateRentStart))
                                && (dateEnd.isEqual(dateRentEnd) || dateEnd.isBefore(dateRentEnd) || dateEnd.isEqual(dateRentEnd)));
                    	
                    	if (isBetween) {
                    		return false;
                    	}
                    }
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
    	
        return true;
    }
    
    static private Oxima createOximaFromResultSet(ResultSet rs) throws SQLException {
        return new Oxima(
        	rs.getInt("cid"),
            rs.getString("marka"),
            rs.getString("montelo"),
            rs.getString("xrwma"),
            rs.getInt("autonomia"),
            rs.getString("arithmo_kukloforias"),
            rs.getString("idos_oximatos"),
            rs.getString("tupos_oximatos"),
            rs.getInt("arithmos_epivatwn"),
            rs.getInt("kostos_enoikiasis"),
            rs.getInt("kostos_asfalias"),
            rs.getBoolean("vlavi"),
            rs.getBoolean("available")
        );
    }
    
    public static Oxima getOximaByCid(int id) {
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {

                String query = "SELECT * FROM oxima WHERE cid=" + id;
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                        if (rs.next()) {
                        	return createOximaFromResultSet(rs);
                        } else {
                            return null;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();  // Log the exception properly or display a user-friendly message
        }
    	return null;
    }
    
    public static Oxima getOximaByCategory(int cid, String category) {
    	try { 
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {

                String query = "SELECT * FROM oxima WHERE idos_oximatos = '" + category + "' AND vlavi = false AND cid <> " + cid;
                try (PreparedStatement st = con.prepareStatement(query)) {
                    
                    try (ResultSet rs = st.executeQuery()) {
                        if (rs.next()) {
                        	return createOximaFromResultSet(rs);
                        } else {
                            return null;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();  // Log the exception properly or display a user-friendly message
        }
    	return null;
    }
    
    public static Enoikiasi getEnoikiasiFromPid(int pid) {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
        try (Connection con = DriverManager.getConnection(
            url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {
        	
            String query = "SELECT * FROM enoikiasi WHERE pid=" + pid;
            try (PreparedStatement st = con.prepareStatement(query)) {
                
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                    	return new Enoikiasi(
                    			rs.getInt("eid"),
                    			rs.getString("imerominia_enoikiasis"),
                    			rs.getInt("diarkia_enoikiasis"),
                    			rs.getString("aithmos_diplwmatos"),
                    			rs.getInt("poso"),
                    			rs.getBoolean("asfaleia"),
                    			rs.getInt("cid"),
                    			rs.getInt("pid"));
                    }
                }
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
    	
        return null;
    }

    public static void add_DB() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);

        
        String query = "INSERT INTO pelatis(firstName, lastName, password, email, dieuthinsi_spitiou, imerominia_genisis, age, adia_odigisis, card_number, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int pid = 0; //to pid mas.
        PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Giorgos");
        pst.setString(2, "Chrysopoulos");
        pst.setString(3, "Chrysopoulos");
        pst.setString(4, "goe_chr@csd.uoc.gr");
        pst.setString(5, "kalokairinou");
        pst.setDate(6, Date.valueOf("1998-05-05"));
        pst.setInt(7, 25);
        pst.setInt(8, 0101);
        pst.setInt(9,00000);
        pst.setBoolean(10, false); 
        System.out.println("okk");
        pst.executeUpdate();
        
        ResultSet rs = pst.getGeneratedKeys();

        if (rs.next()) {
            pid = rs.getInt(1);
        }

        /* 
        ---------- 
        Deuteros pelatis
        -----------
         */
        query = "INSERT INTO pelatis(firstName, lastName, password, email, dieuthinsi_spitiou, imerominia_genisis, age, adia_odigisis, card_number, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        pid = 0; //to pid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Giorgos");
        pst.setString(2, "nikitas");
        pst.setString(3, "nikitas");
        pst.setString(4, "goe_nik@gmail.gr");
        pst.setString(5, "gazi");
        pst.setDate(6, Date.valueOf("2000-05-05"));
        pst.setInt(7, 23);
        pst.setInt(8, 0000);
        pst.setInt(9,123);
        pst.setBoolean(10, false); 
        pst.executeUpdate();
        
        
        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            pid = rs.getInt(1);
        }


        /* 
        ---------- 
        Tritos pelatis
        -----------
         */
        query = "INSERT INTO pelatis(firstName, lastName, password, email, dieuthinsi_spitiou, imerominia_genisis, age, adia_odigisis, card_number, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        pid = 0; //to pid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "sotiris");
        pst.setString(2, "vlasis");
        pst.setString(3, "vlasis");
        pst.setString(4, "sot_vl@gmail.gr");
        pst.setString(5, "voutes");
        pst.setDate(6, Date.valueOf("2001-05-05"));
        pst.setInt(7, 22);
        pst.setInt(8, 1111);
        pst.setInt(9,1234);
        pst.setBoolean(10, false); 
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            pid = rs.getInt(1);
        }


        /* 
        ---------- 
        Tetartos pelatis
        -----------
         */
        
        query = "INSERT INTO pelatis(firstName, lastName, password, email, dieuthinsi_spitiou, imerominia_genisis, age, adia_odigisis, card_number, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        pid = 0; //to pid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Mixalis");
        pst.setString(2, "Georgiou");
        pst.setString(3, "georgiou");
        pst.setString(4, "mix.g@gmail.gr");
        pst.setString(5, "herakleion");
        pst.setDate(6, Date.valueOf("2006-01-01"));
        pst.setInt(7, 18);
        pst.setInt(8, 0001111);
        pst.setInt(9,123456);
        pst.setBoolean(10, false); 
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            pid = rs.getInt(1);
        }
        
        
        /* 
        ---------- 
        Admin
        -----------
         */
        
        query = "INSERT INTO pelatis (firstName, lastName, password, email, dieuthinsi_spitiou, imerominia_genisis, age, adia_odigisis, card_number, is_admin)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        pid = 0; //to pid mas.

        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, "AdminFirstName");
            pst.setString(2, "AdminLastName");
            pst.setString(3, "admin");
            pst.setString(4, "admin");
            pst.setString(5, "AdminAddress");
            pst.setDate(6, Date.valueOf("1990-01-01"));
            pst.setInt(7, 30);
            pst.setInt(8, 12345); // Example adia_odigisis
            pst.setInt(9, 67890); // Example card_number
            pst.setBoolean(10, true); // Set is_admin to true for admin

            
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();

            if (rs.next()) {
                pid = rs.getInt(1);
            }
        
        /*
         --------
         Prwto oxima
        -----------
         */
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        int cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Ford");
        pst.setString(2, "Fiesta");
        pst.setString(3, "aspro");
        pst.setInt(4, 150);
        pst.setString(5, "7777");
        pst.setString(6, "amaxi");
        pst.setString(7, "Coupe");
        pst.setInt(8, 5);
        pst.setInt(9, 50);
        pst.setInt(10, 10);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }

        
        /*
        --------
        Deftero oxima
       -----------
        */
       
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Mersedes");
        pst.setString(2, "CLK");
        pst.setString(3, "mauro");
        pst.setInt(4, 100);
        pst.setString(5, "1111");
        pst.setString(6, "amaxi");
        pst.setString(7, "Suv");
        pst.setInt(8, 6);
        pst.setInt(9, 40);
        pst.setInt(10, 10);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }

        /*
        --------
        trito oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Xiaomi");
        pst.setString(2, "redmi");
        pst.setString(3, "mauro");
        pst.setInt(4, 20);
        pst.setString(5, "p001");
        pst.setString(6, "patini");
        pst.setString(7, "");
        pst.setInt(8, 1);
        pst.setInt(9, 5);
        pst.setInt(10, 2);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }
        
        /*
        --------
        tetarto oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Ideal");
        pst.setString(2, "mountain");
        pst.setString(3, "kokkino");
        pst.setInt(4, 0);
        pst.setString(5, "B001");
        pst.setString(6, "podilato");
        pst.setString(7, "");
        pst.setInt(8, 1);
        pst.setInt(9, 5);
        pst.setInt(10, 2);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }
        
        /*
        --------
        pempto oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Honda");
        pst.setString(2, "c50");
        pst.setString(3, "gri");
        pst.setInt(4, 50);
        pst.setString(5, "m001");
        pst.setString(6, "mixani");
        pst.setString(7, "");
        pst.setInt(8, 2);
        pst.setInt(9, 15);
        pst.setInt(10, 5);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }
        
        /*
        --------
        Ekto oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Fiat");
        pst.setString(2, "Punto");
        pst.setString(3, "Kitrino");
        pst.setInt(4, 50);
        pst.setString(5, "MIX-0707");
        pst.setString(6, "amaxi");
        pst.setString(7, "Hatcback");
        pst.setInt(8, 2);
        pst.setInt(9, 15);
        pst.setInt(10, 5);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }
        
        /*
        --------
        Evdomo oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Alfa Romeo");
        pst.setString(2, "Mito");
        pst.setString(3, "Kitrino");
        pst.setInt(4, 50);
        pst.setString(5, "MIX-0107");
        pst.setString(6, "amaxi");
        pst.setString(7, "Hatcback");
        pst.setInt(8, 2);
        pst.setInt(9, 15);
        pst.setInt(10, 5);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }
        
        /*
        --------
        Ogdoo oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Suzuki");
        pst.setString(2, "Hayabusa");
        pst.setString(3, "Prasino");
        pst.setInt(4, 50);
        pst.setString(5, "Ate-0107");
        pst.setString(6, "mixani");
        pst.setString(7, "Supersport");
        pst.setInt(8, 2);
        pst.setInt(9, 15);
        pst.setInt(10, 5);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }
        
        /*
        --------
        Enato oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Samsung");
        pst.setString(2, "Galaxy");
        pst.setString(3, "Aspro");
        pst.setInt(4, 50);
        pst.setString(5, "c001");
        pst.setString(6, "patini");
        pst.setString(7, "");
        pst.setInt(8, 2);
        pst.setInt(9, 15);
        pst.setInt(10, 5);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }
        
        /*
        --------
        Dekato oxima
       -----------
        */   
        
        
        query= new String("INSERT INTO oxima(marka,montelo,xrwma,autonomia,arithmo_kukloforias,idos_oximatos,tupos_oximatos,arithmos_epivatwn,kostos_enoikiasis,kostos_asfalias,vlavi,available)"
        		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
        
        cid = 0; //to cid mas.
        pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, "Bullet");
        pst.setString(2, "Bmx");
        pst.setString(3, "Aspro");
        pst.setInt(4, 0);
        pst.setString(5, "e001");
        pst.setString(6, "podilato");
        pst.setString(7, "");
        pst.setInt(8, 2);
        pst.setInt(9, 15);
        pst.setInt(10, 5);
        pst.setBoolean(11,false);
        pst.setBoolean(12,true);
        
        pst.executeUpdate();

        rs = pst.getGeneratedKeys();

        if (rs.next()) {
            cid = rs.getInt(1);
        }

        con.close();

        con.close();
        
    }
    
    

    public static void drop_All() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);

        String drop_All = "DROP TABLE pelatis,oxima,enoikiasi";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(drop_All);
        con.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Class.forName("com.mysql.cj.jdbc.Driver");
        int fake_ui;

        while (true) {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("1 = Create Tables \t 2 = define DB(fixed) \n0 = Exit \t -1 = Drop Tables");
            fake_ui = keyboard.nextInt();
            if (fake_ui == 1) {
            	create_oxima();
            	 // System.out.println("okk");
            	create_Pelatis();
            	//System.out.println("okk2");
            	create_enoikiasi();
            	//System.out.println("okk3");
            	create_atyxima();
            } else if (fake_ui == 2) {
            	 add_DB();
            } else if (fake_ui == 0) {
                return;
            } else if (fake_ui == -1) {
                drop_All();
            }
        }
    }
}