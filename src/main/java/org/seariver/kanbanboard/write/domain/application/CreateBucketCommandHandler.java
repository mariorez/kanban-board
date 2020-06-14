package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.BucketRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateBucketCommandHandler {

    private final BucketRepository repository;

    public CreateBucketCommandHandler(BucketRepository repository) {
        this.repository = repository;
    }

    public void handle(CreateBucketCommand command) {

        var bucket = new Bucket()
            .setUuid(command.id())
            .setPosition(command.position())
            .setName(command.name());

        repository.create(bucket);
    }
}
