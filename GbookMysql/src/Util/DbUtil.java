package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbUtil {
    // 定数定義（共通）
    private static final String DB_HOST = "127.0.0.1";
    private static final String DB_NAME = "rezodb";
    private static final String DB_USER = "rezouser";
    private static final String DB_PASS = "Rezo_0000";

    // 定数定義（PostgreSQL用）
    // private static final String DBMS = "postgresql";
    // private static final String DB_DRIVER = "org." + DBMS + ".Driver";
    // private static final String DB_PORT = "5432";
    // private static final String DB_URL = "jdbc:" + DBMS + "://" + DB_HOST +
    // ":" + DB_PORT + "/" + DB_NAME;

    // 定数定義（MySQL用）
    private static final String DBMS = "mysql";
    private static final String DB_DRIVER = "com." + DBMS + ".cj.jdbc.Driver";
    private static final String DB_PORT = "3306";
    private static final String DB_URL = "jdbc:" + DBMS + "://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    //		+ "?serverTimezone=JST";	//JDBCドライバのバージョン8.0.22以前を利用する場合は必要

    // インスタンス化の禁止
//    private  DbUtil() {
//    }

    // Connectionを単一のインスタンスとする
    private static Connection con;

//turn off or on based on your pc unit
    static String url = "jdbc:mysql://localhost:3306/mydb";
    static String userName = "root";
    static String password = "admin";

    public static Connection getConnection() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        //con = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);

        //replacing with my own

        con = DriverManager.getConnection(url, userName, password);
        return con;

    }





    /*
     * TODO 以下の仕様でメソッドを作成してください。
     *
     * 戻り値		：なし
     * メソッド名	：closeStatement
     * 引数			：DBステートメント(PreparedStatement型)
     * 例外			：SQLException
     * 処理			：引数のDBステートメントがnullでない場合、そのDBステートメントをクローズする
     */
    public static void closeStatement(PreparedStatement stmt) throws SQLException {
        if(stmt != null) {
            stmt.close();
        }
    }



    /*
     * TODO 以下の仕様でメソッドを作成してください。
     *
     * 戻り値		：なし
     * メソッド名	：closeConnection()
     * 引数			：切断するコネクション(Connection型)
     * 例外			：SQLException
     * 処理			：引数のコネクションがnullでない場合、そのコネクションを切断する
     */
    public static void closeConnection(Connection con) throws SQLException{
        if(con!=null) {
            con.close();
        }

    }


}
