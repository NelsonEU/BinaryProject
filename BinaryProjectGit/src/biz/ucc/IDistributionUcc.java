package biz.ucc;

import biz.dto.IDistributionDto;

import java.io.IOException;
import java.util.List;

public interface IDistributionUcc {

    List<IDistributionDto> getDistributions() throws IOException;
}
