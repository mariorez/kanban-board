package org.seariver.kanbanboard.write.domain.core;

import java.util.Optional;
import java.util.UUID;

public interface BucketRepository {

    void create(Bucket bucket);

    Optional<Bucket> findByUuid(UUID id);
}
