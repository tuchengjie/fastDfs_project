package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
@CrossOrigin
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private CmsPageService cmsPageService;

    @Override
    @RequestMapping(value = "/list/{page}/{size}", method = RequestMethod.GET)
    public QueryResponseResult findPageQuery(@PathVariable("page") int page,
                                             @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return cmsPageService.findList(page, size, queryPageRequest);
    }

    @Override
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public CmsPageResult getById(@PathVariable("id") String id) {
        return cmsPageService.findById(id);
    }

    @Override
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    @Override
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public CmsPageResult edit(@RequestBody CmsPage cmsPage) {
        return cmsPageService.edit(cmsPage);
    }

    @Override
    @RequestMapping(value = "/del/{id}", method = RequestMethod.DELETE)
    public CmsPageResult delete(@PathVariable("id") String id) {
        return cmsPageService.delete(id);
    }
}
