/**
 * Created by Abhinav

 the @_id is being referenced because it is pulling data from MongoDB.

 URL-Example -> http://movieapp-sitepointdemos.rhcloud.com/api/movies/
 */

angular.module('userApp.services',[]).factory('User',function($resource){
    return $resource('http://localhost:8080/users/employees/:id',{id:'@id'},{
        update: {
            method: 'PUT'
        }
    });
}).service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
}).factory('License',function($resource){
    return $resource('http://localhost:8080/employees/:userid/licenses/:id',{userid:'@userid',id:'@id'},{
        update: {
            method: 'PUT'
        }
    });
    }).factory('Facility',function($resource){
    return $resource('http://localhost:8080/facilities/:id',{id:'@id'},{
        update: {
            method: 'PUT'
        }
    });
    }).factory('StaffMember',function($resource){
       

    return $resource('http://localhost:8080/facilities/:id/staff-members/',{},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('Schedule',function($resource){

     

    return $resource('http://localhost:8080/facilities/:id/schedules',{startDate : '@startdate' ,endDate : '@enddate'},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('Supervisor',function($resource){
    return $resource('http://localhost:8080/users/supervisors/:id',{id:'@id'},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('ScheduleAdd',function($resource){
    return $resource('http://localhost:8080/schedules',{},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('scheduleShift',function($resource){
    return $resource('http://localhost:8080/shifts',{},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('scheduleStatus',function($resource){
    return $resource('http://localhost:8080/schedule-statuses',{},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('schedulePostStatus',function($resource){
    return $resource('http://localhost:8080/schedule-post-statuses',{},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('FacilityStaff',function($resource){
       

    return $resource('http://localhost:8080/facilities/:facid/staff-members/:id',{facid:'@facid',id:'@id'},{
        update: {
            method: 'PUT'
        }
        });
    }).factory('authInterceptor', authInterceptor)
.service('user', userService)
.service('auth', authService); 
function authInterceptor(API, auth,$window) {
  return {
    // automatically attach Authorization header
    request: function(config) {

      var token = auth.getToken();
     
      if(config.url.indexOf(API) === 0){

     
     config.headers.Authorization = 'Basic '+'cmotc2NoZWR1bGUtc3lzOg==';



        
        
      }

      else{
      config.headers.Authorization = 'Bearer '+ token;
}
      return config;
    },

    // If a token was sent back, save it
    response: function(res) {

     if(res.config.url.indexOf(API) === 0 && res.data.token) {
    auth.saveToken(res.data.token);
  }
      return res;
    },

    responseError: function(res){
     if(res.status === 401){

      $window.location.href = "https://www.google.com/?gws_rd=ssl"
     }

    }
  }
}

function authService($window) {
  var self = this;

  self.parseJwt = function(token){
    var base64Url = token.split('.')[1]
    var base64 = base64Url.replace('-','+').replace('_','/')
    return JSON.parse($window.atob(base64))


  }

  self.saveToken = function(token){

    $window.localStorage['jwtToken'] =token ;
  }

  self.getToken = function(){

    return $window.localStorage['jwtToken']
  }


  self.isAuthed = function(){

    var token = self.getToken();
    if(token){

      var params = self.parseJwt(token);
      

      return Math.round(new Date().getTime() / 1000) <= params.exp


    }else{

      return false;
    }
  }

  self.logout = function(){

    $window.localStorage.removeItem('jwtToken');
  }
}

function userService($http, API, auth) {
  var self = this;
  



self.login = function(username,password,grant_type){
  
  var grant_type = 'password';

  return $http({

    method: 'POST',
    url : API,
    data: "username=" + username +
                     "&password=" + password +
                     "&grant_type=" + grant_type,
    headers: {
'Content-Type': 'application/x-www-form-urlencoded',



    }

  })
}
}