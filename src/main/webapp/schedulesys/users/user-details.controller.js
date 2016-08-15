(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('userDetailsController', userDetailsController);
	
	userDetailsController.$Inject = ['$state', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'usersService'];
	
	function userDetailsController($state, $stateParams, $mdDialog, $mdToast, usersService){
		var vm = this;
		
		vm.getSelectedUser = getSelectedUser;
		
		vm.selected = [];
		vm.query = {
				limit: 5,
				page: 1	
		};
		
		
		getSelectedUser();
		
		function getSelectedUser(){
			usersService.get({id:$stateParams.id}, function(result) {
				vm.user = result;
			})
		}
	}
})();