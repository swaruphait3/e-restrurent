var app = angular.module("SalesReportModule", []);
app.controller("SalesReportController", function ($scope, $http) {

    $scope.form = {};
    $scope.views = {};
    $scope.orderList ={};
    autoOrderListFetch();
    function autoOrderListFetch() {
        $http({
            method: 'GET',
            url: 'order/viewRestrurentWiseBill'

        }).then(function successCallback(response) {
            $scope.orderList = response.data.data;
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }


    $scope.fetchSalesDataDateWise = function() {
		$http({
			method: 'GET',
			url: 'order/viewRestrurentWiseBillDateWise',
			params: {
				"startDate": $scope.date.startdate,
				"endDate": $scope.date.enddate
			}
		}).then(function successCallback(response) {
			$scope.orderList = response.data.data;
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: response.data.message,
				showConfirmButton: true,
			}).then(function() {
				$scope.refresh();
			});
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	};

    var SalesReport = {
		headers: true,
		columns: [{
			columnid: 'itemName',
			title: 'Item Name'
		}, {
			columnid: 'purchaseDate',
			title: 'Purchase Date'
		}, {
			columnid: 'rate',
			title: 'Rate'
		}, {
			columnid: 'qty',
			title: 'Qty.'
		}, {
			columnid: 'net',
			title: 'Net Amount'
		}, {
			columnid: 'tax',
			title: 'Tax Amount'
		}, {
			columnid: 'gross',
			title: 'Gross Amount'
		}, {
			columnid: 'modeOfPayment',
			title: 'Mode Of Payment'
		}, {
			columnid: 'platformCharge',
			title: 'Platform Charge'
		}, {
			columnid: 'netEarning',
			title: 'Earnig'
		}
		],
	};


    $scope.exportData = function () {
		alasql(
			'SELECT * INTO XLS("SalesReport.xls",?) FROM ?',
			[SalesReport, $scope.orderList]);

	};


    $scope.printData = function () {
		printData();

	};
	function printData() {

		var sTable = document.getElementById('salesReport').innerHTML;

		var style = "<style>";
		style = style
			+ "table {width: 100%;font: 17px Calibri;}";
		style = style
			+ "table, th, td {border: solid 1px #DDD; border-collapse: collapse;";
		style = style
			+ "padding: 2px 3px;text-align: center;}";
		style = style + "</style>";

		// CREATE A WINDOW OBJECT.
		var win = window.open('', '',
			'height=700,width=700');

		win.document.write('<html><head>');
		win.document.write('<h1>Status Report</h1>'); // <title> FOR PDF HEADER.
		win.document.write(style); // ADD STYLE INSIDE THE HEAD TAG.
		win.document.write('</head>');
		win.document.write('<body>');
		win.document.write(sTable); // THE TABLE CONTENTS INSIDE THE BODY TAG.
		win.document.write('</body></html>');

		win.document.close(); // CLOSE THE CURRENT WINDOW.

		win.print(); // PRINT THE CONTENTS.

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
        $("#newDelivaryBoy").modal("show");


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