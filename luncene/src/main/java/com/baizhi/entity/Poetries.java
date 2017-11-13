package com.baizhi.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/8.
 */
public class Poetries implements Serializable{
    private Integer id;
    private Integer poet_id;
    private String content;
    private String title;
    private Date created_at;
    private Date update_at;
    private Poets poets;

    @Override
    public String toString() {
        return "Poetries{" +
                "id=" + id +
                ", poet_id=" + poet_id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", created_at=" + created_at +
                ", update_at=" + update_at +
                ", poets=" + poets +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPoet_id() {
        return poet_id;
    }

    public void setPoet_id(Integer poet_id) {
        this.poet_id = poet_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Date update_at) {
        this.update_at = update_at;
    }

    public Poets getPoets() {
        return poets;
    }

    public void setPoets(Poets poets) {
        this.poets = poets;
    }

    public Poetries(Integer id, Integer poet_id, String content, String title, Date created_at, Date update_at, Poets poets) {
        this.id = id;
        this.poet_id = poet_id;
        this.content = content;
        this.title = title;
        this.created_at = created_at;
        this.update_at = update_at;
        this.poets = poets;
    }

    public Poetries() {
    }
}
