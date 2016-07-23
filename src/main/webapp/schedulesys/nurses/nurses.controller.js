(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('NursesController', NursesController);
	
	NursesController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog' ,'NursesService'];
	
	function NursesController($scope, $state, $mdToast, $mdDialog, NursesService){
		var vm = this;
		
		vm.allNurses = null;
		vm.nursesOnCurrentPage = null;
		
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceNursesArray = sliceNursesArray;
		vm.showNurseDialog = showNurseDialog;
		
		vm.query = {
				order: 'name',
				limit: 5,
				page: 1
		};
		
		loadAll();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
			console.log('Selected items : ' + angular.toJson(vm.selected));
			console.log('Edit or delete : ' + vm.editOrDelete);
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a nurse')
					.textContent('Are you sure you want to delete this nurse ?')
					.ariaLabel('Nurse deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				NursesService.remove(
						{id:vm.selected[0].id},
						onDeleteSuccess,
						onDeleteFailure
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			NursesService.query({}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allNurses = data;
			vm.nursesOnCurrentPage = vm.sliceNursesArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}
		
		function onDeleteSuccess (){
			vm.nursesOnCurrentPage.splice(vm.nursesOnCurrentPage.indexOf(vm.selected[0]), 1);
			vm.allNurses.splice(vm.allNurses.indexOf(vm.selected[0]), 1);
			vm.editOrDelete = true;
			vm.showToast('Nurse ' + vm.selected[0].firstName + ' successfully deleted', 5000);
		}	
		
		function onDeleteFailure (){
			vm.showToast('Nurse ' + vm.selected[0].firstName 
					+ ' could not be deleted ', 5000);
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay)
					);
		}
		
		function onPaginate(){
			vm.nursesOnCurrentPage = vm.sliceNursesArray();
		}
		
		function sliceNursesArray(){
			var slicedArray = vm.allNurses.slice(5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			console.log('Sliced array : ' + angular.toJson(slicedArray));
			return slicedArray;
		}
		
		function showNurseDialog(ev) {
			$mdDialog.show({
				templateUrl: 'schedulesys/nurses/nurse-dialog.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose:true
			})
			.then(function() {
				//$scope.status = 'You said the information was "' + answer + '".';
			}, function() {
				//$scope.status = 'You cancelled the dialog.';
			});
		};
	
	}
	
})();