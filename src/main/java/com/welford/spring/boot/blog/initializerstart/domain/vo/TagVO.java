package com.welford.spring.boot.blog.initializerstart.domain.vo;

import java.io.Serializable;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public class TagVO implements Serializable {
    private String name;
    private Long count;

    public TagVO(String name, Long count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
