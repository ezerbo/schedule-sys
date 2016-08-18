(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilityDetailsController', FacilityDetailsController);
	
	FacilityDetailsController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'FacilitiesService', 'FacilitiesStaffMemberService','StaffMemberService'];
	
	function FacilityDetailsController($state,$scope, $stateParams, $mdDialog, $mdToast, FacilitiesService, FacilitiesStaffMemberService,StaffMemberService){
		var vm = this;
		vm.allStaffMembers = null;
		vm.StaffMembersOnCurrentPage = null;
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.getStaffMembers = getStaffMembers;
		vm.getSelectedFacility = getSelectedFacility;
		
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceStaffMemberArray = sliceStaffMemberArray;
		vm.showStaffMemberDialog = showStaffMemberDialog;
		vm.query = {
				limit: 5,
				page: 1	
		};
		
		loadAll();
		getStaffMembers();
		getSelectedFacility();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
			console.log('Selected items : ' + angular.toJson(vm.selected));
			console.log('Edit or delete : ' + vm.editOrDelete);
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a Staff-Member')
					.textContent('Are you sure you want to delete this Staff-Member ?')
					.ariaLabel('Staff-Member deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				StaffMemberService.remove(
						{id:vm.selected[0].id},
						onDeleteSuccess,
						onDeleteFailure
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		function loadAll(){
			FacilitiesStaffMemberService.query({id: $stateParams.id}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allStaffMembers = data;
			vm.StaffMembersOnCurrentPage = vm.sliceStaffMemberArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}

		function onDeleteSuccess (){
			vm.StaffMembersOnCurrentPage.splice(vm.StaffMembersOnCurrentPage.indexOf(vm.selected[0]), 1);
			vm.allStaffMembers.splice(vm.allStaffMembers.indexOf(vm.selected[0]), 1);
			vm.editOrDelete = true;
			vm.showToast('Staff-Member ' + vm.selected[0].firstName + ' successfully deleted', 5000);
		}	
		
		function onDeleteFailure (){
			vm.showToast('Staff-Member ' + vm.selected[0].firstName 
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
			vm.StaffMembersOnCurrentPage = vm.sliceStaffMemberArray();
		}
		
		function sliceStaffMemberArray(){
			var slicedArray = vm.allStaffMembers.slice(5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			console.log('Sliced array : ' + angular.toJson(slicedArray));
			return slicedArray;
		}
		
		function showStaffMemberDialog(ev) {
			$mdDialog.show({
				templateUrl: 'schedulesys/staff-members/staff-member-dialog.html',
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
		
		function getSelectedFacility(){
			FacilitiesService.get({id:$stateParams.id}, function(result) {
				vm.facility = result;
			})
		}
		
		function getStaffMembers(){
			FacilitiesStaffMemberService.query({id:$stateParams.id}, function(result) {
				vm.allStaffMembers = result;
			})
		}
	}
})();