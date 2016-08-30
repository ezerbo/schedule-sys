(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('TestSubCategoryDialogController', TestSubCategoryDialogController);
	
	TestSubCategoryDialogController.$Inject = ['$state', '$stateParams', '$mdDialog', '$mdToast',
	                                           'TestsTestSubcategoryService', 'TestSubcategoryService'];
	
	function TestSubCategoryDialogController($state, $stateParams, $mdDialog, $mdToast,
			TestsTestSubcategoryService, TestSubcategoryService){
		var vm = this;
		
		vm.subCategory = {
				id: null,
				testId: $stateParams.id,
				name: null
				};
		
		vm.cancel = cancel;
		vm.showToast = showToast;
		vm.createOrUpdate = createOrUpdate;
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		if(angular.isDefined($stateParams.subCategoryId)){
			TestSubcategoryService.get({id : $stateParams.subCategoryId}, function(result){
				vm.subCategory = result;
			});
		}
		
		function createOrUpdate(){
			if(vm.subCategory.id === null){
				TestsTestSubcategoryService.save({id:$stateParams.id}, vm.subCategory, function(){
					vm.showToast("Sub category successfully created", 5000);
					$mdDialog.cancel();
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}else{
				TestsTestSubcategoryService.update({id:$stateParams.id, subCategoryId: $stateParams.subCategoryId},
						vm.subCategory, function(result) {
							vm.showToast("Sub category successfully updated", 5000);
							$mdDialog.cancel();
						}, function(result) {
							vm.showToast(result.data, 5000);
						});
			}
		}
		//TODO move this to common.js
		function showToast(textContent, delay){
			$mdToast.show($mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
	}
	
})();