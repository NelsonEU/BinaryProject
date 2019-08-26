package dal.daoImpl;

import biz.bizFactory.BizFactory;
import biz.dto.IDistributionDto;
import com.owlike.genson.Genson;
import dal.daoObject.IDistributionDao;
import dal.services.DalServices;
import exceptions.FatalException;
import ihm.Config;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DistributionDao implements IDistributionDao {

    private BizFactory bizFactory;
    private DalServices dalBackendServices;
    private Config config;
    private Genson genson;

    public DistributionDao() throws FileNotFoundException, SQLException {
        this.bizFactory = new BizFactory();
        this.dalBackendServices = new DalServices();
        this.config = new Config("properties/prod.properties");
        this.genson = new Genson();
    }

    @Override
    public List<IDistributionDto> getDistributions() {

        List<IDistributionDto> tabDistributions = new ArrayList<>();
        try {
            PropertyConfigurator.configure(config.getLog4jProperties());
            PreparedStatement preparedStatement =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("getDistributions"));
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    IDistributionDto distr = createDistribution(rs);
                    tabDistributions.add(distr);
//                    logger.info("Utilisateur trouvé selon son login avec succès.");
                }

//                listTournaments = new ArrayList<>(mapTournaments.values());
            }
        }catch (FatalException | SQLException e){

        }
        return tabDistributions;

    }

    private IDistributionDto createDistribution(ResultSet rs) throws SQLException {
        IDistributionDto distribution = bizFactory.getDistributionDto();
        distribution.setId(String.valueOf(rs.getInt(1)));
        String distributionJson = rs.getString(2);
        System.out.println("JSONJSON: " + distributionJson);
//        Map<String, Double> map = genson.deserialize(distributionJson, Map.class);
//        System.out.println("MAPMAP: " + map);
//        distribution.setDistribution(map);
        distribution.setDistribution(distributionJson);
        return distribution;
    }

}
