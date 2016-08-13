(function(){
	'use strict';
	angular.module('scheduleSys')
		.factory('ScheduleService', ScheduleService);
	
	ScheduleService.$Inject = ['$resource'];
	
	function ScheduleService($resource){
		var resourceUrl = '/schedules/:id';
		return $resource(resourceUrl, {}, {
			'update': { method:'PUT' },
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