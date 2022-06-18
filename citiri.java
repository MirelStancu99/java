import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class git {

    public static List<Sectie> citireJSON(String caleFisier) {
        List<Sectie> rezultat = new ArrayList<>();

        try(var fisier = new FileInputStream(caleFisier))
        {
            var tokener = new JSONTokener(fisier);
            var jsonSectii = new JSONArray(tokener);
            for(int i=0;i<jsonSectii.length();i++)
            {
                var jsonSectie = jsonSectii.getJSONObject(i);
                Sectie sectie = new Sectie();
                sectie.setCod(jsonSectie.getInt("cod_sectie"));
                sectie.setDenumire(jsonSectie.getString("denumire"));
                rezultat.add(sectie);
            }

        }

        return rezultat;
    }

    public static Connection connect() throws SQLException {
        String url = "jdbc:sqlite:data\\intretinere_apartamente.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void main(String[] args) {
        //SQL DB
        String sql = "SELECT NumarApartament, Nume, Suprafata, NumarPersoane FROM Apartamente";
        try(Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);)
        {
            while(rs.next()){
                Apartament a = new Apartament();
                a.setNumarApartament(rs.getInt("NumarApartament"));
                a.setNume(rs.getString("Nume"));
                a.setSuprafata(rs.getInt("Suprafata"));
                a.setNrPersoane(rs.getInt("NumarPersoane"));
                listaApartamente.add(a);
            }
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
    }

    public static void InsertDataDB(List<Apartamente> lista)throws Exception{
        try(var connection = DriverManager.getConnection(urlDB);
            var cmdInsert = connection.prepareStatement("INSERT INTO Apartamente VALUES (?,?,?,?)")){

            for ( var a : lista){
                cmdInsert.setInt(1,a.getNumarApartament());
                cmdInsert.setString(2,a.getNume());
                cmdInsert.setInt(3,a.getSuprafata());
                cmdInsert.setInt(4,a.getNumarPersoane());

                cmdInsert.execute();
            }
        }
    }

    public static void CreateTableDB()throws Exception{
        try(var connection = DriverManager.getConnection(urlDB);
            var statement = connection.createStatement()){

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Apartamente(NumarApartament integer, Nume text, Suprafata integer, NumarPerosane integer)");

            statement.executeUpdate("DELETE FROM Apartamente");
        }
    }


}
