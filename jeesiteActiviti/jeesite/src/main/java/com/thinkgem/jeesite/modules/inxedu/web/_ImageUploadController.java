package com.thinkgem.jeesite.modules.inxedu.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.thinkgem.jeesite.common.constants.CommonConstants;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping("${adminPath}/image_")
public class _ImageUploadController extends BaseController {
	private String getProjectRootDirPath(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/");
	}
	// start:******************************gok4**************************************************
	@RequestMapping(value = "/gok4", method = { RequestMethod.POST })//http://127.0.0.1:18080/jeesite/a/image_/gok4&param=teacher&fileType=jpg,gif,png,jpeg&pressText=undefined
	public String gok44(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "uploadfile", required = true) MultipartFile uploadfile,
			@RequestParam(value = "param", required = false) String param,
			@RequestParam(value = "fileType", required = true) String fileType) 
	{
        long maxSize = 4096000L;
        if(uploadfile.getSize() > maxSize)
            return responseErrorData(response, 1, "上传的图片大小不能超过4M。");
        String type[] = fileType.split(",");
        setFileTypeList(type);
//        ext = FileUploadUtils.getSuffix(uploadfile.getOriginalFilename());
		String uploadFileName = uploadfile.getOriginalFilename();
		String ext = uploadFileName.substring(uploadFileName.indexOf("."));
        if(!fileType.contains(ext) || "jsp".equals(ext))
            return responseErrorData(response, 1, "文件格式错误，上传失败。");
        try
        {
            String filePath = getPath(request, ext, param);
            File file = new File(getProjectRootDirPath(request) + filePath);
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            uploadfile.transferTo(file);
            return responseData(filePath, 0, "上传成功", response);
        }
        catch(Exception e)
        {
            logger.error("gok4()--error", e);
        }
        return responseErrorData(response, 2, "系统繁忙，上传失败");
    }
	
	// end:******************************gok4**************************************************
    
    
	// start:******************************keupload**************************************************
	@RequestMapping("/keupload")
	public String kindEditorUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam("param")String param, @RequestParam("fileType")String fileType, @RequestParam("pressText")String pressText,@RequestParam("imgFile")MultipartFile imgFile) {
		//没有图片
		if (imgFile == null)
			try {
				return responseData("", 1, "请选择上传的文件，上传失败", response);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		//图片大小超过4m
		long maxSize = 4096000L;
		if (imgFile.getSize() > maxSize)
			return responseErrorData(response, 1, "上传的图片大小不能超过4M。");
		//判断格式是否合乎规定，再执行
		String type[] = fileType.split(",");
		setFileTypeList(type);
//		String ext = FileUploadUtils.getSuffix(imgFile.getOriginalFilename());
		String uploadFileName = imgFile.getOriginalFilename();
		String ext = uploadFileName.substring(uploadFileName.indexOf("."));
		if (!fileType.contains(ext) || "jsp".equals(ext))
			return responseErrorData(response, 1, "文件格式错误，上传失败。");
		try {
			String filePath = getPath(request, ext, param);
			File file = new File((new StringBuilder()).append(getProjectRootDirPath(request)).append(filePath).toString());
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			imgFile.transferTo(file);
			return responseData(filePath, 0, "上传成功", response);
		} catch (Exception e) {
			logger.error("kindEditorUpload()--error", e);
		}
		return responseErrorData(response, 2, "系统繁忙，上传失败。");
	}

	// 响应信息
	public String responseData(String path, int error, String message, HttpServletResponse response) throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("url", path);
		map.put("error", Integer.valueOf(error));
		map.put("message", message);
		response.getWriter().write(gson.toJson(map));
		return null;
	}

	// 响应错误信息
	public String responseErrorData(HttpServletResponse response, int error, String message) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("error", Integer.valueOf(error));
			map.put("message", message);
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().print(gson.toJson(map));
			response.getWriter().flush();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	// 得到路径
	private String getPath(HttpServletRequest request, String ext, String param) {
		String filePath = "/images/upload/";
		if (param != null && param.trim().length() > 0)
			filePath = (new StringBuilder()).append(filePath).append(param).toString();
		else
			filePath = (new StringBuilder()).append(filePath).append(CommonConstants.projectName).toString();
		filePath = (new StringBuilder()).append(filePath).append("/").append(DateUtils.formatDate(new Date(), "yyyyMMdd")).append("/").append(System.currentTimeMillis()).append(".")
				.append(ext).toString();
		return filePath;
	}

	// 数组转list
	public void setFileTypeList(String type[]) {
		ArrayList<String> fileTypeList = new ArrayList<String>();
		int i = type.length;
		for (int j = 0; j < i; j++) {
			fileTypeList.add(type[j]);
		}

	}

	// end:******************************keupload**************************************************

}
