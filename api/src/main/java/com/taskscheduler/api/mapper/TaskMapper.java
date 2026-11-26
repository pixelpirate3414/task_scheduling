package com.taskscheduler.api.mapper;

import com.taskscheduler.api.dto.response.TaskResponse;
import com.taskscheduler.api.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

	TaskResponse toResponse(Task task);
}
