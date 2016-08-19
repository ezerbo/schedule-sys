(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('NurseDetailsController', NurseDetailsController);
	
	NurseDetailsController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'NursesService','NurseLicenseService','LicenseService'];
	
	function NurseDetailsController($state,$scope, $stateParams, $mdDialog, $mdToast, NursesService,NurseLicenseService,LicenseService){
		var vm = this;
		vm.allLicenses = null;
		vm.licensesOnCurrentPage = null;
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.getSelectedNurse = getSelectedNurse;
		vm.getLicenses = getLicenses;
		
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceLicenseArray = sliceLicenseArray;
		vm.query = {
				limit: 5,
				page: 1	
		};
		
		loadAll();
		getSelectedNurse();
		getLicenses();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
			console.log('Selected items : ' + angular.toJson(vm.selected));
			console.log('Edit or delete : ' + vm.editOrDelete);
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a License')
					.textContent('Are you sure you want to delete this License ?')
					.ariaLabel('License deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				LicenseService.remove(
						{id:vm.selected[0].id},
						onDeleteSuccess,
						onDeleteFailure
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		function loadAll(){
			NurseLicenseService.query({id: $stateParams.id}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allLicenses = data;
			vm.licensesOnCurrentPage = vm.sliceLicenseArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}

		function onDeleteSuccess (){
			vm.licensesOnCurrentPage.splice(vm.licensesOnCurrentPage.indexOf(vm.selected[0]), 1);
			vm.allLicenses.splice(vm.allLicenses.indexOf(vm.selected[0]), 1);
			vm.editOrDelete = true;
			vm.showToast('License ' + vm.selected[0].number + ' successfully deleted', 5000);
		}	
		
		function onDeleteFailure (){
			vm.showToast('License ' + vm.selected[0].number 
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
			vm.licensesOnCurrentPage = vm.sliceLicenseArray();
		}
		
		function sliceLicenseArray(){
			var slicedArray = vm.allLicenses.slice(5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			console.log('Sliced array : ' + angular.toJson(slicedArray));
			return slicedArray;
		}
		
		function getSelectedNurse(){
			NursesService.get({id:$stateParams.id}, function(result) {
				vm.nurse = result;
			})
		}
		
		function getLicenses(){
			NurseLicenseService.query({id:$stateParams.id}, function(result) {
				vm.allLicenses = result;
			})
		}
	}
})();