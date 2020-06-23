package org.seariver.kanbanboard.write.domain.core;

import java.util.Optional;
import java.util.UUID;

public interface WriteCardRepository {

    void create(Card card);

    Optional<Card> findByUuid(UUID uuid);
}
