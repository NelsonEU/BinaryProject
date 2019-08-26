package biz.ucc;

import biz.bizFactory.BizFactory;
import biz.bizFactory.IBizFactory;
import biz.dto.IDistributionDto;
import dal.daoImpl.DistributionDao;
import dal.daoObject.IDistributionDao;
import dal.services.DalServices;
import dal.services.IDalServices;
import exceptions.FatalException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DistributionUcc implements IDistributionUcc {

    private IDalServices dalServices;
    private IBizFactory bizFactory;
    private IDistributionDao distributionDao;

    public DistributionUcc() {
        bizFactory = new BizFactory();
        try {
            dalServices = new DalServices();
            distributionDao = new DistributionDao();
        } catch (SQLException | FileNotFoundException e) {
            System.out.println("ERREUR DB: " + e.getMessage());
        }
    }

    @Override
    public List<IDistributionDto> getDistributions() {
        List<IDistributionDto> tabDistributions;
        try{
            this.dalServices.startTransaction();
            tabDistributions = distributionDao.getDistributions();
        }catch(FatalException exception){
            this.dalServices.rollbackTransaction();
            throw new FatalException("Erreur fatale: " + exception.getMessage());
        }finally{
            this.dalServices.commitTransaction();
        }
        if(tabDistributions.isEmpty()) {
            return null;
        }
        return tabDistributions;
    }

//    private class CustomDistributionsSerializer extends StdSerializer<I>
}
