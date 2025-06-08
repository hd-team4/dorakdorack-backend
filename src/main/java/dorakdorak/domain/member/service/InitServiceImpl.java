package dorakdorak.domain.member.service;

import dorakdorak.domain.member.dto.response.InitDataResponse;
import dorakdorak.domain.member.mapper.InitMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitServiceImpl implements InitService {

  private final InitMapper initMapper;

  @Override
  public List<InitDataResponse> findUniversities() {
    return initMapper.findUniversities();
  }

  @Override
  public List<InitDataResponse> findAllergys() {
    return initMapper.findAllergys();
  }
}
