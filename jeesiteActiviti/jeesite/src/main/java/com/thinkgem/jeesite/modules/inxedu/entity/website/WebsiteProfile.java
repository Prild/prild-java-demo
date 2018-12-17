package com.thinkgem.jeesite.modules.inxedu.entity.website;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 网站配置实体
 * @author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WebsiteProfile extends DataEntity<WebsiteProfile> implements Serializable {
	private static final long serialVersionUID = -6689726203603217717L;
	private int id_;
	private String type;//类型
	private String desciption;//描述内容JSON格式
	private String explain;//说明
	public int getId_() {
		return id_;
	}
	public void setId_(int id_) {
		this.id_ = id_;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesciption() {
		return desciption;
	}
	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	
}
