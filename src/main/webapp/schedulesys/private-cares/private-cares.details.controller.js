(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('PrivateCareDetailsController', PrivateCareDetailsController);
	
	PrivateCareDetailsController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'PrivateCareService', 'FacilitiesStaffMemberService','StaffMemberService'];
	
	function PrivateCareDetailsController($state,$scope, $stateParams, $mdDialog, $mdToast, PrivateCareService, FacilitiesStaffMemberService,StaffMemberService){
		var vm = this;
		vm.allStaffMembers = [];
		vm.staffMembersOnCurrentPage = [];
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.getStaffMembers = getStaffMembers;
		vm.getSelectedFacility = getSelectedFacility;
		
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceStaffMemberArray = sliceStaffMemberArray;
		vm.query = {
				limit: 5,
				page: 1	
		};
		
		loadAll();
		getStaffMembers();
		getSelectedFacility();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
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
			FacilitiesStaffMemberService.query({id: $stateParams.id},
					onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allStaffMembers = data;
			vm.staffMembersOnCurrentPage = vm.sliceStaffMemberArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}

		function onDeleteSuccess (){
			$state.go($state.$current, {}, {reload:true});
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
			vm.staffMembersOnCurrentPage = vm.sliceStaffMemberArray();
		}
		
		function sliceStaffMemberArray(){
			return vm.allStaffMembers.slice(
					5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
		}
		
		function getSelectedFacility(){
			PrivateCareService.get({id:$stateParams.id}, function(result) {
				vm.privateCare = result;
			});
		}
		
		function getStaffMembers(){
			FacilitiesStaffMemberService.query({id:$stateParams.id}, function(result) {
				vm.allStaffMembers = result;
			});
		}
	}
})();