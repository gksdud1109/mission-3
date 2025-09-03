package com.back;

import com.back.domain.wiseSaying.controller.wiseSayingController;

import java.util.Scanner;

public class wiseSayingApp {

    private Scanner sc;
    private wiseSayingController wiseSayingController;

    public wiseSayingApp(){
        this.sc = AppContext.sc;
        this.wiseSayingController = AppContext.wiseSayingController;
    }

    public void run(){
        // 시작
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String nextCmd = sc.nextLine().trim();

            Rq rq = new Rq(nextCmd);
            String action = rq.getActionName();

            switch (action) {
                case "등록" -> wiseSayingController.register();
                case "목록" -> wiseSayingController.list();
                case "삭제" -> wiseSayingController.remove(rq);
                case "수정" -> wiseSayingController.update(rq);
                case "빌드" -> wiseSayingController.dataBuild();
                case "종료" -> {if(wiseSayingController.exit()) return;}
                default -> System.out.println("알 수 없는 명령입니다.");
            }


//            // 종료
//            if (nextCmd.equals("종료"))
//                break;
//            // 등록
//            else if (nextCmd.equals("등록"))
//                wiseSayingController.register();
//            // 목록
//            else if (nextCmd.equals("목록"))
//                wiseSayingController.list();
//            // 삭제
//            else if (nextCmd.startsWith("삭제?id=")) {
//                int id = Integer.parseInt(nextCmd.substring("삭제?id=".length()));
//                wiseSayingController.remove(id);
//            }
//            // 수정
//            else if (nextCmd.startsWith("수정?id=")) {
//                int id = Integer.parseInt(nextCmd.substring("수정?id=".length()));
//                wiseSayingController.update(id);
//            }
//            else if (nextCmd.equals("빌드")){
//                boolean result = wiseSayingController.dataBuild();
//                if(result) System.out.println("data.json 파일의 내용이 갱신되었습니다.");
//                else System.out.println("data.json 파일 갱신 실패ㅠㅠ");
//            }
//            else System.out.println("알 수 없는 명령입니다.");
        }
    }
}