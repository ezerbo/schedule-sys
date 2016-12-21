(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('NursesController', NursesController);
	
	NursesController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog' ,'NursesService','NurseLicenseService'];
	
	function NursesController($scope, $state, $mdToast, $mdDialog, NursesService,NurseLicenseService){
		var vm = this;
		
		vm.allNurses = null;
		vm.allLicenses = null;
		vm.nursesOnCurrentPage = null;
		vm.showAlert = showAlert;
		vm.showNoAlert = showNoAlert;
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.deletion = true;
		vm.editOrDetails = false;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceNursesArray = sliceNursesArray;
		
		vm.query = {
				order: 'name',
				limit: 20,
				page: 1
		};
		
		loadAll();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			if(vm.selected.length === 0){
				vm.deletion = true;
			}else{
				vm.deletion= false;
			}
			vm.editOrDetails = (vm.selected.length === 1) ? false : true;
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a nurse')
					.textContent('Are you sure you want to delete this nurse ?')
					.ariaLabel('Nurse deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				vm.selected.forEach(function(elt, i, array) {
					NursesService.remove({id: elt.id},onDeleteSuccess,onDeleteFailure);
				});
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			NursesService.query({}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allNurses = data;
			vm.nursesOnCurrentPage = vm.sliceNursesArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}
		
		function onDeleteSuccess (){
			vm.deletion = true;
			vm.showToast('Nurse successfully deleted', 5000);
			$state.go($state.current,{}, {reload:true});
		}	
		
		function onDeleteFailure (){
			vm.showToast('Nurse ' + vm.selected[0].firstName 
					+ ' could not be deleted ', 5000);
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay)
					);
		}
		
		function onPaginate(){
			vm.nursesOnCurrentPage = vm.sliceNursesArray();
			
		}
		
		function sliceNursesArray(){
			var slicedArray = vm.allNurses.slice(5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			return slicedArray;
		}
		
		function showAlert(ev) {
			// Appending dialog to document.body to cover sidenav in docs app
			// Modal dialogs should fully cover application
			// to prevent interaction outside of dialog
			$mdDialog.show(
					$mdDialog.alert()
					.parent(angular.element(document.querySelector('#popupContainer')))
					.clickOutsideToClose(true)
					.title('Expired-License')
					.textContent('This Nurse has licenses/license expired.')
					.ariaLabel('License Alert')
					.ok('ok!')
					.targetEvent(ev)
			);
		};

		function showNoAlert(ev) {
			// Appending dialog to document.body to cover sidenav in docs app
			// Modal dialogs should fully cover application
			// to prevent interaction outside of dialog
			$mdDialog.show(
					$mdDialog.alert()
					.parent(angular.element(document.querySelector('#popupContainer')))
					.clickOutsideToClose(true)
					.title('Valid License')
					.textContent('This Nurse has valid licenses')
					.ariaLabel('License Alert')
					.ok('ok!')
					.targetEvent(ev)
			);
		};

	}
	
})();