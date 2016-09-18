(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('NurseTestDialogController', NurseTestDialogController);
	
	NurseTestDialogController.$Inject = ['$state', '$scope', '$stateParams', '$mdDialog', '$mdToast',
	                                  'NursesService','TestsService', 'NurseTestService'];
	
	function NurseTestDialogController($state, $scope, $stateParams, $mdDialog, $mdToast, NursesService, NurseTestService, TestsService){
		var vm = this;
	}
}
)();