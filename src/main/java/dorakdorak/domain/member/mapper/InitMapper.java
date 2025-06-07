package dorakdorak.domain.member.mapper;


import dorakdorak.domain.member.dto.response.InitDataResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InitMapper {

  List<InitDataResponse> findUniversities();

  List<InitDataResponse> findAllergys();
}
