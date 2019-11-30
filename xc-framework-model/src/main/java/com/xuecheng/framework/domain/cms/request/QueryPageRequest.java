package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

import javax.annotation.RegEx;
import java.io.Serializable;

/**
 * 封装cms查询条件
 */
@Data
@ToString
public class QueryPageRequest extends RequestData implements Serializable {

    //站点id
    private String siteId;
    //页面ID
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模版id
    private String templateId;
    
}
