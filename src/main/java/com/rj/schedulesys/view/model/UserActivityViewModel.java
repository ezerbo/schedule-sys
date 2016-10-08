package com.rj.schedulesys.view.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserActivityViewModel {
	private Integer schedulesCreated;
	private Integer schedulesUpdated;
}
