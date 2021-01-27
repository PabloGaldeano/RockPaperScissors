package com.rockpaperscissors.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rockpaperscissors.model.game.Round;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record GameProgressDTO(List<Round> gameProgress)
{

}
