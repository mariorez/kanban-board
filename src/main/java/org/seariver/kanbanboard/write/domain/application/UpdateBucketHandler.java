package org.seariver.kanbanboard.write.domain.application;

import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.seariver.kanbanboard.write.domain.exception.BucketNotExistentException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.seariver.kanbanboard.write.domain.exception.DomainException.Error.BUCKET_NOT_EXIST;

@Service
public class UpdateBucketHandler implements Handler<UpdateBucketCommand> {

    private WriteBucketRepository repository;

    public UpdateBucketHandler(WriteBucketRepository repository) {
        this.repository = repository;
    }

    public void handle(UpdateBucketCommand command) {

        Optional<Bucket> bucketOptional = repository.findByUuid(command.getUuid());

        if (!bucketOptional.isPresent()) {
            throw new BucketNotExistentException(BUCKET_NOT_EXIST);
        }

        var bucket = bucketOptional.get();

        bucket.setName(command.getName());

        repository.update(bucket);
    }
}

