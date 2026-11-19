package com.taskscheduller.api.mapper;

import com.taskscheduller.api.dto.UserDto;
import com.taskscheduller.api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface UserMapper {
	UserDto toDto(User user);

	User toEntity(UserDto user);
}
