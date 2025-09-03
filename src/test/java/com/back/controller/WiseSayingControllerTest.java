package com.back.controller;

import com.back.AppConfig;
import com.back.AppTestRunner;
import com.back.standard.util.jsonUtil;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {

    private static jsonUtil jsonUtil;
    private static Path baseDir;


    @BeforeAll
    static void beforeAll() throws IOException {
        AppConfig.setTestMode();
        jsonUtil = new jsonUtil();
        baseDir = Paths.get(jsonUtil.getFolder());
        Files.createDirectories(baseDir);
    }

    @BeforeEach
    void setUp() throws IOException {
        cleanDir();
        jsonUtil.saveLastId(0);
    }

    @AfterEach
    void tearDown() throws IOException {
        cleanDir();
        jsonUtil.saveLastId(0);
    }

    private void cleanDir() throws IOException {
        if (!Files.exists(baseDir)) return;
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(baseDir)) {
            for (Path p : ds) {
                String name = p.getFileName().toString();
                if (name.matches("\\d+\\.json") || name.equals("data.json")) {
                    Files.deleteIfExists(p);
                }
            }
        }
    }


    @Test
    @DisplayName("등록")
    void t1() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);
        assertThat(out)
                .contains("명령) ")
                .contains("명언 : ")
                .contains("작가 : ");
    }

    @Test
    @DisplayName("등록 시 명언 번호 노출")
    void t2() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);
        assertThat(out).contains("1번 명언이 등록되었습니다");
    }

    @Test
    @DisplayName("등록할때마다 생성되는 명언번호가 증가")
    void t3() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                """);

        assertThat(out).contains("1번 명언이 등록되었습니다");
        assertThat(out).contains("2번 명언이 등록되었습니다");
    }

    @Test
    @DisplayName("목록")
    void t4() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                """);
        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("------------")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("삭제?id=1")
    void t5() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                목록
                """);
        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("삭제 두번 요청에 대한 예외 처리")
    void t6() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                삭제?id=1
                """);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정: 없는 id")
    void t7_update_missing() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=3
                """);

        assertThat(out).contains("3번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정: 1번 수정 후 목록 확인")
    void t8_update_then_list() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=1
                너 자신을 알라
                소크라테스
                목록
                """);

        assertThat(out)
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.")
                .contains("1 / 소크라테스 / 너 자신을 알라");
    }

    @Test
    @DisplayName("빌드: data.json 생성 메시지")
    void t9_build_success_message() {
        String out = AppTestRunner.run("""
                등록
                A
                a
                빌드
                """);

        assertThat(out).contains("data.json 파일의 내용이 갱신되었습니다.");
        // 필요 시 파일 존재도 확인 가능:
        Path dataJson = baseDir.resolve("data.json");
        assertThat(Files.exists(dataJson)).isTrue();
    }

    @Test
    @DisplayName("종료: 종료 메시지")
    void t10_exit_message() {
        String out = AppTestRunner.run("""
                종료
                """);
        assertThat(out).contains("명언 앱을 종료합니다.");
    }
}
