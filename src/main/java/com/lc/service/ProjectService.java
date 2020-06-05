package com.lc.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lc.dao.ImageDao;
import com.lc.dao.ProjectDao;
import com.lc.entity.Comment;
import com.lc.entity.Project;
import com.lc.entity.ProjectDetail;
import com.lc.exception.GlobalException;
import com.lc.result.CodeMsg;
import com.lc.utils.Page;

import com.lc.vo.ImageVo;
import com.lc.vo.ProAndUsers;
import com.lc.vo.ProjectInfoVo;
import com.lc.vo.ProjectVo;
import com.lc.vo.UserAndSups;

@Service
public class ProjectService {

	@Autowired
	ProjectDao projectDao;

	@Autowired
	ImageDao imageDao;

	@Autowired
	MessageService messageService;

	public List<ProjectVo> getProjectByType(Integer type) {
		return projectDao.getProjectByType(type);
	}

	public void insertProject(Project project) {
		projectDao.insertProject(project);
	}

	public Project getById(int id) {
		return projectDao.getProjectById(id);
	}

	public List<Project> getByUserId(int userId) {
		return projectDao.getProjectByUserId(userId);
	}

	public int total() {
		return projectDao.total();
	}

	public List<Project> listAllProjects(Page page) {
		return projectDao.listAllProjects(page);
	}

	public List<Project> getProsByState(Integer state) {
		// TODO Auto-generated method stub
		return projectDao.getProsByState(state);
	}

	public void updateState(Integer id, Integer state,String remark) {
		projectDao.updateState(id, state,remark);
	}

	// PROJECT_DETAIL
	/////////////////////////
	public void insertProjectDetail(ProjectDetail projectDetail) {
		projectDao.insertProjectDetail(projectDetail);
	}

	public ProjectVo getProjectVoById(Integer id) {
		return projectDao.getProjectVoById(id);
	}


	// launch project
	public int launchProject(Integer uid, ProjectInfoVo projectInfoVo) {
		if (projectInfoVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		org.slf4j.Logger log = LoggerFactory.getLogger(ProjectService.class);

		// PROJECT
		/////////////////////////
		Project newProject = new Project();
		newProject.setName(projectInfoVo.getProTitle());
		newProject.setUserId(uid);// 测试用
		newProject.setState(1);
		newProject.setType(1);
		newProject.setTargetMoney(projectInfoVo.getProTargetMoney());
		Date nowtime = new Date();
		java.sql.Date sqlTime = new java.sql.Date(nowtime.getTime());//取当前时间
		newProject.setCreateTime(sqlTime);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, projectInfoVo.getProLastTime());
		sqlTime = new java.sql.Date(calendar.getTime().getTime());
		newProject.setEndTime(sqlTime);
		insertProject(newProject);
		log.info(newProject.getId().toString());

		// PROJECT_DETAIL
		/////////////////////////
		ProjectDetail newProjectDetail = new ProjectDetail();
		newProjectDetail.setId(newProject.getId());
		newProjectDetail.setIdNumber(projectInfoVo.getProIdNumber());
		newProjectDetail.setName(projectInfoVo.getProName());
		newProjectDetail.setPhone(projectInfoVo.getProPhoneNumber());

		newProjectDetail.setTitle(projectInfoVo.getProTitle());
		newProjectDetail.setPurpose(projectInfoVo.getProPurpose());
		newProjectDetail.setLocation(projectInfoVo.getProLocation());
		newProjectDetail.setLabels(projectInfoVo.getProLabels());
		newProjectDetail.setCoverStory(projectInfoVo.getProCoverStory());

		insertProjectDetail(newProjectDetail);



		return newProject.getId();

	}


/*获取支持信息*/
	public List<ProAndUsers> getProAndUsers(Integer id) {
		return projectDao.getProAndUsers(id);
	}


	// 新增
	public void submitComment(Comment comment) {
		projectDao.insertComment(comment.getProjectId(), comment.getUserId(), comment.getContent());
	}

	public List<Comment> getProjectComment(int projectId) {
		return projectDao.selectComment(projectId);
	}


	public List<ImageVo> getProjectImage(int id) {
		return imageDao.getProjectImage(id);
	}


	public List<ProAndUsers> getProAndUsersByProId(Integer id) {
		return projectDao.getProAndUsersByProId(id);
	}
}
