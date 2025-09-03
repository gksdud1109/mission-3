package com.back.domain.wiseSaying.controller;

import com.back.Rq;
import com.back.domain.wiseSaying.entity.wiseSay;
import com.back.domain.wiseSaying.service.wiseSayingService;

import java.util.Scanner;

public class wiseSayingController {
    private Scanner sc;
    private wiseSayingService service;

    public wiseSayingController(Scanner sc, wiseSayingService service) {
        this.sc = sc;
        this.service = service;
    }

    public void register() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        wiseSay ws = service.write(content, author);
        System.out.println(ws.getId() + "번 명언이 등록되었습니다");
    }

    public void list() {
        System.out.println("번호 / 작가 / 명언\n------------");
        for (wiseSay ws : service.findAll())
            System.out.println(ws.getId() + " / " + ws.getAuthor() + " / " + ws.getContent());
    }

    public void remove(Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
            return;
        }
        boolean deleted = service.removeById(id);
        if (!deleted) System.out.println(id + "번 명언은 존재하지 않습니다.");
        else System.out.println(id + "번 명언이 삭제되었습니다.");
    }

    public void update(Rq rq) {
        int id = rq.getParamAsInt("id", -1);
        if (id == -1) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
            return;
        }
        wiseSay target = service.findById(id);
        // 수정하려는 명언이 존재하지 않음.
        if (target == null) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
            return;
        }
        System.out.println("명언(기존) : " + target.getContent());
        System.out.print("명언 : ");
        target.setContent(sc.nextLine());

        System.out.println("작가(기존) : " + target.getAuthor());
        System.out.print("작가 : ");
        target.setAuthor(sc.nextLine());

        service.update(id, target);
    }

    // data.json 파일로 빌드
    public void dataBuild() {
        boolean ok = service.dataBuild();
        if (ok) System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        else System.out.println("data.json 파일 갱신 실패ㅠㅠ");
    }

    public boolean exit(){
        System.out.println("명언 앱을 종료합니다.");
        return true;
    }
}
