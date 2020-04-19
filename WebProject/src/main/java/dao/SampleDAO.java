package dao;

import DBmtd.DBmethods;

import bean.SampleBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class SampleDAO {
	public int save(String uploadedBy) {
        AtomicInteger key = new AtomicInteger();
        Connection connection= DBmethods.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into sample(created_at, uploaded_by) values (?,?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
                preparedStatement.setString(2, uploadedBy);
                key.set(preparedStatement.executeUpdate());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        return key.get();
    }

	public SampleBean findById(int id) {
        AtomicReference<SampleBean> sample = new AtomicReference<>();
        Connection connection= DBmethods.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select id, created_at, uploaded_by from sample where id = ?");
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int sampleId = resultSet.getInt("id");
                    Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                    String uploadedBy = resultSet.getString("uploaded_by");
                    sample.set(new SampleBean(sampleId, createdAt, uploadedBy));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        return sample.get();
    }

	public List<SampleBean> findAll() {
        List<SampleBean> samples = new ArrayList<>();
        Connection connection= DBmethods.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from sample");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int sampleId = resultSet.getInt("id");
                    Date createdAt = new Date(resultSet.getTimestamp("created_at").getTime());
                    String uploadedBy = resultSet.getString("uploaded_by");
                    SampleBean sample = new SampleBean(sampleId, createdAt, uploadedBy);
                    samples.add(sample);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
        return samples;
    }
}
