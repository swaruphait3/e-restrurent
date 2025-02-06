var app = angular.module("SignUpModule", []);


//Forget Pass Controller
app.controller("SignUpController", function ($scope, $http) {

    $scope.form = {};
    $scope.views = {};
    $scope.views.list = true;



    $scope.addUser = function () {
        $http({
            method:  "POST",
            url: 'user/add',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(_success, _error);
    };

    function _success(response) {

        console.log(response);
        Swal.fire({
            text: response.data.message,
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
            text: response.data.message,
            icon: "error",
            buttonsStyling: !1,
            confirmButtonText: "Ok, got it!",
            customClass: {
                confirmButton: "btn btn-primary"
            }
        })
    }


});