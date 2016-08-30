(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('TestsController', TestsController);
	
	TestsController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog' ,'TestsService'];
	
	function TestsController($scope, $state, $mdToast, $mdDialog, TestsService){
		var vm = this;
		
		vm.allTests = null;
		vm.testsOnCurrentPage = null;
		
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceTestsArray = sliceTestsArray;
		vm.query = {
				limit: 10,
				page: 1
		};
		
		loadAll();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a test')
					.textContent('Are you sure you want to delete this test ?')
					.ariaLabel('Test deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				TestsService.remove({id:vm.selected[0].id}, function() {
					vm.testsOnCurrentPage.splice(vm.testsOnCurrentPage.indexOf(vm.selected[0]), 1);
					vm.allTests.splice(vm.allTests.indexOf(vm.selected[0]), 1);
					vm.editOrDelete = true;
					vm.showToast('Test ' + vm.selected[0].name + ' successfully deleted', 5000);
				}, function(result) {
					vm.showToast(result.data, 5000);
				});
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			TestsService.query({}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allTests = data;
			vm.testsOnCurrentPage = vm.sliceTestsArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function onPaginate(){
			vm.testsOnCurrentPage = vm.sliceTestsArray();
		}
		
		function sliceTestsArray(){
			return vm.allTests.slice(10 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
		}
	}
})();