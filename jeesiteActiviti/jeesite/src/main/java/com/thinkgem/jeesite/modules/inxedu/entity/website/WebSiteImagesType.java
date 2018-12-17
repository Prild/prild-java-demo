package com.thinkgem.jeesite.modules.inxedu.entity.website;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 广告图片类型
 * @author www.inxedu.com
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class WebSiteImagesType extends DataEntity<WebSiteImagesType> implements Serializable{
	private static final long serialVersionUID = 1L;
	/**类型ID*/
	private int typeId;
	/**类型名*/
	private String typeName;

}
