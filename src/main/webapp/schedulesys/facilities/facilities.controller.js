(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('FacilitiesController', FacilitiesController);
	
	FacilitiesController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog' ,'FacilitiesService'];
	
	function FacilitiesController($scope, $state, $mdToast, $mdDialog, FacilitiesService){
		var vm = this;
		
		vm.allFacilities = null;
		vm.facilitiesOnCurrentPage = null;
		
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceFacilitiesArray = sliceFacilitiesArray;
		vm.showFacilityDialog = showFacilityDialog;
		
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
					.title('Delete a facility')
					.textContent('Are you sure you want to delete this facility ?')
					.ariaLabel('Facility deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				FacilitiesService.remove(
						{id:vm.selected[0].id},
						onDeleteSuccess,
						onDeleteFailure
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			FacilitiesService.query({}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allFacilities = data;
			vm.facilitiesOnCurrentPage = vm.sliceFacilitiesArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}
		
		function onDeleteSuccess (){
			vm.facilitiesOnCurrentPage.splice(vm.facilitiesOnCurrentPage.indexOf(vm.selected[0]), 1);
			vm.allFacilities.splice(vm.allFacilities.indexOf(vm.selected[0]), 1);
			vm.editOrDelete = true;
			vm.showToast('Facility ' + vm.selected[0].name + ' successfully deleted', 5000);
		}	
		
		function onDeleteFailure (){
			vm.showToast('Facility ' + vm.selected[0].name 
					+ ' could not be deleted, please make sure it has no staff members and past schedules', 5000);
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
			vm.facilitiesOnCurrentPage = vm.sliceFacilitiesArray();
		}
		
		function sliceFacilitiesArray(){
			var slicedArray = vm.allFacilities.slice(5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			console.log('Sliced array : ' + angular.toJson(slicedArray));
			return slicedArray;
		}
		
		function showFacilityDialog(ev) {
			$mdDialog.show({
				templateUrl: 'schedulesys/facilities/facility-dialog.html',
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