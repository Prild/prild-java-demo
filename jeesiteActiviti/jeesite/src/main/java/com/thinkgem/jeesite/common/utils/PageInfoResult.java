package com.thinkgem.jeesite.common.utils;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PageInfoResult {

	// 定义jackson对象

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private Integer total;// 总记录数

	private List<?> rows;// 分页查询集合数

	private int page;// 页码

	public static int page_rows = 10;// 每页条数

	public PageInfoResult() {

	}

	public PageInfoResult(Integer total, List<?> rows) {

		this.total = total;

		this.rows = rows;

	}

	public PageInfoResult(Long total, List<?> rows) {

		this.total = total.intValue();

		this.rows = rows;

	}

	public Integer getTotal() {

		return total;

	}

	public void setTotal(Integer total) {

		this.total = total;

	}

	public List<?> getRows() {

		return rows;

	}

	public void setRows(List<?> rows) {

		this.rows = rows;

	}

	public int getPage() {

		return page;

	}

	public void setPage(int page) {

		this.page = page;

	}

	public int getPage_rows() {

		return page_rows;

	}

	/*
	 * 
	 * 计算页码总数
	 */

	public int total_page() {

		// 判断页码是否能和每页条数整除，能—>商为总页码，否->商+1为总页码

		return total % page_rows == 0 ? total / page_rows : total / page_rows + 1;

	}

	/**
	 * 
	 * Object是集合转化
	 * @param jsonData
	 *            json数据
	 * 
	 * @param clazz
	 * 
	 *            集合中的类型
	 * 
	 * @return
	 */

	public static PageInfoResult formatToList(String jsonData, Class<?> clazz) {

		try {

			JsonNode jsonNode = MAPPER.readTree(jsonData);

			JsonNode data = jsonNode.get("rows");

			List<?> list = null;

			if (data.isArray() && data.size() > 0) {

				list = MAPPER.readValue(data.traverse(),

				MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));

			}

			return new PageInfoResult(jsonNode.get("total").intValue(), list);

		} catch (Exception e) {

			return null;

		}

	}

}