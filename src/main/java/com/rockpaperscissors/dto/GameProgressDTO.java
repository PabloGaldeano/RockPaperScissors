package com.rockpaperscissors.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.rockpaperscissors.model.game.Round;

import java.util.List;
import java.util.Objects;

/**
 * This is an immutable representation of the game progress
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record GameProgressDTO(List<Round> gameProgress)
{
    public GameProgressDTO
    {
        Objects.requireNonNull(gameProgress);
    }
}
