var app = angular.module("OrderModule", ['ngSanitize']);

app.controller("OrderController", function ($scope, $http) {

    $scope.itemName= "";
  $scope.images = "";
  $scope.form = {};
  $scope.ordersLength = null;

  
  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "order/viewOrderRestaurant",
    }).then(
      function successCallback(response) {
        $scope.orders = response.data.data;
        $scope.ordersLength = $scope.orders.length; // Get the length here
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }


  $scope.confirmOrder = function (id) {
    $http({
        method: 'GET',
        params: { 'id': id },
        url: 'order/approveOrder'
    }).then(_success, _error);
};


$scope.readyOrder = function (id) {
  $http({
      method: 'GET',
      params: { 'id': id },
      url: 'order/readyOrder'
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
