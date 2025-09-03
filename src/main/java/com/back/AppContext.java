package com.back;

import com.back.domain.wiseSaying.controller.wiseSayingController;
import com.back.domain.wiseSaying.repository.wiseSayingFileRepository;
import com.back.domain.wiseSaying.repository.wiseSayingRepository;
import com.back.domain.wiseSaying.service.wiseSayingService;

import java.util.Scanner;

public class AppContext {
    public static Scanner sc;
    public static wiseSayingController wiseSayingController;
    public static wiseSayingService wiseSayingService;
    public static wiseSayingRepository wiseSayingRepository;

    public static void init(Scanner sc) {
        AppContext.sc = sc;
        AppContext.wiseSayingRepository = new wiseSayingFileRepository();
        AppContext.wiseSayingService = new wiseSayingService(AppContext.wiseSayingRepository);
        AppContext.wiseSayingController = new wiseSayingController(AppContext.sc, AppContext.wiseSayingService);
    }
    public static void init(){
        init(new Scanner(System.in));
    }
}
