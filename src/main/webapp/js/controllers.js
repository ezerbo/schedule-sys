/**
 * Created by Abhinav
 */
angular.module('userApp.controllers',[]).controller('UserListController',function($scope,$state,popupService,$window,User){

    $scope.users=User.query();

    $scope.deleteUser=function(user){
        if(popupService.showPopup('Really delete this User?')){
            user.$delete(function(){
                
                $window.location.href='';
               
            });

            $window.location.reload();
        }
    }

}).controller('UserViewController',function($scope,$stateParams,User,License,popupService,$window){

    $scope.user=User.get({id:$stateParams.id});

    


    $scope.userid = $stateParams.id;
     $scope.licenses = License.query({userid:$scope.userid});

 $scope.deleteLicense=function(license){
        if(popupService.showPopup('Really delete this License?')){
            license.$delete({userid:$scope.userid},function(){
                
                $window.location.href='';
               
            });

            $window.location.reload();
        }
    }

$scope.dateCheck = function (license){

$scope.checkDate1 = new Date();
$scope.checkDate2 = new Date(license.expirationDate);

console.log($scope.checkDate1);
console.log($scope.checkDate2);

if($scope.checkDate1 > $scope.checkDate2){

    return true;
}
else{

    return false;
}


}
}).controller('UserCreateController',function($scope,$state,$stateParams,User){
     

    $scope.user=new User();

    $scope.addUser=function(){
        $scope.user.$save(function(){
            $state.go('users');
        });
    }


 $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
    console.log($scope.user.dateOfHire);
  };

  $scope.stateRefresh = function(){
$state.go($state.current, {}, {reload: true});
}


}).controller('UserEditController',function($scope,$state,$stateParams,User){

    $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
    console.log($scope.user.dateOfHire);
  };

    $scope.updateUser=function(){
        $scope.user.$update(function(){
            $state.go('users');
        });
    };

    $scope.loadUser=function(){
        $scope.user=User.get({id:$stateParams.id});
    };

    $scope.loadUser();
}).controller('ListDropDowns',function($scope,$state,$stateParams){

     


$scope.typeoptions = ["NURSE","CareTaker"];

$scope.positionoptions = [["RN","LLN","CCHA"],["Inhouse","OutHouse"]];

$scope.options1 = $scope.typeoptions ;
$scope.options2 = [];
$scope.getUserPosition = function(){

    var key = $scope.options1.indexOf($scope.user.positionType);

    var myNewOptions = $scope.positionoptions[key];

    $scope.options2= myNewOptions;
}

}).controller("DropDowns",function($scope,$state,$stateParams){

$scope.typeoptions = ["NURSE","CareTaker"];

$scope.positionoptions = [["RN","TN"],["SN","QN"]];

$scope.options1 = $scope.typeoptions ;
$scope.options2 = ["RN","TN","SN","QN"]






$scope.getUserPosition = function(){

    var key = $scope.options1.indexOf($scope.user.positionType);

    var myNewOptions = $scope.positionoptions[key];

    $scope.options2= myNewOptions;
}



}).controller('LicenseViewController',function($scope,$stateParams,License){




$scope.userid = $stateParams.userid;

    
    
console.log($scope.userid);
    

    $scope.license=License.get({id:$stateParams.id,userid:$stateParams.userid});


}).controller('LicenseEditController',function($scope,$state,$stateParams,License){


console.log($stateParams.userid);
    $scope.updateLicense=function(){
        $scope.license.$update({userid:$stateParams.userid},function(){
            $state.go('users');
        });
    };

    $scope.loadLicense=function(){


        $scope.license=License.get({id:$stateParams.id,userid:$stateParams.userid});
    };

    $scope.loadLicense();

    $scope.isShow = function(){

        return false;
    }
}).controller('LicenseCreateController',function($scope,$state,$stateParams,License){
     

    $scope.license=new License();

    $scope.addLicense=function(){
        $scope.license.$save({userid:$stateParams.userid},function(){
            $state.go('users');
        });
    }

     $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
    
  };

  $scope.stateRefresh = function(){
$state.go($state.current, {}, {reload: true});
}

$scope.isShow = function(){

        return true;
    }
}).controller('FacilityListController',function($scope,$state,popupService,$window,Facility){

    $scope.facilities=Facility.query();

    $scope.deleteFacility=function(facility){
        if(popupService.showPopup('Really delete this Facility?')){
            facility.$delete(function(){
                
                $window.location.href='';
               
            });

            $window.location.reload();
        }
    }

}).controller('FacilityViewController',function($scope,$stateParams,Facility,StaffMember,Schedule,$state,popupService,$window){

    $scope.facility=Facility.get({id:$stateParams.id});

    

    $scope.staffmembers=StaffMember.query({id:$stateParams.id});

    

    

    $scope.deleteStaff=function(staffmember){
        if(popupService.showPopup('Really delete this Staff-Member?')){
            staffmember.$delete(function(){
                
                $window.location.href='';
               
            });

            $window.location.reload();
        }
    }


    $scope.showModal = false;
    $scope.toggleModal = function(){
        $scope.showModal = !$scope.showModal;
    };
 


$scope.facid = $stateParams.id;

$scope.nameCheck = function (schedule){

$scope.incompleteName = 'test2-fn,test2-ln';





if($scope.incompleteName == schedule.employeeName){

    return true;
}
else{

    return false;
}


}



$scope.display = false;
    

  $scope.nextWeek = function(sd,ed,c) {




   
if(sd == 0 && ed ==2){
$scope.numberOfDaysToStart = sd ;
$scope.numberOfDaysToAddEnd =ed ;
$scope.fcounter = 0;
}
else{


$scope.fcounter = $scope.fcounter+c;


    
$scope.numberOfDaysToStart = sd * $scope.fcounter;
$scope.numberOfDaysToAddEnd =ed * ($scope.fcounter+1);

console.log($scope.fcounter);
}

      $scope.someDate1 = new Date();
      $scope.someDate1.setDate($scope.someDate1.getDate() + $scope.numberOfDaysToStart);
  $scope.startdate = ('0' + ($scope.someDate1.getMonth() + 1)).slice(-2) + '/' + ('0' + $scope.someDate1.getDate()).slice(-2) + '/' + $scope.someDate1.getFullYear();
       
       $scope.someDate2 = new Date();

$scope.someDate2.setDate($scope.someDate2.getDate() + $scope.numberOfDaysToAddEnd);
  $scope.enddate = ('0' + ($scope.someDate2.getMonth() + 1)).slice(-2) + '/' + ('0' + $scope.someDate2.getDate()).slice(-2) + '/' + $scope.someDate2.getFullYear();

 $scope.schedules=Schedule.query({id:$stateParams.id,startDate:$scope.startdate,endDate:$scope.enddate});

 

  }

$scope.lastWeek = function(sd,ed,c) {

$scope.display= true;


   
if(sd == 2 && ed ==0){
$scope.numberOfDaysToStart = sd ;
$scope.numberOfDaysToAddEnd =ed ;
$scope.counter = 0;
}
else{


$scope.counter = $scope.counter+c;


    
$scope.numberOfDaysToStart = sd * ($scope.counter+1);
$scope.numberOfDaysToAddEnd =ed * $scope.counter;

console.log($scope.counter);
}

      $scope.someDate1 = new Date();
      $scope.someDate1.setDate($scope.someDate1.getDate() - $scope.numberOfDaysToStart);
  $scope.startdate = ('0' + ($scope.someDate1.getMonth() + 1)).slice(-2) + '/' + ('0' + $scope.someDate1.getDate()).slice(-2) + '/' + $scope.someDate1.getFullYear();
       
       $scope.someDate2 = new Date();

$scope.someDate2.setDate($scope.someDate2.getDate() - $scope.numberOfDaysToAddEnd);
  $scope.enddate = ('0' + ($scope.someDate2.getMonth() + 1)).slice(-2) + '/' + ('0' + $scope.someDate2.getDate()).slice(-2) + '/' + $scope.someDate2.getFullYear();

 $scope.schedules=Schedule.query({id:$stateParams.id,startDate:$scope.startdate,endDate:$scope.enddate});

 

  }

  $scope.nextWeek(0,2,0);

$scope.stateRefresh = function(){
$state.go($state.current, {}, {reload: true});
}


}).controller('FacilityCreateController',function($scope,$state,$stateParams,Facility){
     

    $scope.facility=new Facility();

    $scope.addFacility=function(){
        $scope.facility.$save(function(){
            $state.go('facilities');
        });
    }

      $scope.stateRefresh = function(){
$state.go($state.current, {}, {reload: true});
}
$scope.isShow = function(){

        return true;
    }

}).controller('FacilityEditController',function($scope,$state,$stateParams,Facility){

    $scope.updateFacility=function(){
        $scope.facility.$update(function(){
            $state.go('facilities');
        });
    };

    $scope.loadFacility=function(){
        $scope.facility=Facility.get({id:$stateParams.id});
    };

    $scope.loadFacility();

    $scope.isShow = function(){

        return false;
    }
}).controller('StaffListController',function($scope,$state,popupService,$window,StaffMember){

    $scope.staffmembers=StaffMember.query();

    $scope.deleteStaff=function(staffmember){
        if(popupService.showPopup('Really delete this Staff-Member?')){
            staffmember.$delete(function(){
                
                $window.location.href='';
               
            });

            $window.location.reload();
        }
    }

}).controller('StaffViewController',function($scope,$stateParams,FacilityStaff){




$scope.facid = $stateParams.facid;

    
    
console.log($scope.facid);
    

    $scope.staffmember=FacilityStaff.get({id:$stateParams.id,facid:$stateParams.facid});


}).controller('StaffCreateController',function($scope,$state,$stateParams,StaffMember,FacilityStaff){
     

    $scope.staffmember=new FacilityStaff();

    $scope.addStaff=function(){
        $scope.staffmember.$save({facid:$stateParams.facid},function(){
            $state.go('facilities');
        });
    }

      $scope.stateRefresh = function(){
$state.go($state.current, {}, {reload: true});
}

$scope.isShow = function(){

        return true;
    }

}).controller('StaffEditController',function($scope,$state,$stateParams,StaffMember,FacilityStaff){

    $scope.updateStaff=function(){
        $scope.staffmember.$update({facid:$stateParams.facid},function(){
            $state.go('facilities');
        });
    };

    $scope.loadStaff=function(){


        $scope.staffmember=FacilityStaff.get({id:$stateParams.id,facid:$stateParams.facid});
    };

    $scope.loadStaff();

    $scope.isShow = function(){

        return false;
    }
}).controller('ScheduleViewController',function($scope,$stateParams,Schedule){




$scope.facid = $stateParams.facid;

    
    
console.log($scope.facid);
    

    $scope.schedule=Schedule.get({id:$stateParams.id,facid:$stateParams.facid});


}).controller('ScheduleCreateController',function($scope,$state,$stateParams,ScheduleAdd){
     

    $scope.schedule=new ScheduleAdd();

    $scope.addSchedule=function(){
        $scope.schedule.$save(function(){
            $state.go('facilities');
        });
    }

     $scope.open = function($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
    console.log($scope.schedule.scheduleDate);
  };

  $scope.stateRefresh = function(){
$state.go($state.current, {}, {reload: true});
}
}).controller('ScheduleEditController',function($scope,$state,$stateParams,StaffMember,FacilityStaff){

    $scope.updateSchedule=function(){
        $scope.schedule.$update({facid:$stateParams.facid},function(){
            $state.go('facilities');
        });
    };

    $scope.loadSchedule=function(){


        $scope.schedule=FacilityStaff.get({id:$stateParams.id,facid:$stateParams.facid});
    };

    $scope.loadSchedule();
}).controller('SupervisorListController',function($scope,$state,popupService,$window,Supervisor){

    $scope.supervisors=Supervisor.query();

    $scope.deleteSupervisor=function(supervisor){
        if(popupService.showPopup('Really delete this Supervisor?')){
            supervisor.$delete(function(){
                
                $window.location.href='';
               
            });

            $window.location.reload();
        }
    }

}).controller('SupervisorViewController',function($scope,$stateParams,Supervisor){

    $scope.supervisor=Supervisor.get({id:$stateParams.id});


}).controller('SupervisorCreateController',function($scope,$state,$stateParams,Supervisor){
     

    $scope.supervisor=new Supervisor();

    $scope.addSupervisor=function(){
        $scope.supervisor.$save(function(){
            $state.go('supervisors');
        });
    }

      $scope.stateRefresh = function(){
$state.go($state.current, {}, {reload: true});
}

$scope.isShow = function(){

        return true;
    }

}).controller('SupervisorEditController',function($scope,$state,$stateParams,Supervisor){

    $scope.updateSupervisor=function(){
        $scope.supervisor.$update(function(){
            $state.go('supervisors');
        });
    };

    $scope.loadSupervisor=function(){
        $scope.supervisor=Supervisor.get({id:$stateParams.id});
    };

    $scope.loadSupervisor();

    $scope.isShow = function(){

        return false;
    }
}).controller("ScheduleDropDown",function($scope,$state,$stateParams,scheduleShift,Facility,scheduleStatus,schedulePostStatus){

$scope.shiftoptions = scheduleShift.query();


$scope.facilityoptions = Facility.query();

$scope.schedulestatustoptions = scheduleStatus.query();

$scope.schedulepoststatusoptions = schedulePostStatus.query();

$scope.options1 = $scope.shiftoptions ;
$scope.options2 = $scope.facilityoptions;
$scope.options3 = $scope.schedulestatustoptions;
$scope.options4 = $scope.schedulepoststatusoptions;






$scope.getUserPosition = function(){

    var key = $scope.options1.indexOf($scope.user.positionType);

    var myNewOptions = $scope.positionoptions[key];

    $scope.options2= myNewOptions;
}



}).controller('MyCtrl', function($scope) {
  $scope.currencyVal;
}).controller('Main', MainCtrl);
function MainCtrl(user, auth,$state) {
  var self = this;

  function handleRequest(res) {
    var token = res.data ? res.data.access_token : null;
    if(token) { console.log('JWT:', token); 
    auth.saveToken(token);
$state.go('users');}
    self.message = res.data.message;
  }

  self.login = function() {
    user.login(self.username, self.password,self.grant_type)
      .then(handleRequest, handleRequest)
  }
 
  
  self.isAuthed = auth.isAuthed
  self.logout =function(){ 
   auth.logout();
   $state.go('authenticatejwt');

   


  }
}