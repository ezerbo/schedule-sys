(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.factory('PrivateCareSchedulingService', PrivateCareSchedulingService);
	
	PrivateCareSchedulingService.$Inject = ['$resource'];
	
	function PrivateCareSchedulingService($resource) {
		var resourceUrl = '/private-cares/:id/schedules';
		
		return $resource(resourceUrl, {}, {
            'query': { 
            	method: 'GET',
            	isArray: true,
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