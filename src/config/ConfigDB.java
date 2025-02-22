/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;
import java.sql.Driver;
    import java.sql.DriverManager;
    import java.sql.Connection;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import javax.swing.JOptionPane;
/**
 *
 * @author munn
 */

public class ConfigDB {
        String jdbcURL ="jdbc:mysql://localhost:3306/2210010358_pbo2";
    String username ="root";
    String password ="";
    
    Connection koneksi;
    
    public ConfigDB(){
        try (Connection Koneksi = DriverManager.getConnection(jdbcURL, username, password)){
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Berhasil");
        } catch (SQLException error) {
            System.err.println(error.toString());
        }
    }
    
        public ConfigDB(String url, String user, String pass){
        
        try(Connection Koneksi = DriverManager.getConnection(url, user, pass)) {
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
            System.out.println("Berhasil");
        } catch (Exception error) {
            System.err.println(error.toString());
        }
        
    }
        
        public static Connection getKoneksi() {
        try {
            String url = "jdbc:mysql://localhost:3306/2210010358_pbo2";  // Ganti dengan URL dan database Anda
            String username = "root";  // Ganti dengan username database Anda
            String password = "";  // Ganti dengan password database Anda
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }

    
    public static boolean duplicateKey(String table, String PrimaryKey, String value){
        boolean hasil=false;
        
        try {
            Statement sts = getKoneksi().createStatement();
            ResultSet rs = sts.executeQuery("SELECT*FROM "+table+" WHERE "+PrimaryKey+" ='"+value+"'");
            hasil = rs.next();
            
            rs.close();
            sts.close();
            getKoneksi().close();
            
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
        
        return hasil;
    }
    
    public void SimpanactorStatement(String id_Pelanggan, String email, String Telp, String Alamat){
        
        try {
            if (duplicateKey("ACTOR","id_Pelanggan",id_Pelanggan)){
                JOptionPane.showMessageDialog(null, "ID sudah terdaftar");
            } else{
                String SQL = "INSERT INTO actor (id_Pelanggan,email,Telp,Alamat) Value('"+id_Pelanggan+"','"+email+"','"+Telp+"','"+Alamat+"')";
                Statement perintah = getKoneksi().createStatement();
                
                perintah.executeUpdate(SQL);
                perintah.close();
                getKoneksi().close();
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    public String getTabelField(String[] Field) {
    // Menggabungkan field dengan koma
    return "(" + String.join(", ", Field) + ")";
    }

    public String getTabelValue(String[] Value) {
        // Menggabungkan value dengan koma dan menambahkan tanda kutip untuk nilai string
        return "(" + String.join(", ", Value) + ")";
    }

    
// Method untuk membangun nilai field yang akan di-update
     public static String getFieldValueEdit(String[] Field, String[] value){
        String hasil = "";
        int deteksi = Field.length-1;
        try {
            for (int i = 0; i < Field.length; i++) {
                if (i==deteksi){
                    hasil = hasil +Field[i]+" ='"+value[i]+"'";
                }else{
                   hasil = hasil +Field[i]+" ='"+value[i]+"',";  
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return hasil;
    }

     
    // Method untuk update data secara dinamis
    public static void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary, String[] Field, String[] Value) {
        try {
            // Mencari apakah ID yang diberikan ada di dalam tabel
            String SQLCheck = "SELECT COUNT(*) FROM " + NamaTabel + " WHERE " + PrimaryKey + " = '" + IsiPrimary + "'";
            Statement perintah = getKoneksi().createStatement();
            ResultSet rs = perintah.executeQuery(SQLCheck);

            if (rs.next() && rs.getInt(1) > 0) { // Jika ID ditemukan
                // Melakukan update jika ID ada
                String SQLUbah = "UPDATE " + NamaTabel + " SET " + getFieldValueEdit(Field, Value) + " WHERE " + PrimaryKey + "='" + IsiPrimary + "'";
                perintah.executeUpdate(SQLUbah);
                JOptionPane.showMessageDialog(null, "Data Berhasil DiUpdate");
            } else { // Jika ID tidak ditemukan
                JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan");
            }

            rs.close(); // Tutup ResultSet
            perintah.close(); // Tutup Statement
            getKoneksi().close(); // Tutup koneksi

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


     
    public static void HapusDinamis(String NamaTabel, String PK, String isi) {
        try {
            // Mencari apakah ID yang diberikan ada di dalam tabel
            String SQLCheck = "SELECT COUNT(*) FROM " + NamaTabel + " WHERE " + PK + " = '" + isi + "'";
            Statement perintah = getKoneksi().createStatement();
            ResultSet rs = perintah.executeQuery(SQLCheck);

            if (rs.next() && rs.getInt(1) > 0) { // Jika ID ditemukan
                // Menghapus data jika ID ditemukan
                String SQLDelete = "DELETE FROM " + NamaTabel + " WHERE " + PK + "='" + isi + "'";
                perintah.executeUpdate(SQLDelete);
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            } else { // Jika ID tidak ditemukan
                JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan");
            }

            rs.close(); // Tutup ResultSet
            perintah.close(); // Tutup Statement
            getKoneksi().close(); // Tutup koneksi

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // Ini untuk Tabel Pasien
        
    public static void SimpanPengeluaranStatement(String No, String Daftar_Belanja, String Biaya){
        
        try {
            if (duplicateKey("PENGELUARAN","No",No)){
                JOptionPane.showMessageDialog(null, "ID sudah terdaftar");
            } else{
                String SQL = "INSERT INTO pengeluaran (no,Daftar_Belanja,Biaya) Value('"+No+"','"+Daftar_Belanja+"','"+Biaya+"')";
                Statement perintah = getKoneksi().createStatement();
                
                perintah.executeUpdate(SQL);
                perintah.close();
                getKoneksi().close();
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
                
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    
  
}