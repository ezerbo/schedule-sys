(function(){
	'use strict';
	angular.module('scheduleSys')
		.factory('PrivateCareScheduleService', PrivateCareScheduleService);
	
	PrivateCareScheduleService.$Inject = ['$resource'];
	
	function PrivateCareScheduleService($resource){
		var resourceUrl = '/private-care-schedules/:id';
		return $resource(resourceUrl, {}, {
			'update':{
                method: 'PUT',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            },
			'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'remove':  {
                method: 'DELETE',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            },
            'save': {
                method: 'POST',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            
            }
		});
	}
})();