var app = angular.module("IndexModule", ['ngSanitize']);

app.controller("IndexController", function ($scope, $http) {

  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "food/findAllActiveList",
    }).then(
      function successCallback(response) {


        // console.log(response.data);
        $scope.foods = response.data.data;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

  autoActivityListFetch();
  function autoActivityListFetch() {
    $http({
      method: "GET",
      url: "restrurent/findAllActiveList",
    }).then(
      function successCallback(response) {
        // console.log(response);
        $scope.restrurents = response.data.data.filter(function (item) {
          return item.priority !== 0;
        });
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

  // $scope.ItemsListbyRestrurent = function (id) {
  //   $http({
  //     method: 'GET',
  //     params: { 'id': id },
  //     url: 'food/findItemsByResturentId'
  //   }).then(function successCallback(response) {
  //     console.log(response);
      
  //     $scope.$apply(() => { 
  //       $scope.items = response.data.data;
  //     });
  
  //     alert(JSON.stringify(response.data.data)); 
  //   }, function errorCallback(response) {
  //     console.log(response.statusText);
  //   });
  // };
  
});


app.controller("RestrurentItemController", function ($scope, $http) {


  autoWorkshopFetch();
  function autoWorkshopFetch() {

    const params = new URLSearchParams(window.location.search);
        const restId = params.get("restid");
        $scope.activityId = restId;

    $http({
      method: "GET",
      params: { id: $scope.activityId },
      url: "food/findItemsByResturentId",
    }).then(
      function successCallback(response) {
        $scope.foods = response.data.data;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

});

app.controller("OrderItemController", function ($scope, $http) {

  $scope.itemName= "";
  $scope.images = "";
  $scope.form = {};

  
  autoWorkshopFetch();
  function autoWorkshopFetch() {

    const params = new URLSearchParams(window.location.search);
        const restId = params.get("itemId");
        $scope.activityId = restId;
        // $scope.form.itemId = $scope.activityId;
    $http({
      method: "GET",
      params: { id: $scope.activityId },
      url: "food/findById",
    }).then(
      function successCallback(response) {
        $scope.foods = response.data.data;
        $scope.form =  $scope.foods;
        $scope.itemName = $scope.foods.name;
        $scope.images = $scope.foods.images;
        $scope.form.rate = $scope.foods.price;
        $scope.form.itemId = response.data.data.id;
        $scope.form.restId = response.data.data.restId;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }


  $scope.priceCalculate = function (qty) {
    $scope.form.totalAmount = $scope.form.rate * qty;
  }

  $scope.proceedToOrder = function () {
        $http({
            method: "POST",
            url: 'order/purchase',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(function (response) {
      Swal.fire({
          text: response.data.message,
          icon: "success",
          buttonsStyling: !1,
          confirmButtonText: "Ok, got it!",
          customClass: {
              confirmButton: "btn btn-primary"
          }
      })
          console.log(response.data);
      }).catch(function (error) {
          Swal.fire({
              text: response.data.message,
              icon: "error",
              buttonsStyling: !1,
              confirmButtonText: "Ok, got it!",
              customClass: {
                  confirmButton: "btn btn-primary"
              }
          })
          console.error(error);
      });
    };

});


app.controller("OrderListController", function ($scope, $http,  $window) {

  $scope.itemName= "";
  $scope.images = "";
  $scope.form = {};
  $scope.ordersLength = null;

  $scope.billDate = null;
  $scope.billNo = null;
  $scope.sgst = null;
  $scope.cgst = null;
  $scope.netAmount = null;
  $scope.item = null;
  $scope.rate = null;
  $scope.qty = null;
  $scope.payMode = null;
  $scope.total = null;




  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "order/viewOrderPurchaser",
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

  viewBill

  $scope.viewBill = function (id) {
		$scope.form = {};
		$("#viewBill").modal("show");
		$scope.findByBillDetails(id);
	}

  $scope.generatePDF = function() {
    var element = document.getElementById('bill');
    html2pdf(element);
};


  $scope.findByBillDetails = function (id) {
       $http({
           method: 'GET',
           params: { 'id': id },
           url: 'order/viewBill'

       }).then(function successCallback(response) {
           console.log(response.data);
           $scope.billData = response.data.data;
           $scope.billDate = $scope.billData.purchaseDate;
           $scope.billNo = $scope.billData.id;
           $scope.sgst = $scope.billData.totalAmount * 0.05;
           $scope.cgst =  $scope.billData.totalAmount * 0.05;
           $scope.netAmount =($scope.billData.totalAmount - ($scope.billData.totalAmount * 0.10));
           $scope.item =  $scope.billData.itemName;;
           $scope.rate =  $scope.billData.rate;;
           $scope.qty =  $scope.billData.qty;;
           $scope.payMode =  $scope.billData.modeOfPayment;;
           $scope.total =  $scope.billData.totalAmount;;
         

       }, function errorCallback(response) {
           console.log(response.statusText);
       });
   }


  $scope.buyAgain = function(itemId) {
    $window.location.href = 'orederitem?itemId=' + itemId;
};


$scope.cancelOrder = function (id) {
  $http({
      method: 'GET',
      params: { 'id': id },
      url: 'order/cancelOrder'
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


$scope.printBill = function () {
  printBill();

};

function printBill() {
  var sTable = document.getElementById('billPrint').innerHTML;

  var style = "<style>";
  style += "body { font-family: Arial, sans-serif; }";
  style += ".bill { width: 50%; margin: auto; padding: auto border: 1px solid #ddd; }";
  style += "table { width: 100%; border-collapse: collapse; }";
  style += "th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }";
  style += ".total { font-weight: bold; }";
  style += ".center { text-align: center; }";
  style += "</style>";

  // CREATE A NEW WINDOW OBJECT
  var win = window.open('', '', 'height=700,width=700');

  win.document.write('<html><head><title>Restaurant Bill</title>');
  win.document.write(style); // ADD STYLE INSIDE HEAD
  win.document.write('</head><body>');
  win.document.write('<div class="bill">'); // ADD BILL CLASS WRAPPER
  win.document.write('<h2 class="center">Restaurant Bill</h2>');
  win.document.write(sTable); // INSERT TABLE CONTENT
  win.document.write('</div>');
  win.document.write('</body></html>');

  win.document.close(); // CLOSE THE DOCUMENT
  win.print(); // PRINT THE CONTENTS
}


});
