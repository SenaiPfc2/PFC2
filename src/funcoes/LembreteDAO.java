/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package funcoes;

import atributos.Cliente;
import atributos.Lembrete;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author S015365
 */
public class LembreteDAO {
    
    public static void CadLembrete(Lembrete lembrete){
        
        PreparedStatement stmt;
        try {   
            String sql = ("INSERT INTO tablembrete (dataContato,hora,descricao,tabCliente_idcliente) VALUES(?,?,?,?)");
            stmt = Conexao.getConnection().prepareStatement(sql);      
                  
               
                stmt.setDate(1, (Date) lembrete.getDataLembrete());
                stmt.setTime(2, lembrete.getHora());
                stmt.setString(3, lembrete.getDescricao());
                stmt.setInt(4, lembrete.getCodCliente());
                              
                stmt.executeUpdate();
                stmt.close();  

            } catch (SQLException ex) {      
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("Erro ao Cadastrar Lembrete: ",ex);       
            }
    }
    
    public static ArrayList CarregaLembrete(int id) {
        
        Statement stmt;
        ArrayList<Lembrete> lembretes = new ArrayList<Lembrete>();
        
        try {            
            String Sql = "select idLembrete, dataContato, hora, descricao, tabCliente_idcliente "
                    + "from tablembrete inner join tabcliente on idcliente = tabCliente_idcliente "
                    + "where idcliente = '"+ id +"';";
            
            ResultSet rs;            
            stmt = Conexao.getConnection().createStatement();            
            rs = stmt.executeQuery(Sql); 
            
            while(rs.next()){
                Lembrete l = new Lembrete();
                
                l.setIdLembrete(rs.getInt("idLembrete"));
                l.setDataLembrete((rs.getDate("dataContato")));
                l.setHora(rs.getTime("hora"));
                l.setDescricao(rs.getString("descricao"));
                l.setCodCliente(rs.getInt("tabCliente_idcliente"));
                lembretes.add(l);                
            }            
            rs.close();
            stmt.close();
            
        } catch (SQLException ex) {      
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Erro ao excluir os dados do Cliente: ", ex);    
        }    
        return lembretes;
    }
    
    public static void ExcluirLembrete(int id){ //se tiver mais de um lembrete ver
        
        PreparedStatement stmt;
        try {
            
            String sql = ("DELETE FROM tablembrete WHERE idLembrete = ?; ");
            
            stmt = Conexao.getConnection().prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();

        } catch (SQLException ex) {      
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Erro ao excluir os dados do Cliente: ",ex);    
        }
    }
    
    public static void UpdateLembrete(Lembrete lembrete, int id) {
        
        CallableStatement stmt;
        try { 
            
            stmt = Conexao.getConnection().prepareCall("{call UpdateLembrete(?,?,?)}");
            
            stmt.setInt(1, id);
            
            stmt.execute();
            stmt.close();

        } catch (SQLException ex) {      
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Erro ao excluir os dados do Cliente: ",ex);    
        }
    }
    
    public static ArrayList<Cliente> ListarCliente(){
        
        Statement stmt;
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        
        try {            
            String Sql = "SELECT * FROM tabcliente cli\n" +
                        "INNER JOIN tabtel tel\n" +
                        "INNER JOIN tabemail email\n" +
                        "INNER JOIN tabcontato cont\n" +
                        "ON cont.id_contato = cli.tabContato_id_contato AND\n" +
                        "cont.id_contato = tel.contato_id AND\n" +
                        "id_contato = email.contato_id_contato;";

            ResultSet rs;
            
            stmt = Conexao.getConnection().createStatement();
            
            rs = stmt.executeQuery(Sql); 
            
            while(rs.next()){
                Cliente c = new Cliente();
                
                c.setId(rs.getInt("idcliente"));
                c.setEmpresa((rs.getString("empresa")));
                c.setCnpj(rs.getString("cnpj"));
                c.setContato(rs.getString("contato"));
                c.setSetor(rs.getString("setor"));                 
                c.setTel(rs.getString("telefone"));
                c.setEmail(rs.getString("email"));               
                clientes.add(c);                
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Erro ao Listar os dados do Clientes: ",ex);
        }
        return clientes;
    }
    
}
