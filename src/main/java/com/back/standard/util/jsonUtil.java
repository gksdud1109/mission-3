package com.back.standard.util;

import com.back.AppConfig;
import com.back.domain.wiseSaying.entity.wiseSay;

import java.io.*;
import java.util.ArrayList;

public class jsonUtil {
    private final String FOLDER ;

    public jsonUtil(){
        String mode = AppConfig.getMode();
        this.FOLDER = mode + "/db/wiseSaying/";
    }

    public String getFolder(){
        return this.FOLDER;
    }

    public int getLastId() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FOLDER + "lastId.txt"))) {
            return Integer.parseInt(reader.readLine().trim());
        }
        catch(IOException e){
            return -1;
        }
    }

    public void saveLastId(int id) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FOLDER + "lastId.txt"))) {
            bw.write(String.valueOf(id));
        }
        catch(IOException e){
            System.err.println("lastID 저장 실패");
        }
    }

    public void saveWiseSay(wiseSay ws) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FOLDER + ws.getId() + ".json"))) {
            bw.write("{\n");
            bw.write("  \"id\": " + ws.getId() + ",\n");
            bw.write("  \"content\": \"" + escape(ws.getContent()) + "\",\n");
            bw.write("  \"author\": \"" + escape(ws.getAuthor()) + "\"\n");
            bw.write("}");
        }
        catch(IOException e){
            System.err.println("파일 저장 실패");
        }
    }

    public wiseSay getWiseSay(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int id = -1;
            String author = "";
            String content = "";
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"id\"")) {
                    int colonIdx = line.indexOf(":");
                    String value = line.substring(colonIdx + 1).trim();
                    value = value.replace(",", ""); // 쉼표 제거
                    id = Integer.parseInt(value);

                } else if (line.startsWith("\"content\"")) {
                    int colonIdx = line.indexOf(":");
                    String value = line.substring(colonIdx + 1).trim();
                    content = cleanJsonString(value);

                } else if (line.startsWith("\"author\"")) {
                    int colonIdx = line.indexOf(":");
                    String value = line.substring(colonIdx + 1).trim();
                    author = cleanJsonString(value);
                }
            }
            return new wiseSay(id, author, content);
        }
        catch (IOException e) {
            System.err.println("파일 읽기 실패: " + file.getName());
            return null;
        }
    }

    public boolean buildTotal() {
        ArrayList<wiseSay> wiseList = new ArrayList<>();
        File dir = new File(FOLDER);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json") && !name.equals("lastId.txt") && !name.equals("data.json"));
        if (files == null) return false;

        for (File f : files) {
            wiseSay ws = getWiseSay(f);
            if (ws != null) wiseList.add(ws);
        }
        wiseList.sort((a, b) -> Integer.compare(a.getId(), b.getId()));

        File out = new File(FOLDER + "data.json");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
            writer.write("[\n");
            for (int i = 0; i < wiseList.size(); i++) {
                wiseSay ws = wiseList.get(i);
                writer.write("  {\n");
                writer.write("    \"id\": " + ws.getId() + ",\n");
                writer.write("    \"content\": \"" + escape(ws.getContent()) + "\",\n");
                writer.write("    \"author\": \"" + escape(ws.getAuthor()) + "\"\n");
                writer.write("  }");

                if (i < wiseList.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.err.println("data.json 저장 실패");
            return false;
        }
        return true;
    }

    private String cleanJsonString(String raw) {
        raw = raw.trim();

        // 마지막에 쉼표가 있다면 제거
        if (raw.endsWith(",")) {
            raw = raw.substring(0, raw.length() - 1);
        }

        // 이스케이프 문자 처리
        raw = raw.replace("\\\"", "\"").replace("\\\\", "\\");

        // 앞뒤 쌍따옴표 제거
        if (raw.startsWith("\"") && raw.endsWith("\"")) {
            raw = raw.substring(1, raw.length() - 1);
        }

        return raw;
    }



    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
