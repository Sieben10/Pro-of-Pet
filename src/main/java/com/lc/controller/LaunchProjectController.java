package com.lc.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lc.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lc.dao.ImageDao;
import com.lc.entity.Image;
import com.lc.service.ProjectService;
import com.lc.utils.AuthorizationUtils;
import com.lc.vo.ProjectInfoVo;;

@Controller
@RequestMapping("/launch")
public class LaunchProjectController {
	private static Logger log = LoggerFactory.getLogger(LaunchProjectController.class);

	@Autowired
	private ProjectService projectService;
	
	@Autowired 
	AuthorizationUtils auth;

	@Autowired
	ImageDao newImageDao;

	@RequestMapping("/toLaunch")
	public String toLaunch(HttpSession session) {
		if (!auth.check(session))
			return "redirect:/login/toLogin";

		return "/fore/launch/launchproject";
	}

	@RequestMapping(value = "/launchClose", method = RequestMethod.POST)
	public String launchOpen(HttpSession session, HttpServletRequest request, ProjectInfoVo projectInfoVo)
			throws IllegalStateException, IOException {

		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		log.info(projectInfoVo.toString());
		log.info(params.getParameter("name"));
		
		if (!auth.check(session))
			return "redirect:/login/toLogin";

		 int uid = (int) session.getAttribute("myId");

		// 为projectInfoVo赋值
		projectInfoVo.setProName(params.getParameter("closeName"));
		projectInfoVo.setProIdNumber(params.getParameter("closeIdNumber"));
		projectInfoVo.setProPhoneNumber(params.getParameter("closePhoneNumber"));
		projectInfoVo.setProTitle(params.getParameter("closeTitle"));
		projectInfoVo.setProPurpose(params.getParameter("closePurpose"));
		projectInfoVo.setProCoverStory(params.getParameter("closeCoverStory"));
		projectInfoVo.setProTargetMoney(Integer.valueOf(params.getParameter("closeTargetMoney")));
		projectInfoVo.setProLastTime(Integer.valueOf(params.getParameter("closeLastTime")));
		projectInfoVo.setProLocation(params.getParameter("closeProvince") + params.getParameter("closeCity"));

		log.info(projectInfoVo.toString());
		int newPid = projectService.launchProject(uid, projectInfoVo);
		String myPid = Integer.toString(newPid);

		// 图片表
		// 封面图片——1
		// 描述图片——2
		// 身份证图片——3
		Image newImage = new Image();
		// 描述图片
		log.info("描述图片：");
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("closeDecImage");
		String prePath = "E:/springUpload/springUpload/";
		String decPath = prePath + "describePhoto/pid_" + myPid + "/";// 根据pid检索项目描述图片
 		File decDir = new File(decPath);
		if (!decDir.exists())
			decDir.mkdirs();
		MultipartFile file = null;
		log.info(Integer.toString(files.size()));
		for (int i = 0; i < files.size(); ++i) {
			file = files.get(i);
			if (!file.isEmpty()) {
				log.info(file.getOriginalFilename());
				String path = decPath + file.getOriginalFilename();
				log.info(path);
				file.transferTo(new File(path));//本质上还是流的处理，不过做了封装
				// 插入数据库图片表
				newImage.setName(file.getOriginalFilename());
				newImage.setType(2);
				newImage.setProjectId(Integer.valueOf(myPid));
				newImageDao.insertImage(newImage);
			}
			else{
				return "forward:/launch/error";

			}
		}

		// 身份证图片
		log.info("身份证正反面照片:");
		List<MultipartFile> idPhotos = ((MultipartHttpServletRequest) request).getFiles("closeIdPhoto");
		MultipartFile idPhoto = null;
		log.info(Integer.toString(idPhotos.size()));

		String idPath = prePath + "idPhoto/pid_" + myPid + "/";// 根据pid检索身份证图片
		File idDir = new File(idPath);
		if (!idDir.exists())
			idDir.mkdirs();
		for (int i = 0; i < idPhotos.size(); ++i) {
			idPhoto = idPhotos.get(i);
			if (!idPhoto.isEmpty()) {
				log.info(idPhoto.getOriginalFilename());
				String path = idPath + idPhoto.getOriginalFilename();
				log.info(path);
				idPhoto.transferTo(new File(path));
				// 插入数据库图片表
				newImage.setName(idPhoto.getOriginalFilename());
				newImage.setType(3);
				newImage.setProjectId(Integer.valueOf(myPid));
				newImageDao.insertImage(newImage);
			}
			else{
				return "forward:/launch/error";

			}
		}

		// 封面图片
		log.info("封面图片:");
		
		List<MultipartFile> coverPhotos = ((MultipartHttpServletRequest) request).getFiles("closeCoverPhoto");
		MultipartFile coverPhoto = coverPhotos.get(0);
		if (!coverPhoto.isEmpty()) {
			String coverPath = prePath + "coverPhoto/pid_" + myPid + "/";// 根据pid检索项目封面图片
			File coverDir = new File(coverPath);
			if (!coverDir.exists())
				coverDir.mkdirs();
			File coverFile = new File(coverPath + coverPhoto.getOriginalFilename());
			coverPhoto.transferTo(coverFile);
			// 插入数据库图片表
			newImage.setName(coverPhoto.getOriginalFilename());
			newImage.setType(1);
			newImage.setProjectId(Integer.valueOf(myPid));
			newImageDao.insertImage(newImage);
		}
		else{
			return "forward:/launch/error";

		}

		return "redirect:/fore/index";
	}

	@RequestMapping("/error")
	@ResponseBody
	public String error() {


		return "请务必完整填写项目信息！";
	}

}
