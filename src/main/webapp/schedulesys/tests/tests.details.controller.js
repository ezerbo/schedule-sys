(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('TestDetailsController', TestDetailsController);
	
	TestDetailsController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'TestsService', 'TestsTestSubcategoryService', 'TestSubcategoryService'];
	
	function TestDetailsController($state, $scope, $stateParams, $mdDialog, $mdToast,
			TestsService, TestsTestSubcategoryService, TestSubcategoryService){
		var vm = this;
		
		vm.allSubCategories = [];
		vm.subCategoriesOnCurrentPage = [];
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.getSubCategories = getSubCategories;
		vm.getSelectedTest = getSelectedTest;
		
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceSubCategoriesArray = sliceSubCategoriesArray;
		vm.query = {
				limit: 10,
				page: 1	
		};
		
		loadAll();
		getSubCategories();
		getSelectedTest();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a sub category')
					.textContent('Are you sure you want to delete this sub category ?')
					.ariaLabel('Sub category deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				TestSubcategoryService.remove({id:vm.selected[0].id},function (){
					vm.subCategoriesOnCurrentPage.splice(vm.subCategoriesOnCurrentPage.indexOf(vm.selected[0]), 1);
					vm.allSubCategories.splice(vm.allSubCategories.indexOf(vm.selected[0]), 1);
					vm.editOrDelete = true;
					vm.showToast('Sub category ' + vm.selected[0].name + ' successfully deleted', 5000);
				} , function(result) {
					vm.showToast(result.data, 5000);
				}
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			TestsTestSubcategoryService.query({id: $stateParams.id}, function(data) {
				vm.allSubCategories = data;
				vm.subCategoriesOnCurrentPage = vm.sliceSubCategoriesArray();
			}, function(status) {
				console.log('Error status : ' + status);
			});
		}
		
		function showToast(textContent, delay){
			$mdToast.show($mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function onPaginate(){
			vm.subCategoriesOnCurrentPage = vm.sliceSubCategoriesArray();
		}
		
		function sliceSubCategoriesArray(){
			return vm.allSubCategories.slice(
					10 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
		}
		
		function getSelectedTest(){
			TestsService.get({id:$stateParams.id}, function(result) {
				vm.test = result;
			});
		}
		
		function getSubCategories(){
			TestsTestSubcategoryService.query({id:$stateParams.id}, function(result) {
				vm.allSubCategories = result;
			});
		}
	}
})();