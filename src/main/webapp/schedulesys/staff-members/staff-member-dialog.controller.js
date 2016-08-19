(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('StaffMemberDialogController', StaffMemberDialogController);
	
	StaffMemberDialogController.$Inject = ['$state','$stateParams', '$mdDialog', '$mdToast',
	                                       'StaffMemberService','FacilitiesService', 'FacilitiesStaffMemberService'];
	
	function StaffMemberDialogController($state,$stateParams, $mdDialog, $mdToast,
			StaffMemberService, FacilitiesService, FacilitiesStaffMemberService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.showToast = showToast;
		vm.getSelectedStaffMember = getSelectedStaffMember;
		vm.createOrUpdateStaffMember = createOrUpdateStaffMember;
		
		vm.staffmember = {
				id: null,
				firstName: null,
				lastName: null,
				title: null,
				phoneNumber: null,
				fax: null
				};
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		if(angular.isDefined($stateParams.staffMemberId)){
			vm.getSelectedStaffMember();
		}
		
		function getSelectedStaffMember(){
			StaffMemberService.get({id : $stateParams.staffMemberId},function(result){
				vm.staffmember = result;
				
			});
		}
		
		function createOrUpdateStaffMember(){
			if(vm.staffmember.id === null){
				FacilitiesStaffMemberService.save({id: $stateParams.id}
				, vm.staffmember, onCreateSucess, onCreateFailure);
			}else{
				FacilitiesStaffMemberService.update({id:$stateParams.id,staffMemberId: $stateParams.staffMemberId},
						vm.staffmember, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			vm.showToast('Staff-Member ' + vm.staffmember.firstName + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.facilities',{}, {reload: true});
			vm.showToast('Staff-Member ' + vm.staffmember.firstName + ' successfully updated', 5000);
		}
		
		function onUpdateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function showToast(textContent, delay){
			$mdToast.show($mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
	}
	
})();