var app = angular.module("forgetpassword", []);

app.controller("resetpassword", function ($scope, $http) {

    $scope.search = {};
    $scope.form = {};
    $scope.form.otp = false;
    showHideLoad(true);
    $scope.getOtp = function () {
        showHideLoad();
        console.log($scope.form);
        $http({
            method: "PUT",
            data: angular.toJson($scope.form),
            url: "user/otpGenerate",
            headers: {
                "Content-Type": "application/json",
            },
            transformResponse: angular.identity,
        }).then(
            function successCallback(response) {
                showHideLoad(true);
                $scope.form.otp = !$scope.form.otp;
                if (!$scope.form.otp) {
                    dvForgetPass.classList.add('d-none');
                    dvLogin.classList.remove('d-none');
                    $scope.form = {};
                    $scope.form.otp = false;
                }
                Swal.fire({
                    text: response.data,
                    icon: "success",
                    buttonsStyling: !1,
                    confirmButtonText: "Ok, got it!",
                    customClass: {
                        confirmButton: "btn btn-primary",
                    },
                });
            },
            function errorCallback(response) {
                showHideLoad(true);
                Swal.fire({
                    text: response.data,
                    icon: "error",
                    buttonsStyling: !1,
                    confirmButtonText: "Ok, got it!",
                    customClass: {
                      confirmButton: "btn btn-primary",
                    },
                  });
            }
        );
    };
    $scope.openModal = function () {
        alert("hi");
    }
    $scope.closeModal = function () {
        $scope.search = {};
        $scope.categorys = JSON.parse(JSON.stringify($scope.data));
        $("#exampleModal").modal("hide");
    }


});

