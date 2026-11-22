package com.taskscheduller.api.mapper;

import com.taskscheduller.api.dto.response.UserResponse;
import com.taskscheduller.api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface UserMapper {

	UserResponse toResponse(User user);
}
