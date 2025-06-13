package dorakdorak.domain.admin.service;

import dorakdorak.domain.admin.dto.response.DosirakSearchResponse;
import dorakdorak.domain.admin.dto.response.DosirakSearchResponseDto;
import dorakdorak.domain.admin.mapper.AdminMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final AdminMapper adminMapper;

  @Override
  public DosirakSearchResponse searchDosiraksByName(String name) {
    List<DosirakSearchResponseDto> dosiraks = adminMapper.findDosiraksByName(name);

    return new DosirakSearchResponse(dosiraks);
  }
}