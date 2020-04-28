package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.config.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@RestController
public class CmsPageController extends BaseController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService CmsPageService;

    @GetMapping("/cms/page/list/{page}/{size}")
    @Override
    public QueryResponseResult findList(@PathVariable int page, @PathVariable int size, QueryPageRequest queryPageRequest) {

        return CmsPageService.findList(page, size, queryPageRequest);
    }

    @PostMapping("/cms/page/add")
    @Override
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return CmsPageService.add(cmsPage);
    }

    @GetMapping("/cms/page/get/{id}")
    @Override
    public CmsPage findById(@PathVariable String id) {
        return CmsPageService.getById(id);
    }

    @PutMapping("/cms/page/edit/{id}")//这里使用put方法，http 方法中put表示更新
    @Override
    public CmsPageResult edit(@PathVariable String id, @RequestBody CmsPage cmsPage) {
        return CmsPageService.edit(id, cmsPage);
    }

    @DeleteMapping("/cms/page/delete/{id}")
    @Override
    public ResponseResult delete(@PathVariable String id) {
        return CmsPageService.delete(id);
    }


    @Override
    @PostMapping("/cms/page/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return CmsPageService.postPage(pageId);
    }

    //接收到页面id
    @RequestMapping(value = "/cms/preview/{pageId}", method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String pageId) {
        response.setHeader("Content-type", "text/html;charset=utf-8");
        String pageHtml = CmsPageService.getPageHtml(pageId);
        if (StringUtils.isNotEmpty(pageHtml)) {
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(pageHtml.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @PostMapping("/cms/page/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return CmsPageService.save(cmsPage);
    }

    @Override
    @PostMapping("/cms/page/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return CmsPageService.postPageQuick(cmsPage);
    }
}
