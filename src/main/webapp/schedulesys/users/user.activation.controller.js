(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('UserActivationController', UserActivationController);
	
	UserActivationController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog', '$location', 'UserAccountService'];
	
	function UserActivationController($scope, $state, $mdToast, $mdDialog, $location, UserAccountService){
		var vm = this;
		
		vm.passwordConfirmation = null;
		vm.activateBtnDiabled = true;
		
		vm.accountActivation = angular.isDefined($location.search().key) ? true : false;
		
		vm.profile = {
			activationToken: angular.isDefined($location.search().key) ? $location.search().key : $location.search().token,
			password: null
		};
		
		vm.activate = activate;
		vm.showToast = showToast;
		vm.onPasswordInput = onPasswordInput;
		
		function activate(){
			UserAccountService.save(vm.profile, function() {
				if(vm.accountActivation == true){
					vm.showToast('Account successfully activated. You may now sign in');
				}else{
					vm.showToast('Password successfully reset. You may now sign in');
				}
				$state.go('login', {}, {});
			}, function(result) {
				vm.showToast('Unable to activate account');
			});
		}
		
		function onPasswordInput(){
			if(angular.isDefined(vm.profile.password) && angular.isDefined(vm.passwordConfirmation) && vm.passwordConfirmation !== null){
				vm.activateBtnDiabled = (vm.profile.password === vm.passwordConfirmation) ? false : true;
			}else if(vm.passwordConfirmation == null){
				vm.activateBtnDiabled = true;
			}
		}
		
		function showToast(content){
			 $mdToast.show(
				      $mdToast.simple()
				        .textContent(content)
				        .position('top center')
				        .hideDelay(5000));
		}
	}
	
})();