package com.incito.interclass.app;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.incito.interclass.app.result.ApiResult;
import com.incito.interclass.business.cloud.PaperWorkService;
import com.incito.interclass.common.BaseCtrl;
import com.incito.interclass.entity.cloud.PaperWork;
import com.incito.interclass.entity.cloud.TestEntity;

@RestController
@RequestMapping("/api/paper")
public class PaperWorkCtrl extends BaseCtrl {

	@Autowired
	private PaperWorkService paperService;

	/**
	 * 保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addlist", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView addList(@RequestBody List<PaperWork> paperWorks,
			@RequestParam() MultipartFile file) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					// 接收表单里的参数
					if (item.getFieldName().equals("json")) {
						String json = item.getString("UTF-8");
						System.out.println("接收参数json：" + json);
					}
				} else {
					// 存储文件
					FileUtils.copyInputStreamToFile(item.getInputStream(),
							new File(request.getRealPath("/") + "upload/"
									+ item.getFieldName()));
				}
			}

		}
		int count = paperService.savePaperWork(paperWorks);
		ModelAndView mv = new ModelAndView();
		mv.addObject("code", "0");
		mv.addObject("count", count);
		return mv;
	}

	/**
	 * 保存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public String save(@RequestBody List<PaperWork> paperWorks) {
		return renderJSONString(ApiResult.SUCCESS);
	}

	/**
	 * 测试
	 * 
	 * @return
	 */
	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public String test(@RequestBody List<TestEntity> tests) {
		return renderJSONString(ApiResult.SUCCESS);
	}
}
