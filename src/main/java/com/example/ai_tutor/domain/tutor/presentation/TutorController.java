package com.example.ai_tutor.domain.tutor.presentation;

import com.example.ai_tutor.domain.tutor.application.TutorService;
import com.example.ai_tutor.domain.tutor.dto.request.QuestionReq;
import com.example.ai_tutor.domain.tutor.dto.response.AnswerRes;
import com.example.ai_tutor.domain.tutor.dto.response.TutorRes;
import com.example.ai_tutor.global.config.security.token.CurrentUser;
import com.example.ai_tutor.global.config.security.token.UserPrincipal;
import com.example.ai_tutor.global.payload.ErrorResponse;
import com.example.ai_tutor.global.payload.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "챗봇", description = "챗봇 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tutor")
public class TutorController {

    private final TutorService tutorService;

    @Operation(summary = "챗봇 질의응답 시작", description = "챗봇 최초 시작 시 원문을 학습시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "원문 학습 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "원문 학습 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/{noteId}/init")
    public ResponseEntity<?> startChatting(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "노트의 Id를 입력해주세요.", required = true) @PathVariable Long noteId
    ){
        return tutorService.initializeChatting(userPrincipal, noteId);
    }

    @Operation(summary = "챗봇 질의응답 조회", description = "3단계 챗봇 질의응답 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TutorRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/{noteId}")
    public ResponseEntity<?> findQuestion(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "노트의 Id를 입력해주세요.", required = true) @PathVariable Long noteId
    ){
        return tutorService.getQuestionAndAnswer(userPrincipal, noteId);
    }

    @Operation(summary = "챗봇 질문", description = "3단계 챗봇에게 질문하고 답변을 받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AnswerRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/{noteId}/question")
    public ResponseEntity<?> sendQuestionToTutor(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "노트의 Id를 입력해주세요.", required = true) @PathVariable Long noteId,
            @Parameter(description = "Schemas의 QuestionReq를 확인해주세요", required = true) @RequestBody QuestionReq questionReq
            ) throws JsonProcessingException {
        return tutorService.questionToTutor(userPrincipal, noteId, questionReq);
    }

}
