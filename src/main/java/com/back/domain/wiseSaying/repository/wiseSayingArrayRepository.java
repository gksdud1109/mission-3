package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.wiseSay;

import java.util.ArrayList;

public class wiseSayingArrayRepository implements wiseSayingRepository{
    private ArrayList<wiseSay> db = new ArrayList<>();
    private int lastId = 0;

    public wiseSay save(String content, String author) {
        lastId++;
        wiseSay ws = new wiseSay(lastId, author, content);
        db.add(ws);
        return ws;
    }

    public ArrayList<wiseSay> findAll() {
        return new ArrayList<>(db);
    }

    public wiseSay findById(int id) {
        for(wiseSay ws : db){
            if(ws.getId() == id)
                return ws;
        }
        return null;
    }

    public boolean removeById(int id) {
        for (int i = 0; i < db.size(); i++) {
            if (db.get(i).getId() == id) {
                db.remove(i);
                return true;
            }
        }
        return false;
    }

    public wiseSay update(int id, wiseSay target) {
        for (int i = 0; i < db.size(); i++) {
            if (db.get(i).getId() == id) {
                db.set(i, target);
                return target;
            }
        }
        return null;
    }
    // 안만들었음
    @Override
    public boolean dataBuild() {
        return false;
    }
}
