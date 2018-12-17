package com.thinkgem.jeesite.modules.act.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.activiti.engine.ManagementService;
import org.activiti.engine.management.TableMetaData;
import org.activiti.engine.management.TablePage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.act.utils.Page_;
import com.thinkgem.jeesite.modules.act.utils.Page_Util;

/**
 * 数据库查询控制器
 * User: henryyan
 */
@Controller
@RequestMapping("${adminPath}/act/database")
public class DatabaseController extends BaseController{

    @Autowired
    ManagementService managementService;

    @RequestMapping("")
    public ModelAndView index(@RequestParam(value = "tableName", required = false) String tableName, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("chapter6/database");

        // 读取表
        Map<String, Long> tableCount = managementService.getTableCount();
        mav.addObject("tableCount", tableCount);

        // 读取表记录
        if (StringUtils.isNotBlank(tableName)) {
            TableMetaData tableMetaData = managementService.getTableMetaData(tableName);
            mav.addObject("tableMetaData", tableMetaData);
            Page_<Map<String, Object>> page = new Page_<Map<String, Object>>(10);
            int[] pageParams = Page_Util.init(page, request);
            TablePage tablePages = managementService.createTablePageQuery().tableName(tableName).listPage(pageParams[0], pageParams[1]);

            page.setResult(tablePages.getRows());
            page.setTotalCount(tableCount.get(tableName));
            mav.addObject("page", page);
        }
        return mav;
    }

}
