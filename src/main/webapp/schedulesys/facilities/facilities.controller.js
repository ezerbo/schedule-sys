(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('FacilitiesController', FacilitiesController);
	
	FacilitiesController.$Inject = ['$scope', '$state','$mdDialog' ,'FacilitiesService'];
	
	function FacilitiesController($scope, $state, $mdDialog, FacilitiesService){
		var vm = this;
		
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.editOrDelete = true;
		vm.transition = transition;
		vm.query = {
				order: 'name',
				limit: 5,
				page: 1
		};
		
		loadAll();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
			console.log('Selected items : ' + angular.toJson(vm.selected));
			console.log('Edit or delete : ' + vm.editOrDelete);
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a facility')
					.textContent('Are you sure you want to delete this facility ?')
					.ariaLabel('Facility deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				var selectedFacility = vm.selected[0];
				FacilitiesService.remove({id:selectedFacility.id});
				vm.facilities.splice(vm.facilities.indexOf(selectedFacility), 1);
				console.log('Deleting faclitity ... : ' + selectedFacility.id);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			FacilitiesService.query({}
			, onSuccess
			, onError);
		}
		
		function onSuccess(data){
			vm.facilities = data;
		}
		
		function onError(status){
			console.log('Error status : ' + status);
		}
		
		function transition () {
            $state.transitionTo($state.$current, {
            	reload: true,
                inherit: false,
                notify: true
            });
        }
	}
	
})();