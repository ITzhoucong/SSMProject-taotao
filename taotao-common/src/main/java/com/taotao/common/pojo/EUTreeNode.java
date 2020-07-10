package com.taotao.common.pojo;

/**
 * @auther: ZhouCong
 * @date: Create in 2019/8/2 15:27
 * @description:
 */
public class EUTreeNode {
    private long id;
    private String text;
    private String state;

    public EUTreeNode() {
        this.id = id;
        this.text = text;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
