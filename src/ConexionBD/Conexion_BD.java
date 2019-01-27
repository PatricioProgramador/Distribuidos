/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionBD;

import Juego.Carta;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author ale
 */

/*
    id para las operaciones SQL en ServidorReplicas
    1->INSERT_JUGADOR
    2->INSERT_CARTA_JUGADOR
    
    3->CLEAN_CARTA_JUGADOR
    4->CLEAN_JUGADOR
    5->ELMINAR_CARTA_JUGADOR
*/
public class Conexion_BD {
    private static final String SQL_INSERT_JUGADOR="INSERT INTO jugador (idJugador,host) values(?,?)";
    private static final String SQL_INSERT_CARTA_JUGADOR="INSERT INTO carta_jugador (idCarta,idJugador) values(?,?)";
    private static final String SQL_UPDATE_CARTA="UPDATE carta set disponible=? WHERE idCarta=?";
    private static final String SQL_SELECT_ALL="SELECT * FROM carta";
    private static final String SQL_CLEAN_JUGADOR="DELETE FROM jugador";
    private static final String SQL_CLEAN_CARTA_JUGADOR="DELETE FROM carta_jugador";
    private static final String SQL_BUSCAR_JUGADOR="SELECT * FROM jugador WHERE idJugador=?";
    private static final String SQL_BUSCAR_CARTA_JUGADOR="SELECT carta_jugador.idJugador, carta.valor, carta.tipo_carta FROM carta INNER JOIN carta_jugador ON (carta.idCarta=carta_jugador.idCarta) where carta_jugador.idJugador=?";
    private static final String SQL_ELIMINAR_CARTA_JUGADOR="DELETE FROM carta_jugador WHERE idJugador=? AND idCarta=?";
    
    //replicas
    //private static final String SQL_UPDATE_REPLICA="UPDATE replicas set ip=?";
    private static final String SQL_ELIMINAR_REPLICA="DELETE FROM replica WHERE ip=?";
    private static final String SQL_BUSCAR_REPLICAS="SELECT * FROM replica";
    private static final String SQL_INSERT_REPLICA="INSERT INTO replica (ip,principal) values(?,?)";
    
    private String driverMysql="com.mysql.jdbc.Driver";
    private String usuario="root";

    private String clave="xxxx";
    private String url="jdbc:mysql://localhost:3306/JuegoCartas"; 
    private Connection con;
    
    public Conexion_BD(String host) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        url="jdbc:mysql://"+host+":3306/JuegoCartas";
        //obtenerConexion();
    }
    
    public void eliminarCon() throws SQLException
    {
        con.close();
    }
    private void obtenerConexion() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        try{
            Class.forName(driverMysql).newInstance();
            con= (Connection) DriverManager.getConnection(url, usuario, clave);
        }
        catch(ClassNotFoundException | SQLException e)
        {
           System.out.println("no se hizo la conexión a la "+ e);
        }
    }
    public void ActualizarCarta(int idCarta) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_UPDATE_CARTA);
            ps.setBoolean(1, false);
            ps.setInt(2, idCarta);
            ps.executeUpdate();
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    public boolean BuscarJugador(int idJugador) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        PreparedStatement ps=null;
        ResultSet rs=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_BUSCAR_JUGADOR);
            ps.setInt(1, idJugador);
            rs=ps.executeQuery();
            int resultados=getResultadosJugador(rs);
            if(resultados>0)
                return true;
            else{
                return false;
            }
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    public Vector BuscarCartaJugador(int idJugador) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        PreparedStatement ps=null;
        ResultSet rs=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_BUSCAR_CARTA_JUGADOR);
            ps.setInt(1, idJugador);
            rs=ps.executeQuery();
            Vector resultados=ObtenerResultadosJugador(rs);
            return resultados;
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    public void InsertarJugador(int idJugador, String host) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_INSERT_JUGADOR);
            ps.setInt(1, idJugador);
            ps.setString(2, host);
            ps.executeUpdate();
        }
        //catch(SQLExc)
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    public List ObtenerListaCartas() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        PreparedStatement ps=null;
        ResultSet rs =null;
        obtenerConexion();

        try{
            ps=con.prepareStatement(SQL_SELECT_ALL);
            rs=ps.executeQuery();
            List resultados=getResultados(rs);
            if(resultados.size()>0)
                return resultados;
            else{
                return null;
            }
        }
        finally{
            if(ps!=null)
                ps.close();
            if(rs!=null)
                rs.close();
        }
    }
    private int getResultadosJugador(ResultSet rs) throws SQLException{
        int i=0;
        while(rs.next())
        {
            i++;
        }
        return i;
    }
    private List getResultados(ResultSet rs) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        List resultados=new ArrayList();
        obtenerConexion();

        while(rs.next())
        {
            Carta c=new Carta(rs.getInt("idCarta"), rs.getInt("valor"), rs.getString("tipo_carta"));
            c.setUrl(rs.getString("ruta"));
            resultados.add(c);
        }
        return resultados;
    }    
    public void limpiarJugadores() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_CLEAN_JUGADOR);
            ps.executeUpdate();
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    public void limpiarCartasJugadores() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_CLEAN_CARTA_JUGADOR);
            ps.executeUpdate();
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    
    public void EliminarCartaJugador(int idJugador, int idCarta) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_ELIMINAR_CARTA_JUGADOR);
            ps.setInt(1,idJugador);
            ps.setInt(2,idCarta);
            ps.executeUpdate();
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    
    public void InsertarCartaJugador(int idJugador, int idCarta) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_INSERT_CARTA_JUGADOR);
            ps.setInt(1, idCarta);
            ps.setInt(2, idJugador);
            ps.executeUpdate();
        }
        //catch(SQLExc)
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
  
    private Vector ObtenerResultadosJugador(ResultSet rs) throws SQLException {
        Vector resultados=new Vector(48);
        while(rs.next())
        {
            Carta c=new Carta(rs.getInt("idJugador"),rs.getInt("valor"), rs.getString("tipo_carta"));
            resultados.addElement(c);
        }
        return resultados;
    }
    //REPLICAS
    public void EliminarReplica(String ip) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_ELIMINAR_REPLICA);
            ps.setString(1,ip);
            ps.executeUpdate();
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }

    public List BuscarReplicas() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        PreparedStatement ps=null;
        ResultSet rs=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_BUSCAR_REPLICAS);
            rs=ps.executeQuery();
            List resultados=getReplicas(rs);
            return resultados;
        }
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    
     private List getReplicas(ResultSet rs) throws SQLException{
        List resultados=new ArrayList();
        while(rs.next())
        {
            String ip=rs.getString("ip");
            resultados.add(ip);
        }
        return resultados;
    }
    
     public void InsertarReplica(String ip, boolean principal) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        PreparedStatement ps=null;
        obtenerConexion();
        try{
            ps=con.prepareStatement(SQL_INSERT_REPLICA);
            ps.setString(1, ip);
            ps.setBoolean(2, principal);
            ps.executeUpdate();
        }
        //catch(SQLExc)
        finally{
            if(ps!=null){
                ps.close();
            }
        }
    }
    
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
    {
        Conexion_BD c=new Conexion_BD("localhost");
        //c.ActualizarCarta(1);
        //c.InsertarJugador("123.22.4.12");
        //c.obtenerConexion();
        c.InsertarReplica("8.120.0.123", true);
        //c.InsertarReplica("2017-12-14 08:50:05", "192.168.10.3", false);
        //c.EliminarReplica("12.12.12.12");
        List r=c.BuscarReplicas();
        
        for(int i=0; i<r.size();i++)
        {
            System.out.println(r.get(i));
        }
        /*List a=c.BuscarCartaJugador(2);
        for(int i=0;i<a.size();i++)
            System.out.println(a.get(i).toString());
        if(c.con!=null)
           c.con.close();*/
    }

    
}


