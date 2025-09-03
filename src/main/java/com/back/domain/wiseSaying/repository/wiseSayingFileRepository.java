package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.wiseSay;
import com.back.standard.util.jsonUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class wiseSayingFileRepository implements wiseSayingRepository {
    private final jsonUtil jsonUtil;

    public wiseSayingFileRepository() {
        try {
            this.jsonUtil = new jsonUtil();
            File folder = new File(jsonUtil.getFolder());

            if (!folder.exists()) folder.mkdirs();

            File lastId = new File(jsonUtil.getFolder() + "lastId.txt");
            if (!lastId.exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(lastId))) {
                    bw.write("0");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Repository 초기화 중 오류 발생", e);
        }
    }

    @Override
    public wiseSay save(String content, String author) {
        int newId = jsonUtil.getLastId() + 1;
        wiseSay ws = new wiseSay(newId, author, content);
        jsonUtil.saveLastId(newId);
        jsonUtil.saveWiseSay(ws);
        return ws;
    }

    @Override
    public ArrayList<wiseSay> findAll() {
        ArrayList<wiseSay> list = new ArrayList<>();
        File dir = new File(jsonUtil.getFolder());

        File[] files = dir.listFiles((d, name) -> name.endsWith(".json") && !name.equals("lastId.txt") && !name.equals("data.json"));
        if (files != null) {
            for (File file : files) {
                wiseSay ws = jsonUtil.getWiseSay(file);
                if (ws != null) list.add(ws);
            }
        }
        // id 내림차순 정렬
        list.sort((a, b) -> b.getId() - a.getId());
        return list;
    }

    @Override
    public wiseSay findById(int id) {
        File file = new File(jsonUtil.getFolder() + id + ".json");
        if (!file.exists()) return null;
        return jsonUtil.getWiseSay(file);
    }

    @Override
    public boolean removeById(int id) {
        File file = new File(jsonUtil.getFolder() + id + ".json");
        return file.exists() && file.delete();
    }

    @Override
    public wiseSay update(int id, wiseSay target) {
        File file = new File(jsonUtil.getFolder() + id + ".json");
        if (!file.exists()) return null;
        jsonUtil.saveWiseSay(target);
        return target;
    }

    @Override
    public boolean dataBuild() {
        return jsonUtil.buildTotal();
    }
//
//    @Override
//    public void clear() {
//        jsonUtil.clear();
//    }
}