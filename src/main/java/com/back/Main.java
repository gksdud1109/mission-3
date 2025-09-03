package com.back;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AppContext.init(new Scanner(System.in));
        new wiseSayingApp().run();
    }
}