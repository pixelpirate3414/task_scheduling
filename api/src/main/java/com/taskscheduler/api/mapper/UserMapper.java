package com.taskscheduler.api.mapper;

import com.taskscheduler.api.dto.response.UserResponse;
import com.taskscheduler.api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface UserMapper {

	UserResponse toResponse(User user);
}
