package com.example.ai_tutor.domain.note.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StepOneListRes {
   private List<StepOneRes> stepOneRes;
}
