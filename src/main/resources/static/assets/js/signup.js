var app = angular.module("SignUpModule", []);


//Forget Pass Controller
app.controller("SignUpController", function ($scope, $http) {

    $scope.form = {};
    $scope.views = {};
    $scope.views.list = true;

    $scope.views.list = true;
	$scope.isPasswordVisible = false;


	$scope.togglePassword = function() {
		$scope.isPasswordVisible = !$scope.isPasswordVisible;
		var passwordInput = document.getElementById('idPassword');
		var toggleIcon = document.getElementById('togglePassword');
		
		if ($scope.isPasswordVisible) {
			passwordInput.type = 'text';
			toggleIcon.classList.remove('icon-toggle-pass-slash');
			toggleIcon.classList.add('icon-toggle-pass');
		} else {
			passwordInput.type = 'password';
			toggleIcon.classList.remove('icon-toggle-pass');
			toggleIcon.classList.add('icon-toggle-pass-slash');
		}
	};


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