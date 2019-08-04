package biz.ucc;

import biz.bizFactory.BizFactory;
import biz.bizFactory.IBizFactory;
import biz.bizObject.ITournamentBiz;
import biz.dto.ITournamentDto;
import dal.daoImpl.TournamentDao;
import dal.daoImpl.UserDao;
import dal.daoObject.ITournamentDao;
import dal.daoObject.IUserDao;
import dal.services.DalServices;
import dal.services.IDalServices;
import exceptions.BizException;
import exceptions.FatalException;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TournamentUcc implements ITournamentUcc {

    private IDalServices dalServices;
    private IBizFactory bizFactory;
    private ITournamentDao  tournamentDao;

    public TournamentUcc(){
        bizFactory = new BizFactory();
        try {
            dalServices = new DalServices();
            tournamentDao = new TournamentDao();
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("ERREUR DB: " + e.getMessage());
        }
    }

    @Override
    public List<ITournamentDto> get24hTournaments(int userId) {
        List<ITournamentDto> listTournaments = new ArrayList<>();
        try{
            this.dalServices.startTransaction();
            listTournaments = tournamentDao.get24hTournaments();
            if(userId != -1) {
                this.checkUserRegistration(userId, listTournaments);
            }
        }catch(FatalException exception){
            this.dalServices.rollbackTransaction();
            throw new FatalException("Erreur fatale: " + exception.getMessage());
        }finally{
            this.dalServices.commitTransaction();
        }
        return listTournaments;
    }

    private void checkUserRegistration(int userId, List<ITournamentDto> listTournaments) {
        for(ITournamentDto tournamentDto : listTournaments){
            ITournamentBiz tournamentBiz = (ITournamentBiz)tournamentDto;
            if(tournamentBiz.getParty().contains(userId)){
                tournamentDto.setRegistered(true);
            }
        }
    }
}
