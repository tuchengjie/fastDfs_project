package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;

public interface CmsPageControllerApi {
    public QueryResponseResult findPageQuery(int page, int size, QueryPageRequest queryPageRequest);
    public CmsPageResult getById(String id);
    public CmsPageResult add(CmsPage cmsPage);
    public CmsPageResult edit(CmsPage cmsPage);
    public CmsPageResult delete(String id);
}

