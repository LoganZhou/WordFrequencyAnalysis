/**
 * Created by zhouheng on 2017/7/4.
 */
package OutOfBound;
import java.sql.*;

public class DBConnection {
    /**
     * 创建一个数据库连接
     * @return Connection 连接对象
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:DictionaryDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
