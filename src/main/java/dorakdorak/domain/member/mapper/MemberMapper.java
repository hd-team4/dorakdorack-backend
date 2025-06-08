package dorakdorak.domain.member.mapper;


import dorakdorak.domain.member.dto.request.MemberSignupRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

  void insertMember(MemberSignupRequest memberSignupRequest);

  void insertAllergyCategoryMap(@Param("memberId") long memberId,
      @Param("allergyCategoryId") long allergyCategoryId);

}
