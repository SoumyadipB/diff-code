package com.ericsson.isf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.WebNotificationModel;

public interface NotificationMapper {

	public List<WebNotificationModel> getNotificationsBySignum(@Param("signum") String signum,@Param("start") int start,@Param("length") int length,@Param("role") String role);

}
