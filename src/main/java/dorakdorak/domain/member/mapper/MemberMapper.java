package dorakdorak.domain.member.mapper;


import dorakdorak.domain.auth.dto.MemberAuthDto;
import dorakdorak.domain.member.dto.MemberSignupDto;
import dorakdorak.domain.member.dto.MemberSummaryDto;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

  void insertMember(MemberSignupDto memberSignupDto);

  void insertAllergyCategoryMap(@Param("memberId") long memberId,
      @Param("allergyCategoryId") long allergyCategoryId);

  Boolean existByEmail(@Param("email") String email);

  Optional<MemberAuthDto> findByEmailIntoAuth(@Param("email") String email);

  void updateMemberRefreshToken(@Param("email") String email,
      @Param("refreshToken") String refreshToken);

  int findMemberByMemberEmail(@Param("email") String email);

  List<String> findAllergyCategoryNameByMemberId(@Param("memberId") Long memberId);

  Optional<MemberSummaryDto> findMemberSummaryByMemberId(@Param("memberId") Long memberId);

  void incrementZeroWasteCount(@Param("memberId") Long memberId);
}
