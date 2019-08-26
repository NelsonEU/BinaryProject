package dal.daoImpl;

import biz.bizFactory.BizFactory;
import biz.bizFactory.IBizFactory;
import biz.dto.IUserDto;
import biz.impl.User;
import dal.daoObject.IUserDao;
import dal.services.DalServices;
import dal.services.IBackendDalServices;
import exceptions.FatalException;
import ihm.Config;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserDao implements IUserDao {


    private Config config;
    private IBizFactory bizFactory;
    private IBackendDalServices dalBackendServices;

    public UserDao() throws FileNotFoundException, SQLException {
        this.bizFactory = new BizFactory();
        this.dalBackendServices = new DalServices();
        this.config = new Config("properties/prod.properties");
    }

    @Override
    public IUserDto getUserByEmail(String email) throws FatalException {
        IUserDto user = bizFactory.getUserDto();
        try {
            PropertyConfigurator.configure(config.getLog4jProperties());
            PreparedStatement preparedStatement =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("getUserByEmail"));
            preparedStatement.setString(1, email);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                    user.setEmail(rs.getString(2));
                    user.setPassword(rs.getString(3));
                    user.setSalt(rs.getString(4));
                    user.setUsername(rs.getString(5));
                    user.setBalance(rs.getInt(6));
                    user.setAdmin(rs.getBoolean(7));
//                    logger.info("Utilisateur trouvé selon son login avec succès.");
                    return user;
                }

            }
        } catch (SQLException exception) {
//            logger.info("Exception lancée lors de l'appel de la methode: findUserByLogin : "
//                    + exception.getMessage() + "----" + exception.getErrorCode() + "-----");
            System.out.println("Erreur getUserByEmail");
            throw new FatalException("Erreur getUserByEmail");
        }
        return null;
    }

    @Override
    public IUserDto insertNewUser(IUserDto userDto) {
        try {
            // Si il n'y pas de user pour ce login, on peut l'inscrire
            PreparedStatement ps =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("insertNewUser"));

            ps.setString(1, userDto.getEmail());
            ps.setString(2, userDto.getPassword());
            ps.setString(3, userDto.getSalt());
            ps.setString(4, userDto.getUsername());

            System.out.println(ps.toString());
            ps.executeUpdate();

            return userDto;

        } catch (SQLException exception) {
            System.out.println("Erreur insertUser");
            throw new FatalException("Erreur insertUser");
        }
    }
}
