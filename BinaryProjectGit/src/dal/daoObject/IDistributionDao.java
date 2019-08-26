package dal.daoObject;

import biz.dto.IDistributionDto;

import java.util.List;

public interface IDistributionDao {
    List<IDistributionDto> getDistributions();
}
