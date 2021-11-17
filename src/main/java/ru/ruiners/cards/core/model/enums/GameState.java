package ru.ruiners.cards.core.model.enums;

import javax.persistence.Embeddable;

@Embeddable
public enum GameState {
    CREATED,
    IN_PROGRESS,
    CANCELLED,
    FINISHED
}
