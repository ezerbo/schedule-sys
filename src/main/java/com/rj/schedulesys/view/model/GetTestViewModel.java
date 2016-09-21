package com.rj.schedulesys.view.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTestViewModel {

	private Long id;
	
	private String name;

	private Boolean allowNotApplicable;

	private Boolean hasCompletedDate;

	private Boolean hasExpirationDate;
	
	private List<TestSubCategoryViewModel> subcategories;
}
