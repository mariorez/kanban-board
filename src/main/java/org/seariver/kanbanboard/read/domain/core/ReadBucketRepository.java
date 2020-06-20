package org.seariver.kanbanboard.read.domain.core;

import java.util.List;

public interface ReadBucketRepository {

    List<BucketDto> findAll();
}
