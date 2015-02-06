package com.incito.interclass.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.incito.interclass.common.BaseCtrl;

@RestController
public class UpdateCtrl extends BaseCtrl{

	@RequestMapping("/update")
	public void Update(HttpServletRequest rsq,HttpServletResponse res){
		 try {
			res.sendRedirect("/admin/1.apk");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
