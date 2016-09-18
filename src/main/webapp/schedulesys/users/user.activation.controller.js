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
		
		vm.profile = {
			activationToken: $location.search().key,
			password: null
		};
		
		vm.activate = activate;
		vm.onPasswordInput = onPasswordInput;
		
		function activate(){
			UserAccountService.save(vm.profile, function() {
				$state.go('login', {}, {});
			}, function(result) {
				console.log('Something unexpected happened : ');
				
			});
		}
		
		function onPasswordInput(){
			if(angular.isDefined(vm.profile.password) && angular.isDefined(vm.passwordConfirmation) && vm.passwordConfirmation !== null){
				console.log('main password : ' + vm.profile.password);
				console.log('COnfirmation : ' + vm.passwordConfirmaton)
				vm.activateBtnDiabled = (vm.profile.password === vm.passwordConfirmation) ? false : true;
			}
		}
	}
	
})();