package org.seariver.kanbanboard.write.domain.application;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;
import org.seariver.kanbanboard.write.domain.exception.BucketNotExistentException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
public class UpdateBucketCommandHandlerTest {

    @Test
    void GIVEN_ValidCommand_MUST_UpdateBucketInDatabase() {

        // given
        var position = 200.25;
        var name = "SECOND-BUCKET";
        var uuid = UUID.fromString("6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e");
        var command = new UpdateBucketCommand(uuid, position, name);
        var repository = mock(WriteBucketRepository.class);
        Bucket bucket = new Bucket()
            .setId(1)
            .setUuid(uuid)
            .setPosition(123)
            .setName("FOOBAR");
        when(repository.findByUuid(uuid)).thenReturn(Optional.of(bucket));

        // when
        var handler = new UpdateBucketCommandHandler(repository);
        handler.handle(command);

        // then
        verify(repository).findByUuid(uuid);
        verify(repository).update(bucket);
        assertThat(bucket.getUuid()).isEqualTo(uuid);
        assertThat(bucket.getPosition()).isEqualTo(position);
        assertThat(bucket.getName()).isEqualTo(name);
    }

    @Test
    void GIVEN_NotExistentBucket_MUST_ThrowException() {

        // given
        var uuid = UUID.fromString("019641f6-6e9e-4dd9-ab02-e864a3dfa016");
        var command = new UpdateBucketCommand(uuid, 1, "WHATEVER");
        var repository = mock(WriteBucketRepository.class);
        when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

        // when
        var handler = new UpdateBucketCommandHandler(repository);
        var exception = assertThrows(BucketNotExistentException.class, () -> handler.handle(command));

        // then
        verify(repository).findByUuid(uuid);
        assertThat(exception.getMessage()).isEqualTo(String.format("Bucket not exist"));
    }
}
