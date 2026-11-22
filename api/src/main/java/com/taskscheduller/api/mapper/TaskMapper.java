package com.taskscheduller.api.mapper;

import com.taskscheduller.api.dto.response.TaskResponse;
import com.taskscheduller.api.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

	TaskResponse toResponse(Task task);
}
