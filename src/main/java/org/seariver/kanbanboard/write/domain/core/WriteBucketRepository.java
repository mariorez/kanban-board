package org.seariver.kanbanboard.write.domain.core;

import java.util.Optional;
import java.util.UUID;

public interface WriteBucketRepository {

    void create(Bucket bucket);

    void update(Bucket bucket);

    Optional<Bucket> findByUuid(UUID id);

    Optional<Bucket> findByUuidOrPosition(UUID id, double position);
}
