/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package atm;


import java.sql.*;

/**
 *
 * @author User
 */
public class clsDBConnection1 {
    
    public static Connection getConnection1()
    {
        Connection con = null;
        try
        {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_auth?user=root&password=8834");
        
        System.out.println("The connection with mysql is succesful");
        
        
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            System.out.println(e);
        }
        catch(SQLException e)
        {
           e.printStackTrace();
           System.out.println(e);
        }   
        return con;
    }

        
    
    
}

