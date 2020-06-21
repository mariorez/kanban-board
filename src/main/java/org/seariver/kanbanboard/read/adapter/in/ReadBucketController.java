package org.seariver.kanbanboard.read.adapter.in;

import org.seariver.kanbanboard.read.adapter.out.ReadBucketRepositoryImpl;
import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReadBucketController {

    private static final String BUCKETS_PATH = "v1/buckets";

    @Autowired
    private ReadBucketRepositoryImpl repository;

    @GetMapping(path = BUCKETS_PATH)
    public ResponseEntity<List<BucketDto>> listAll() {

        return ResponseEntity.ok(repository.findAll());
    }
}
