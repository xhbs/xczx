package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
