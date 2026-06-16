package com.jsa.dto.response;

/**
 * 运动项目视图（见 docs/04 3.2），用于打卡页下拉选择。
 */
public class SportVO {

    private Long id;
    private String name;
    private String description;

    public SportVO() {
    }

    public SportVO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
