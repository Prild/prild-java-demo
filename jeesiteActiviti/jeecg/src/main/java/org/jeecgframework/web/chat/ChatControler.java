package org.jeecgframework.web.chat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.util.ResourceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
@RequestMapping("/chat")
public class ChatControler extends BaseController {
	private static final Logger logger = Logger.getLogger(ChatControler.class);
	/**
	 * 获取图片流/获取文件用于下载
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value="/imController2/showOrDownByurl",method = RequestMethod.GET)
	public void getImgByurl(HttpServletResponse response,HttpServletRequest request) throws Exception{
		String flag=request.getParameter("down");//是否下载否则展示图片
		String dbpath = request.getParameter("dbPath");
		if("1".equals(flag)){
			response.setContentType("application/x-msdownload;charset=utf-8");
			String fileName=dbpath.substring(dbpath.lastIndexOf(File.separator)+1);

			String userAgent = request.getHeader("user-agent").toLowerCase();
			if (userAgent.contains("msie") || userAgent.contains("like gecko") ) {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			}else {  
				fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");  
			} 
			response.setHeader("Content-disposition", "attachment; filename="+ fileName);

		}else{
			response.setContentType("image/jpeg;charset=utf-8");
		}
	
		InputStream inputStream = null;
		OutputStream outputStream=null;
		try {
			String localPath=ResourceUtil.getConfigByName("webUploadpath");
			String imgurl = localPath+File.separator+dbpath;
			inputStream = new BufferedInputStream(new FileInputStream(imgurl));
			outputStream = response.getOutputStream();
			byte[] buf = new byte[1024];
	        int len;
	        while ((len = inputStream.read(buf)) > 0) {
	            outputStream.write(buf, 0, len);
	        }
	        response.flushBuffer();
		} catch (Exception e) {
			logger.info("--通过流的方式获取文件异常--"+e.getMessage());
		}finally{
			if(inputStream!=null){
				inputStream.close();
			}
			if(outputStream!=null){
				outputStream.close();
			}
		}
	}
}
