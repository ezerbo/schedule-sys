(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.factory('FacilitiesSchedulingService', FacilitiesSchedulingService);
	
	FacilitiesSchedulingService.$Inject = ['$resource'];
	
	function FacilitiesSchedulingService($resource) {
		var resourceUrl = '/facilities/:id/schedules';
		
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