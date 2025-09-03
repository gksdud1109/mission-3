package com.back.repository;

import com.back.AppConfig;
import com.back.domain.wiseSaying.entity.wiseSay;
import com.back.domain.wiseSaying.repository.wiseSayingFileRepository;
import com.back.standard.util.jsonUtil;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingFileRepositoryTest {

    private wiseSayingFileRepository repository;
    private jsonUtil jsonUtil;
    private Path baseDir;

    @BeforeAll
    static void beforeAll() {
        AppConfig.setTestMode();
    }

    @BeforeEach
    void setUp() throws IOException {
        jsonUtil = new jsonUtil();
        baseDir = Paths.get(jsonUtil.getFolder());
        Files.createDirectories(baseDir);
        cleanDir();
        jsonUtil.saveLastId(0);
        repository = new wiseSayingFileRepository();
    }

    @AfterEach
    void tearDown() throws IOException {
        cleanDir();
        jsonUtil.saveLastId(0);
    }

    private void cleanDir() throws IOException {
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
    @DisplayName("명언 등록")
    void ti() {
        wiseSay saved = repository.save("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있어.", "괴테");
        assertThat(saved.getId()).isEqualTo(1);

        wiseSay found = repository.findById(1);
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1);
        assertThat(found.getContent()).isEqualTo("꿈을 지녀라. 그러면 어려운 현실을 이길 수 있어.");
        assertThat(found.getAuthor()).isEqualTo("괴테");
    }

    @Test
    @DisplayName("목록 출력시에 id 내림차순")
    void t2() {
        repository.save("A", "a");
        repository.save("B", "b");
        repository.save("C", "c");

        ArrayList<wiseSay> found = repository.findAll();
        assertThat(found).hasSize(3);
        assertThat(found.get(0).getId()).isEqualTo(3);
        assertThat(found.get(1).getId()).isEqualTo(2);
        assertThat(found.get(2).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("명언 수정")
    void t3() {
        repository.save("세상은 돌고도네.", "uncoolclub");
        wiseSay target = new wiseSay(1, "newAuthor", "newContent");

        wiseSay update = repository.update(1, target);
        assertThat(update).isNotNull();

        wiseSay found = repository.findById(1);
        assertThat(found.getAuthor()).isEqualTo("newAuthor");
        assertThat(found.getContent()).isEqualTo("newContent");
    }

    @Test
    @DisplayName("명언 삭제")
    void t4() {
        repository.save("새벽의 감촉", "언쿨클럽");
        repository.save("하지만 씽 얼롱~", "언쿨클럽2");

        boolean ok1 = repository.removeById(1);
        assertThat(ok1).isTrue();
        assertThat(repository.findById(1)).isNull();

        boolean ok2 = repository.removeById(999);
        assertThat(ok2).isFalse();
    }

    @Test
    @DisplayName("빌드 테스트")
    void t5(){
        repository.save("내 어깨에 기대어 웃던", "나상현씨");
        repository.save("너의 눈동자만 내 맘을 흔들어", "나상현씨");

        boolean buildDone = repository.dataBuild();
        assertThat(buildDone).isTrue();

        Path dataJson = Paths.get(new jsonUtil().getFolder(), "data.json");
        assertThat(Files.exists(dataJson)).isTrue();
    }
}
