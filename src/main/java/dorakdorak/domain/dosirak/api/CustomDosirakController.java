package dorakdorak.domain.dosirak.api;

import dorakdorak.domain.auth.dto.response.CustomMemberDetails;
import dorakdorak.domain.dosirak.dto.CustomDosirakSaveDto;
import dorakdorak.domain.dosirak.dto.request.CustomDosirakPreviewRequest;
import dorakdorak.domain.dosirak.dto.request.CustomDosirakRegisterRequest;
import dorakdorak.domain.dosirak.dto.response.CustomDosirakPreviewResponse;
import dorakdorak.domain.dosirak.dto.response.CustomDosirakRegisterResponse;
import dorakdorak.domain.dosirak.service.DosirakPromptGenerator;
import dorakdorak.domain.dosirak.service.DosirakService;
import dorakdorak.domain.member.dto.MemberAllergyDto;
import dorakdorak.domain.member.service.MemberService;
import dorakdorak.infra.openai.CustomDosirakUploader;
import dorakdorak.infra.openai.OpenAiVisionClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CustomDosirak", description = "커스텀 도시락 생성 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/custom-dosiraks")
public class CustomDosirakController {

  private static final Logger log = LoggerFactory.getLogger(CustomDosirakController.class);

  private final DosirakPromptGenerator dosirakPromptGenerator;
  private final OpenAiVisionClient openAiVisionClient;
  private final MemberService memberService;
  private final DosirakService dosirakService;
  private final CustomDosirakUploader customDosirakUploader;

  @Operation(
      summary = "커스텀 도시락 이미지 및 정보 생성",
      description = "사용자의 응답을 바탕으로 도시락 정보를 생성하고 이미지를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "성공",
              content = @Content(schema = @Schema(implementation = CustomDosirakPreviewResponse.class))),
          @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
          @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
      }
  )
  @PostMapping("/preview")
  public ResponseEntity<CustomDosirakPreviewResponse> generateImage(
      @RequestBody CustomDosirakPreviewRequest customDosirakPreviewRequest,
      @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {

    Long memberId = customMemberDetails.getId();

    List<String> allergyCategoryNames = memberService.findAllergyCategoryNameByMemberId(
        memberId);

    customDosirakPreviewRequest.setAllergies(allergyCategoryNames);

    List<String> question = extractStringFields(customDosirakPreviewRequest);

    MemberAllergyDto memberAllergyDto = new MemberAllergyDto(allergyCategoryNames);

    CustomDosirakPreviewResponse customDosirakPreviewResponse = dosirakPromptGenerator.generateCustomInfo(
        question);

    return ResponseEntity.status(HttpStatus.OK).body(customDosirakPreviewResponse);
  }

  @PostMapping()
  private ResponseEntity<CustomDosirakRegisterResponse> customDosirakRegister(
      @RequestBody CustomDosirakRegisterRequest customDosirakRegisterRequest,
      @AuthenticationPrincipal CustomMemberDetails customMemberDetails) {

    String imageUrl = customDosirakUploader.uploadCustomDosirak(
        customDosirakRegisterRequest.getImageUrl());

    Long memberId = customMemberDetails.getId();

    CustomDosirakSaveDto customDosirakSaveDto = new CustomDosirakSaveDto(
        customDosirakRegisterRequest, imageUrl, memberId);

    dosirakService.registerCustomDosirak(customDosirakSaveDto);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new CustomDosirakRegisterResponse("success", "커스텀 도시락이 성공적으로 등록되었습니다."));

  }

  private static List<String> extractStringFields(
      CustomDosirakPreviewRequest customDosirakPreviewRequest) {
    List<String> answer = new ArrayList<>();

    for (Field field : CustomDosirakPreviewRequest.class.getDeclaredFields()) {
      field.setAccessible(true);
      try {
        Object value = field.get(customDosirakPreviewRequest);

        if (value instanceof String) {
          answer.add((String) value);
        } else if (value instanceof List<?>) {
          List<?> list = (List<?>) value;

          // 필드 이름이 "allergies"이고, 리스트가 비어있지 않으면
          if ("allergies".equals(field.getName()) && !list.isEmpty() && list.get(
              0) instanceof String) {
            String joinedList = String.join(", ", (List<String>) list);
            answer.add("현재 사용자가 가지고 있는 알러지: " + joinedList);
          }
        }

      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return answer;
  }
}
