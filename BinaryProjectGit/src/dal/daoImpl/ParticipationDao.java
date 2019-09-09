package dal.daoImpl;


import biz.bizFactory.BizFactory;
import biz.bizFactory.IBizFactory;
import biz.dto.IParticipationDto;
import dal.daoObject.IParticipationDao;
import dal.services.DalServices;
import dal.services.IBackendDalServices;
import exceptions.FatalException;
import ihm.Config;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParticipationDao implements IParticipationDao {

    private IBizFactory bizFactory;
    private IBackendDalServices dalBackendServices;
    private Config config;

    public ParticipationDao() throws FileNotFoundException, SQLException {
        this.bizFactory = new BizFactory();
        this.dalBackendServices = new DalServices();
        this.config = new Config("properties/prod.properties");
    }

    @Override
    public List<IParticipationDto> getRanking(int tournamentId) {
        List<IParticipationDto> participationDtoList = new ArrayList<>();
        try {
            // Si il n'y pas de user pour ce login, on peut l'inscrire
            PreparedStatement ps =
                    dalBackendServices.getPreparedStatement(config.getValueOfKey("getRanking"));
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    IParticipationDto participationDto = bizFactory.getParticipationDto();
                    participationDto.setUsername(rs.getString(1));
                    participationDto.setUserId(rs.getInt(2));
                    participationDto.setTournamentId(rs.getInt(3));
                    participationDto.setBalance(rs.getDouble(4));
                    participationDtoList.add(participationDto);
                }
            }
        } catch (SQLException e) {
            throw new FatalException(e.getMessage());
        }
        participationDtoList.sort(Comparator.comparingDouble(IParticipationDto::getBalance).reversed());
        return participationDtoList;
    }

    @Override
    public double getUserTournamentBalance(int tournamentID, int userId) {
        double balance = -1;
        try{
            PreparedStatement ps = dalBackendServices.getPreparedStatement(config.getValueOfKey("getUserTournamentBalance"));
            ps.setInt(1, tournamentID);
            ps.setInt(2, userId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    balance = rs.getInt(1);
                }
            }
        }catch(SQLException e){
            throw new FatalException(e.getMessage());
        }
        return balance;
    }

}
