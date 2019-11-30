package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 18:33.
 * @Modified By:
 */
@Data
@ToString
public class QueryResult<T> {
    //数据总数
    private long total;
    //数据列表
    private List<T> list;

    public QueryResult(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public QueryResult() {
        super();
    }
}
