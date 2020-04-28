package com.xuecheng.manage_cms_client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Testww {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void test1() {
        Optional<CmsPage> byId = cmsPageRepository.findById("5a795ac7dd573c04508f3a56");
        System.out.println("asd");
    }
}
