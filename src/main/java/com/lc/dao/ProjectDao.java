package com.lc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lc.entity.Comment;
import com.lc.entity.Project;
import com.lc.entity.ProjectDetail;
import com.lc.utils.Page;
 
 
import com.lc.vo.ProAndUsers;
 import com.lc.vo.ProjectVo;

import com.lc.vo.UserAndSups;

@Mapper
public interface ProjectDao {

 
	@Insert("insert into project(name,user_id,state,type,target_money,end_time) values(#{project.name},#{project.userId},"
			+ "#{project.state},#{project.type},#{project.targetMoney},#{project.endTime})")
	@Options(useGeneratedKeys = true, keyProperty = "project.id", keyColumn = "id")//将主键回传对象
	void insertProject(@Param("project") Project project);

	@Select("<script>" + "select * from project" + "<if test = 'page.start!=null and page.count!=null'> "
			+ "limit #{page.start},#{page.count}" + "</if>" + "</script>")
	List<Project> listAllProjects(@Param("page") Page page);

	// 按照id获取项目
	@Select("select * from project where id = #{id}")
	Project getProjectById(@Param("id") int id);

	// 按照状态筛选项目
	@Select("select * from project where state = #{state}")
	List<Project> getProsByState(Integer state);

	// 按照类型筛选项目默认1
	@Select("select * from view_project where type = #{type} ")
	List<ProjectVo> getProjectByType(@Param("type") int type);
 
	// 按照项目名筛选项目
	@Select("select * from project where name like '%#{name}%'")
	List<Project> getProjectByName(@Param("name") String name);

	// 按照用户id获取其发起的项目
	@Select("select * from project where user_id = #{userId}")
	List<Project> getProjectByUserId(@Param("userId") int userId);

	@Update("update project set days = #{days} where id = #{id}")
	void updateDays(@Param("id") int id, @Param("days") int days);

	//状态更新
	@Update("update project set state = #{state}, remark=#{remark} where id = #{id}")
	void updateState(@Param("id") int id, @Param("state") int state,@Param("remark") String remark);

	@Delete("delete  from project where id = #{id}")
	void deleteById(int id);

	//插入项目详情
	@Insert("insert into project_detail(id,id_number,name,phone,title,purpose,location,labels,cover_story,additional) values(#{projectDetail.id},#{projectDetail.idNumber},"
			+ "#{projectDetail.name},#{projectDetail.phone},#{projectDetail.title},#{projectDetail.purpose},#{projectDetail.location},#{projectDetail.labels},#{projectDetail.coverStory},#{projectDetail.additional})")
	void insertProjectDetail(@Param("projectDetail") ProjectDetail projectDetail);

	@Select("select * from view_project where id = #{id}")
	ProjectVo getProjectVoById(@Param("id") int id);


 
	@Select("select * from pro_and_users where project_id = #{id}")
	List<ProAndUsers> getProAndUsers(Integer id);

	@Select("select count(*) from project")
	int total();

	// 新增
	@Insert("insert into comment(project_id,user_id,content) values(#{pid},#{uid},#{content})")
	void insertComment(@Param("pid") int pid, @Param("uid") int uid, @Param("content") String comment);

	// 查找项目的评论
	@Select("select * from view_comment where project_id=#{projectId}")
	List<Comment> selectComment(@Param("projectId") int projectId);

	// 查找项目对应的支持者列表
	@Select("select * from user_and_sups where project_id = #{proId}")
	List<ProAndUsers> getProAndUsersByProId(Integer proId);

	// 通过用户id获取其支持项目
	@Select("select * from user_and_sups where user_id = #{userId}")
	List<UserAndSups> getUserAndSups(Integer userId);
 
}
