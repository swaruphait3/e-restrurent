var app = angular.module("RestrurentModule", ['ckeditor']);

// DIRECTIVE - FILE MODEL
app.directive("fileModel", [
    "$parse",
    function ($parse) {
        return {
            restrict: "A",
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind("change", function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            },
        };
    },
]);

//Forget Pass Controller
app.controller("RestrurentController", function ($scope, $http) {

    $scope.form = {};
    $scope.views = {};
    showHideLoad(true);
    $scope.views.list = true;

    $scope.data = {
        textInput: 'pretext',
        options: {
          language: 'en',
          allowedContent: true,
          entities: false
        }
      };

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
      
    autoRestrurentListFetch();

    function autoRestrurentListFetch() {
       // alert(" Fetch Hi-------------------------")
        $http({
            method: 'GET',
            url: 'restrurent/findAll'

        }).then(function successCallback(response) {
            console.log(response);
            $scope.restrurents = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


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

    $scope.addRow = function () {
        $scope.uploadModels.push({ type: '', fileName: '', fileLink: '', regularId: $scope.workBaseId });
    };

    $scope.removeRow = function (index) {
        $scope.uploadModels.splice(index, 1);
    };

    $scope.uploadFileModel = function (id) {
        $scope.uploadModels = [{ type: '', fileName: '', fileLink: ''}];
        $("#upload_modal").modal("show");
    }


    $scope.locationListbyCity= function (id) {
        $http({
            method: 'GET',
            params: { 'cityId': id },
            url: 'location/getLocationFindByCity'

        }).then(function successCallback(response) {
            $scope.locations = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    app.directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;
    
                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]); // Only take first file
                    });
                });
            }
        };
    }]);
    
    $scope.addEditRestrurents = function () {
        var formData = new FormData();
        formData.append("name", $scope.form.name);
        formData.append("cityId", $scope.form.cityId);
        formData.append("locId", $scope.form.locId);
        formData.append("email", $scope.form.email);
        formData.append("contact", $scope.form.contact);
        formData.append("password", $scope.form.password);
        formData.append("address", $scope.form.address);
        formData.append("specality", $scope.form.specality);
    
        if ($scope.form.Images) {
            formData.append("images", $scope.form.Images); // Ensure this matches your backend field name
        }
    
        // ✅ Debugging: Check if formData is correctly populated
        for (let [key, value] of formData.entries()) {
            console.log(key, value);
        }
    
        $http.post('restrurent/addResaturant', formData, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(function (response) {
            $("#newRestaurant").modal("hide");
            autoRestrurentListFetch();
    
            Swal.fire({
                text: response.data.message,
                icon: "success",
                confirmButtonText: "Ok, got it!",
                customClass: { confirmButton: "btn btn-primary" }
            });
    
            console.log(response.data);
        }).catch(function (error) {
            Swal.fire({
                text: error.data?.message || "An error occurred",
                icon: "error",
                confirmButtonText: "Ok, got it!",
                customClass: { confirmButton: "btn btn-primary" }
            });
    
            console.error("Error:", error);
        });
    };
    


    // $scope.addEditRestrurents = function () {
    //     if ($scope.form.id == '' || $scope.form.id == undefined) {
    //         var method = "POST";
    //         var url = 'restrurent/add';
    //     } else {
    //         var method = "PUT";
    //         var url = 'restrurent/edit';
    //     }
    //     $http({
    //         method: method,
    //         url: url,
    //         data: angular.toJson($scope.form),
    //         headers: {
    //             'Content-Type': 'application/json'
    //         },
    //         transformResponse: angular.identity
    //     }).then(_success, _error);
    // };
    // function _success(response) {

    //     console.log(response);
    //     $("#test_new").modal("hide");
    //     autoRestrurentListFetch();
    //     Swal.fire({
    //         text: response.data,
    //         icon: "success",
    //         buttonsStyling: !1,
    //         confirmButtonText: "Ok, got it!",
    //         customClass: {
    //             confirmButton: "btn btn-primary"
    //         }
    //     })
    // }

    // function _error(response) {
    //     console.log(response);

    //     Swal.fire({
    //         text: response.data,
    //         icon: "error",
    //         buttonsStyling: !1,
    //         confirmButtonText: "Ok, got it!",
    //         customClass: {
    //             confirmButton: "btn btn-primary"
    //         }
    //     })
    // }


    $scope.editRegularClasses = function (id) {
     //   alert("hi------------")
        $http({
            method: 'GET',
            params: { 'id': id },
            url: 'restrurent/findById'

        }).then(function successCallback(response) {
            console.log(response.data);
            $scope.form = response.data;
            $("#test_new").modal("show");
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }
    
    $scope.openNewRestaurant = function () {

        $scope.form = {};
        $("#newRestaurant").modal("show");


    }

    $scope.deleteRegularClasses = function (id) {
        Swal.fire({
            title: 'Are you sure?',
            text: "you want to change the status!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Status Changed!'
        }).then((result) => {
            if (result.isConfirmed) {

                var method = "DELETE";
                var url = 'deleteRegularClasses';
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

                    Swal.fire({
                        text: response.data,
                        icon: "success",
                        buttonsStyling: !1,
                        confirmButtonText: "Ok, got it!",
                        customClass: {
                            confirmButton: "btn btn-primary"
                        }
                    })
                    autoRegularClassesListFetch();
                }, function errorCallback(response) {
                    console.log(response);
                });

            }
        })
    }

    $scope.uploadSubmit = function () {
        console.log($scope.uploadModels);
        $http({
            method: 'PUT',
            url: 'updateRegularClassesUploads',
            data: angular.toJson($scope.uploadModels),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity

        }).then(function successCallback(response) {
            autoRegularClassesListFetch();
            $("#upload_modal").modal("hide");
            Swal.fire({
                text: response.data,
                icon: "success",
                buttonsStyling: !1,
                confirmButtonText: "Ok, got it!",
                customClass: {
                    confirmButton: "btn btn-primary"
                }
            })
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }







    $scope.deleteRegularClassesImage = function (id) {
        Swal.fire({
          title: "Are you sure?",
          text: "you want to Delete Image!",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "Yes, Changed!",
        }).then((result) => {
          if (result.isConfirmed) {
            var method = "DELETE";
            var url = "deleteRegularClassesImage";
            $http({
              method: method,
              params: { id: id },
              url: url,
              headers: {
                "Content-Type": "application/json",
              },
              transformResponse: angular.identity,
            }).then(
              function successCallback(response) {
                console.log(response);
    
                Swal.fire({
                  text: response.data,
                  icon: "success",
                  buttonsStyling: !1,
                  confirmButtonText: "Ok, got it!",
                  customClass: {
                    confirmButton: "btn btn-primary",
                  },
                });
                $("#view_image").modal("hide");
                autoRegularClassesListFetch();
              },
              function errorCallback(response) {
                console.log(response);
              }
            );
          }
        });
      };
});