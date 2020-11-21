package com.platform4j.arch.domain;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseObject implements Serializable {

    protected Date createTime;

    protected Date lastModifyTime;

    protected int version;

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
