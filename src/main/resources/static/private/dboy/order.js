var app = angular.module("OrderDelivaryModule", ['ngSanitize']);

app.controller("OrderDelivaryController", function ($scope, $http) {

    $scope.itemName= "";
  $scope.images = "";
  $scope.form = {};
  $scope.ordersLength = null;

  
  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "order/pickOrderForDelivaryBoy",
    }).then(
      function successCallback(response) {
        $scope.orders = response.data.data;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }



  $scope.pickOrder = function (id) {
    $http({
        method: 'GET',
        params: { 'id': id },
        url: 'order/pickOrderDelivaryBoy'
    }).then(_success, _error);
};


$scope.delivaryOrder = function (id) {
  $http({
      method: 'GET',
      params: { 'id': id },
      url: 'order/delivaryOrder'
  }).then(_success, _error);
};



function _success(response) {
    autoWorkshopFetch();
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
