var app = angular.module("ShopManagment", []);

app.controller("ShopManagmentController", function ($scope, $http, $timeout) {
	$scope.form = {};
	$scope.views = {};
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

	_autoShopListFetch();
	function _autoShopListFetch() {
		$http({
			method: 'GET',
			url: 'shop/findAll'

		}).then(function successCallback(response) {
			console.log(response);
			$scope.shopList = response.data.data;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	
	$scope.addShop = function () {
		var method = "POST";
		var url = 'shop/registation';
		console.log($scope.form);
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
		autoEmployeeListFetch();
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


	$scope.editSaveEmployee = function () {
		showHideLoad();
		var method = "PUT";
		var url = 'employee_master/updateEmployee';
		console.log($scope.form);
		$http({
			method: method,
			url: url,
			data: angular.toJson($scope.form),
			headers: {
				'Content-Type': 'application/json'
			},
			transformResponse: angular.identity
		}).then(function successCallback(response) {
            		Swal.fire({
            			position: 'center',
            			icon: 'success',
            			title: response.message,
            			showConfirmButton: true,
            		}).then(function () {
            			location.reload();
            		});
            		console.log("ID" + response);
            	}, function errorCallback(response) {
            		Swal.fire({
            			icon: 'error',
            			title: response.message,
            			text: 'Something went wrong!'
            		})
            		console.log("Error" + response);
            	});
            };

	// $scope.userActivation = function (str) {
	// 	$http({
	// 		method: 'GET',
	// 		url: 'employee_master/userActivation',
	// 		params: {
	// 			"id": str

	// 		},
	// 		headers: {
	// 			'Content-Type': 'application/json'
	// 		},
	// 		transformResponse: angular.identity

	// 	}).then(function successCallback(response) {
	// 		Swal.fire({
	// 			position: 'center',
	// 			icon: 'success',
	// 			title: response.data,
	// 			showConfirmButton: true,
	// 		}).then(function () {
	// 			location.reload();
	// 		});
	// 	}, function errorCallback(response) {
	// 		Swal.fire({
	// 			icon: 'error',
	// 			title: response.data,
	// 			text: 'Something went wrong!'
	// 		})
	// 		console.log(response.statusText);
	// 	});
	// };

	// $scope.userDeactivation = function (str) {
	// 	$http({
	// 		method: 'GET',
	// 		url: 'employee_master/userDeactivation',
	// 		params: {
	// 			"id": str

	// 		},
	// 		headers: {
	// 			'Content-Type': 'application/json'
	// 		},
	// 		transformResponse: angular.identity

	// 	}).then(function successCallback(response) {
	// 		Swal.fire({
	// 			position: 'center',
	// 			icon: 'success',
	// 			title: response.data,
	// 			showConfirmButton: true,
	// 		}).then(function () {
	// 			location.reload();
	// 		});
	// 	}, function errorCallback(response) {
	// 		Swal.fire({
	// 			icon: 'error',
	// 			title: response.data,
	// 			text: 'Something went wrong!'
	// 		})
	// 		console.log(response.statusText);
	// 	});
	// };

	// $scope.resetDeviceId = function (str) {
	// 	$http({
	// 		method: 'GET',
	// 		url: 'employee_master/resetDeviceId',
	// 		params: {
	// 			"id": str

	// 		},
	// 		headers: {
	// 			'Content-Type': 'application/json'
	// 		},
	// 		transformResponse: angular.identity

	// 	}).then(function successCallback(response) {
	// 		Swal.fire({
	// 			position: 'center',
	// 			icon: 'success',
	// 			title: response.data,
	// 			showConfirmButton: true,
	// 		}).then(function () {
	// 			location.reload();
	// 		});
	// 		console.log("ID" + response);
	// 	}, function errorCallback(response) {
	// 		Swal.fire({
	// 			icon: 'error',
	// 			title: response.data,
	// 			text: 'Something went wrong!'
	// 		})
	// 		console.log("Error" + response);
	// 	});
	// };

    // $scope.resetDeviceAllId = function () {
    //        $http({
    //            method: 'GET',
    //            url: 'employee_master/resetDeviceIdAll',
    //            headers: {
    //                'Content-Type': 'application/json'
    //            },
    //            transformResponse: angular.identity
   
    //        }).then(function successCallback(response) {
    //             Swal.fire({
    //                position: 'center',
    //                icon: 'success',
    //                title: response.data,
    //                showConfirmButton: true,
    //            }).then(function () {
    //                location.reload();
    //            });
    //            console.log("ID"+response);
    //        }, function errorCallback(response) {	
    //            console.log(response.statusText);
    //            console.log("Error"+response);
    //        });
    //    };

	// $scope.resetPassword = function (str) {

	// 	Swal.fire({
	// 		title: 'Are you sure?',
	// 		text: "You want to Reset Password!",
	// 		icon: 'warning',
	// 		showCancelButton: true,
	// 		confirmButtonColor: '#00B571',
	// 		cancelButtonColor: '#d33',
	// 		confirmButtonText: 'Yes...!'
	// 	}).then((result) => {
	// 		if (result.isConfirmed) {
	// 			$http({
	// 			     method: 'GET',
	// 				        url: 'employee_master/resetPassword',
	// 				        params: {
	// 				            "id": str
				
	// 				        },
	// 				        headers: {
	// 				            'Content-Type': 'application/json'
	// 				        },
	// 				        transformResponse: angular.identity
				
	// 				    }).then(function successCallback(response) {
	// 				        Swal.fire({
	// 				            position: 'center',
	// 				            icon: 'success',
	// 				            title: response.data,
	// 				            showConfirmButton: true,
	// 				        }).then(function () {
	// 				            location.reload();
	// 				        });
	// 				    }, function errorCallback(response) {
	// 						Swal.fire({
	// 							icon: 'error',
	// 							title:  response.data,
	// 							text: 'Something went wrong!'
	// 						  })
	// 				        console.log(response.statusText);
	// 				    });
	// 		} else {
	// 			location.reload();
	// 		}
	// 	})
	// };

	// $scope.changeView = function (view) {
	// 	if (view == "add" || view == "list" || view == "show") {
	// 		$scope.form = {};
	// 	}
	// 	$scope.views.add = false;
	// 	$scope.views.edit = false;
	// 	$scope.views.list = false;
	// 	$scope.views[view] = true;
	// }


});