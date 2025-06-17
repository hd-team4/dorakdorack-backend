package dorakdorak.domain.zeroWaste.mapper;

import dorakdorak.domain.zeroWaste.dto.UniversityRankingDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ZeroWasteMapper {

  List<UniversityRankingDto> findUniversityRankings();
}
