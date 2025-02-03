
var app = angular.module("CityModule", []);


//Forget Pass Controller
app.controller("CityController", function ($scope, $http) {

    $scope.form = {};
    $scope.views = {};
    showHideLoad(true);
    $scope.views.list = true;

    autoCityListFetch();
    function autoCityListFetch() {
        $http({
            method: 'GET',
            url: 'location/fetchAllCity'

        }).then(function successCallback(response) {
            console.log(response);
            $scope.cites = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    } 

    $scope.cancelFormData = function () {
        $scope.form = {};
    }


    $scope.addEditCity= function () {
        if ($scope.form.id == '' || $scope.form.id == undefined) {
            var method = "POST";
            var url = 'location/addCity';
        } else {
            var method = "PUT";
            var url = 'location/addCity';
        }
        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };
    function _success(response) {
        console.log(response);
        autoCityListFetch();
        $scope.form = {};
        Swal.fire({
            text: response.data,
            icon: "success",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }

    function _error(response) {
        console.log(response);
        
        Swal.fire({
            text: response.data,
            icon: "error",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }


    $scope.editHoliday = function (id) {
        
        $http({
            method: 'GET',
            params: { 'id': id },
            url: 'location/findCityById'

        }).then(function successCallback(response) {
            console.log(response.data);
            $scope.form = response.data;
            
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.deleteHoliday = function (id) {
        Swal.fire({
            title: 'Are you sure?',
            text: "you want to delete this Holiday!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                var method = "DELETE";
                var url = 'location/';
                $http({
                    method: method,
                    params: { 'id': id },
                    url: url,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    transformResponse: angular.identity
                }).then(function successCallback(response) {
                    console.log(response);
                    
                    Swal.fire(
                        'Deleted!',
                        response.data,
                        'success'
                    )
                    autoCityListFetch();
                }, function errorCallback(response) {
                    console.log(response);
                });

            }
        })
    }

   
});


app.controller("LocationController", function ($scope, $http) {

    $scope.form = {};
    $scope.views = {};
    showHideLoad(true);
    $scope.views.list = true;

    autoCityListFetch();
    function autoCityListFetch() {
        $http({
            method: 'GET',
            url: 'location/fetchAllCity'

        }).then(function successCallback(response) {
            console.log(response);
            $scope.cites = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    } 

    $scope.cancelFormData = function () {
        $scope.form = {};
    }


    $scope.addEditCity= function () {
        if ($scope.form.id == '' || $scope.form.id == undefined) {
            var method = "POST";
            var url = 'location/addCity';
        } else {
            var method = "PUT";
            var url = 'location/addCity';
        }
        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };
    function _success(response) {
        console.log(response);
        autoCityListFetch();
        $scope.form = {};
        Swal.fire({
            text: response.data,
            icon: "success",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }

    function _error(response) {
        console.log(response);
        
        Swal.fire({
            text: response.data,
            icon: "error",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }


    $scope.editHoliday = function (id) {
        
        $http({
            method: 'GET',
            params: { 'id': id },
            url: 'location/findCityById'

        }).then(function successCallback(response) {
            console.log(response.data);
            $scope.form = response.data;
            
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    $scope.deleteHoliday = function (id) {
        Swal.fire({
            title: 'Are you sure?',
            text: "you want to delete this Holiday!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                var method = "DELETE";
                var url = 'location/';
                $http({
                    method: method,
                    params: { 'id': id },
                    url: url,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    transformResponse: angular.identity
                }).then(function successCallback(response) {
                    console.log(response);
                    
                    Swal.fire(
                        'Deleted!',
                        response.data,
                        'success'
                    )
                    autoCityListFetch();
                }, function errorCallback(response) {
                    console.log(response);
                });

            }
        })
    }

   
});