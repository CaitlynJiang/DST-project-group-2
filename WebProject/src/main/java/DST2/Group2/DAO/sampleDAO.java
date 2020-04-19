package DST2.Group2.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import DST2.Group2.Database.database;
import DST2.Group2.bean.Sample;


public class sampleDAO {
	public static int save(String uploadedBy) {
        AtomicInteger key = new AtomicInteger();
        Connection conn=database.connpostgres();
        System.out.println(key);
            try {
                PreparedStatement preparedStatement = conn.prepareStatement("insert into sample(created_at, uploaded_by) values (?,?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
                preparedStatement.setString(2, uploadedBy);
                key.set(preparedStatement.executeUpdate());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        return key.get();
    }
	
	
	public static Sample findById(int id) {
        AtomicReference<Sample> sample = new AtomicReference<>();
        Connection connection=database.connpostgres();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select id, created_at, uploaded_by from sample where id = ?");
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int sampleId = resultSet.getInt("id");
                    Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                    String uploadedBy = resultSet.getString("uploaded_by");
                    sample.set(new Sample(sampleId, createdAt, uploadedBy));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        return sample.get();
    }
	public static List<Sample> findAll() {
        List<Sample> samples = new ArrayList<>();
        Connection connection=database.connpostgres();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from sample");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int sampleId = resultSet.getInt("id");
                    Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                    String uploadedBy = resultSet.getString("uploaded_by");
                    Sample sample = new Sample(sampleId, createdAt, uploadedBy);
                    samples.add(sample);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        return samples;
    }
}
