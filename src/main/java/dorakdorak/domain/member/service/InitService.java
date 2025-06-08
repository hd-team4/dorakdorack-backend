package dorakdorak.domain.member.service;

import dorakdorak.domain.member.dto.response.InitDataResponse;
import java.util.List;

public interface InitService {

  List<InitDataResponse> findUniversities();

  List<InitDataResponse> findAllergys();
}
