(function() {
	'use strict';
	
	angular
		.module("scheduleSys")
		.controller("LoginController", LoginController);
	
	LoginController.$inject = ['$scope', '$state', '$mdToast', 'AuthenticationProvider'];
	
	function LoginController($scope, $state, $mdToast, AuthenticationProvider){
		
		var vm = this;
		
		vm.credentials = {
				username: null,
				password: null,
				rememberMe: true
		};
		
		vm.login = login;
		vm.showAuthFailureToast = showAuthFailureToast;
		
		function login() {
			AuthenticationProvider.login(vm.credentials, function(result) {
				if(result){
					$state.go('home.facilities');
				}else{
					showAuthFailureToast();
				}
			});
		}
		
		function showAuthFailureToast(){
			 $mdToast.show(
				      $mdToast.simple()
				        .textContent('Incorrect username or password')
				        .position('top center')
				        .hideDelay(5000));
		}
	}
	
})();