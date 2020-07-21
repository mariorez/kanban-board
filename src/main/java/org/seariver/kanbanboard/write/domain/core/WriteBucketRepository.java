package org.seariver.kanbanboard.write.domain.core;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WriteBucketRepository {

    String ID_FIELD = "id";
    String EXTERNAL_ID_FIELD = "external_id";
    String POSITION_FIELD = "position";
    String NAME_FIELD = "name";
    String CREATED_AT_FIELD = "created_at";
    String UPDATED_AT_FIELD = "updated_at";

    void create(Bucket bucket);

    void update(Bucket bucket);

    Optional<Bucket> findByExteranlId(UUID externalId);

    List<Bucket> findByExternalIdOrPosition(UUID externalId, double position);
}
