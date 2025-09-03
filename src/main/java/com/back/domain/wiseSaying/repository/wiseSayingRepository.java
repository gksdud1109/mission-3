package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.wiseSay;

import java.util.ArrayList;

public interface wiseSayingRepository {
    wiseSay save(String content, String author);
    ArrayList<wiseSay> findAll();
    wiseSay findById(int id);
    boolean removeById(int id);
    wiseSay update(int id, wiseSay target);
    boolean dataBuild();
//    void clear();
}
