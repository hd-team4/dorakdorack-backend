package dorakdorak.domain.member.mapper;


import dorakdorak.domain.auth.dto.response.MemberAuthDto;
import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import dorakdorak.domain.member.dto.response.MemberSummaryResponseDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

  void insertMember(MemberSignupRequest memberSignupRequest);

  void insertAllergyCategoryMap(@Param("memberId") long memberId,
      @Param("allergyCategoryId") long allergyCategoryId);

  Boolean existByEmail(@Param("email") String email);

  MemberAuthDto findByEmailIntoAuth(@Param("email") String email);

  void updateMemberRefreshToken(@Param("email") String email,
      @Param("refreshToken") String refreshToken);

  int findMemberByMemberEmail(String email);

  List<String> findAllergyCategoryNameByMemberId(@Param("memberId") Long memberId);

  MemberSummaryResponseDto findMemberSummaryByMemberId(@Param("memberId") Long memberId);

}
