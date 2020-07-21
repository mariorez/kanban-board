package org.seariver.kanbanboard.write.domain.application;

import helper.TestHelper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.seariver.kanbanboard.write.domain.core.Bucket;
import org.seariver.kanbanboard.write.domain.core.WriteBucketRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
public class MoveBucketHandlerTest extends TestHelper {

    @Test
    void GIVEN_ValidPosition_MUST_UpdateBucketPosition() {

        // given
        var externalId = UUID.randomUUID();
        var position = faker.number().randomDouble(3, 1, 10);
        var command = new MoveBucketCommand(externalId.toString(), position);
        var repository = mock(WriteBucketRepository.class);
        var bucket = new Bucket().setExternalId(externalId).setPosition(123);
        when(repository.findByExteranlId(externalId)).thenReturn(Optional.of(bucket));

        // when
        var handler = new MoveBucketHandler(repository);
        handler.handle(command);

        // then
        verify(repository).findByExteranlId(externalId);
        verify(repository).update(bucket);
        assertThat(bucket.getExternalId()).isEqualTo(externalId);
        assertThat(bucket.getPosition()).isEqualTo(position);
    }
}
