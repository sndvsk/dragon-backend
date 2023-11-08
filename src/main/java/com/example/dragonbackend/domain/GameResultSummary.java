package com.example.dragonbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameResultSummary {

    private int wins;
    private int losses;
    private DoubleSummaryStatistics scoreStatistics;
    private List<String> successGameIds;
    private List<String> failedGameIds;

}
