package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageDao extends MongoRepository<CmsPage,String> {
    public CmsPage findByPageId(String pageId);
    public CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
    public int deleteByPageId(String pageId);
}
