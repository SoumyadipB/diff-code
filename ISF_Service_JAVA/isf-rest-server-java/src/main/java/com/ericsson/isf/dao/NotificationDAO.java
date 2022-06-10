package com.ericsson.isf.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.NotificationMapper;
import com.ericsson.isf.model.WebNotificationModel;

@Repository
public class NotificationDAO {
	
	@Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

	public List<WebNotificationModel> getNotificationsBySignum(String signum, int start, int length, String role) {
		NotificationMapper notificationsMapper = sqlSession.getMapper(NotificationMapper.class);
		return notificationsMapper.getNotificationsBySignum(signum, start, length,role);
	}


}
