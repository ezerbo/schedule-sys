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
		vm.showToast = showToast;
		vm.onPasswordInput = onPasswordInput;
		
		function activate(){
			console.log('User profile : ' + angular.toJson(vm.profile));
			UserAccountService.save(vm.profile, function() {
				$state.go('login', {}, {});
			}, function(result) {
				vm.showToast('Unable to activate account');
				console.log('Something unexpected happened : ');
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