(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('EmployeesService', EmployeesService)
		.factory('EmployeesScheduleService', EmployeesScheduleService);
	
	EmployeesService.$Inject = ['$resource'];
	
	function EmployeesService($resource){
		var resourceUrl = '/employees/:id';
		return $resource(resourceUrl, {},{
			  'get': {method: 'GET'}
		});
	}
	
	EmployeesScheduleService.$Inject = ['$resource'];
	
	function EmployeesScheduleService($resource){
		var resourceUrl = '/employees/:id/schedules';
		return $resource(resourceUrl, {},{
			'query': { method: 'GET', isArray: true,
				transformResponse: function(data){
            		try{
            			data = angular.fromJson(data);
            		}catch(error){
            			data = angular.toJson(data);
            		}
                    return data;
                }
			}
		});
	}
	
})();