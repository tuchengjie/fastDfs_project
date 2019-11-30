package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.utils.CopyProject;
import com.xuecheng.manage_cms.dao.CmsPageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class CmsPageService {
    @Autowired
    private CmsPageDao cmsPageDao;
    @Autowired
    private MongoTemplate mongoTemplate;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //暂时采用测试数据，测试接口是否可以正常运行
        if(page < 1 || size < 1) {
            throw new RuntimeException(page < 1? "当前页码最小为1":"每页显示记录数最小为1");
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CmsPage> all = null;
        if (!StringUtils.isEmpty(queryPageRequest)) {
            CmsPage cmsPage = new CmsPage();
            CopyProject.copyPropertiesIgnoreNull(queryPageRequest,cmsPage);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains())
                    .withMatcher("pageHtml", ExampleMatcher.GenericPropertyMatchers.contains());
            Example<CmsPage> example = Example.of(cmsPage,matcher);
            all = cmsPageDao.findAll(example,pageable);
        }else {
            all = cmsPageDao.findAll(pageable);
        }
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,
                new QueryResult(all.getTotalElements(), all.getContent()));
        return queryResponseResult;
    }


    public CmsPageResult findById(String id) {
        CmsPage cmsPage = cmsPageDao.findByPageId(id);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
    }

    public CmsPageResult add(CmsPage cmsPage) {
        //校验页面是否存在，根据页面名称、站点Id、页面webpath查询
        CmsPage cmsPage1 = cmsPageDao.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(StringUtils.isEmpty(cmsPage1)) {
            cmsPage.setPageId(null);
            cmsPageDao.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }else {
            throw new RuntimeException("添加失败，页面已存在");
        }
    }


    public CmsPageResult edit(CmsPage cmsPage) {
        cmsPageDao.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
    }

    public CmsPageResult delete(String id) {
        int result = cmsPageDao.deleteByPageId(id);
        if(result > 0) {
            return new CmsPageResult(CommonCode.SUCCESS,null);
        }else {
            throw new RuntimeException("删除失败，页面不存在");
        }
    }
    
}
