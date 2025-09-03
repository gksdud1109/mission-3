package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.wiseSay;
import com.back.domain.wiseSaying.repository.wiseSayingRepository;

import java.util.ArrayList;

public class wiseSayingService {
    private wiseSayingRepository repository;

    public wiseSayingService(wiseSayingRepository repository){
        this.repository = repository;
    }

    public wiseSay write(String content, String author) {
        return repository.save(content, author);
    }

    public ArrayList<wiseSay> findAll() {
        return repository.findAll();
    }

    public boolean removeById(int id) {
        return repository.removeById(id);
    }

    public wiseSay findById(int id) {
        return repository.findById(id);
    }

    public wiseSay update(int id, wiseSay target) {
        return repository.update(id, target);
    }

    public boolean dataBuild() {
        return repository.dataBuild();
    }
}
/*

 */