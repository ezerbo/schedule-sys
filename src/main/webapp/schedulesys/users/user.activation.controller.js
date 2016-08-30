(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('UserActivationController', UserActivationController);
	
	UserActivationController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog', '$location', 'UserAccountService'];
	
	function UserActivationController($scope, $state, $mdToast, $mdDialog, $location, UserAccountService){
		var vm = this;
		vm.profile = {
			activationToken: $location.search().key,
			password: null
		};
		
		vm.activate = activate;
		
		function activate(){
			UserAccountService.save(vm.profile, function() {
				$state.go('login', {}, {});
			}, function(result) {
				console.log('Something unexpected happened : ' + result);
			});
		}
	}
	
})();