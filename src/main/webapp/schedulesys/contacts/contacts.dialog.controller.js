(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('ContactDialogController', ContactDialogController);
	
	ContactDialogController.$Inject = ['$state','$stateParams', '$mdDialog', '$mdToast',
	                                       'ContactService','PrivateCareService', 'PrivateCareContactService'];
	
	function ContactDialogController($state,$stateParams, $mdDialog, $mdToast,
			ContactService, PrivateCareService, PrivateCareContactService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.showToast = showToast;
		vm.getSelectedContact = getSelectedContact;
		vm.createOrUpdate = createOrUpdate;
		
		vm.contact = {
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
		
		if(angular.isDefined($stateParams.contactId)){
			vm.getSelectedContact();
		}
		
		function getSelectedContact(){
			ContactService.get({id : $stateParams.contactId}, function(result){
				vm.contact = result;
				
			});
		}
		
		function createOrUpdate(){
			if(vm.contact.id === null){
				PrivateCareContactService.save({id: $stateParams.id}
				, vm.contact, onCreateSucess, onCreateFailure);
			}else{
				PrivateCareContactService.update({id:$stateParams.id, contactId: $stateParams.contactId},
						vm.contact, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			vm.showToast('Contact ' + vm.contact.firstName + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			vm.showToast('Contact ' + vm.contact.firstName + ' successfully updated', 5000);
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