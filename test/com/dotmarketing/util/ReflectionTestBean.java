package com.dotmarketing.util;

import com.dotcms.util.ReflectionUtils;

/**
 * Just a testing bean for the {@link ReflectionUtils}
 * @author jsanca
 */
public class ReflectionTestBean {

    private final String name;

    protected ReflectionTestBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        return this.name;
    }
}
