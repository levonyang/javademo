package com.example.springes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest(classes = SpringEsApplication.class)
public class EsBlogRepositoryTest {

    @Autowired
    private EsBlogRepository esBlogRepository;

    /**
     * 初始化数据
     */
    @Test
    public void initRepositoryData() {
        esBlogRepository.deleteAll();
        esBlogRepository.save(new EsBlog("登鹤雀楼", "王之涣的登鹤雀楼", "白日依山尽，黄河入海流。欲穷千里目，更上一层楼。"));
        esBlogRepository.save(new EsBlog("相思", "王维的相思", "红豆生南国，春来发几枝。愿君多采撷，此物最相思。"));
        esBlogRepository.save(new EsBlog("静夜思", "李白的静夜思", "床前明月光，疑是地上霜。举头望明月，低头思故乡。"));
    }

    @Test
    public void testEsBlogRepository() {
        // PageRequest已经不用了，改成PageRequest.of()
        Pageable pageable = PageRequest.of(0, 20);
        String title = "思";
        String summary = "相思";
        String content = "相思";
        AtomicReference<EsBlog> esBlog = new AtomicReference<>();
        esBlogRepository.findAll().forEach(v -> {
            System.out.println(v.getId());
            esBlog.set(v);
        });
        EsBlog dasd = esBlog.get();
        dasd.setContent("dasdasdsadas");
        String id = dasd.getId();
        dasd.setId("'");
        System.out.println(dasd.getId());
        //通过ID进行删除
        esBlogRepository.delete(dasd);
        Optional<EsBlog> ddassad =esBlogRepository.findById(id);
        System.out.println(ddassad.isPresent());
        // esBlogRepository.delete();
        //       List<EsBlog> dsa= esBlogRepository.findDistinctByEsBlogtttt_Content(content);
        //       Page<EsBlog> page = esBlogRepository.
        //                findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining
        //                        (title, summary, content, pageable);
        //        // 使用JUnit断言
        //        assertThat(page.getTotalElements()).isEqualTo(2);
        //        System.out.println("Start--------------------1");
        //        for (EsBlog esBlog : page.getContent()){
        //            System.out.println(esBlog.toString());
        //        }
        //        System.out.println("End--------------------1");
        //
        //        title = "相思";
        //        page = esBlogRepository.
        //                findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining
        //                        (title, summary, content, pageable);
        //
        //        // 使用JUnit断言
        //        assertThat(page.getTotalElements()).isEqualTo(1);
        //        System.out.println("Start--------------------2");
        //        for (EsBlog esBlog : page.getContent()){
        //            System.out.println(esBlog.toString());
        //        }
        //        System.out.println("End--------------------2");
    }
}