(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.controller('PasswordResetRequestController', PasswordResetRequestController);
	
	PasswordResetRequestController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog', '$location', 'PasswordResetRequestService'];
	
	function PasswordResetRequestController($scope, $state, $mdToast, $mdDialog, $location, PasswordResetRequestService){
		var vm = this;
		vm.passwordResetRequest = {
				usernameOrEmailAddress: null
		}
		vm.submitPasswordResetRequest = submitPasswordResetRequest;
		
		function submitPasswordResetRequest(){
			PasswordResetRequestService.save(vm.passwordResetRequest, function(result) {
				vm.passwordResetRequest.usernameOrEmailAddress = null;
				showToast("Passord reset email successfully sent. Please click on the link provided in the email to reset your password");
			}, function() {
				showToast('Something unexpected happened, please make sure you entered the right username or email address');
			})
		}
		
		function showToast(content){
			 $mdToast.show(
				      $mdToast.simple()
				        .textContent(content)
				        .position('top center')
				        .hideDelay(7000));
		}
	}
	
})();