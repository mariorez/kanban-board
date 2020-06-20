package org.seariver.kanbanboard.read.adapter.in;

import org.seariver.kanbanboard.read.domain.core.BucketDto;
import org.seariver.kanbanboard.read.adapter.out.ReadBucketRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/buckets")
public class ReadBucketController {

    @Autowired
    private ReadBucketRepositoryImpl repository;

    @GetMapping
    public ResponseEntity<List<BucketDto>> listAll() {

        return ResponseEntity.ok(repository.findAll());
    }
}
