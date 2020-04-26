package dao;

import DBmtd.DBmethods;
import bean.GeneBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneDAO {

    public List<GeneBean> findAll(){
        List<GeneBean>  geneBeans = new ArrayList<>();
        DBmethods.execSQL(connection -> {
            try{
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT symbol, ensembl_id FROM gene;");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String symbol = resultSet.getString("symbol");
                    String ensembl_id = resultSet.getString("ensembl_id");
                    GeneBean geneBean = new GeneBean(symbol, ensembl_id);
                    geneBeans.add(geneBean);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return geneBeans;
    }
}
