package biz.ucc;

import biz.bizFactory.BizFactory;
import biz.dto.IParticipationDto;
import dal.daoImpl.DistributionDao;
import dal.daoImpl.ParticipationDao;
import dal.daoImpl.TournamentDao;
import dal.daoObject.IParticipationDao;
import dal.services.DalServices;
import dal.services.IDalServices;
import exceptions.FatalException;
import javafx.collections.transformation.SortedList;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ParticipationUcc implements IParticipationUcc {

    private IParticipationDao participationDao;
    private IDalServices dalServices;

    public ParticipationUcc(){
        try {
            dalServices = new DalServices();
            participationDao = new ParticipationDao();
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("ERREUR DB: " + e.getMessage());
        }
    }

    @Override
    public List<IParticipationDto> getRanking(int tournamentId) {
        List<IParticipationDto> participationDtoList = new ArrayList<IParticipationDto>();
        try{
            this.dalServices.startTransaction();
            participationDtoList = this.participationDao.getRanking(tournamentId);
        }catch(FatalException e){
            this.dalServices.rollbackTransaction();
            throw new FatalException(e.getMessage());
        }finally {
            this.dalServices.commitTransaction();
        }
        return participationDtoList;
    }
}
