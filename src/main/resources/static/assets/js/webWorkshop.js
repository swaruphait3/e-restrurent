var app = angular.module("webWorkshopModule", ['ngSanitize']);

app.controller("webWorkshopController", function ($scope, $http) {
  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "file/getWorkShopForBooking",
    }).then(
      function successCallback(response) {
        console.log(response.data);
        $scope.listWorkshop = response.data;
        //       alert( JSON.stringify($scope.listWorkshop) )
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }
});

app.controller("weeklySpecialController", function ($scope, $http, $sce) {

  showHideLoad(true);
  $scope.trustAsHtml = function (html) {
    return $sce.trustAsHtml(html);
  };


  getParamData();
  function getParamData() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");
    $scope.workshopId = id;
    $scope.form = {};
    $http({
      method: "GET",
      params: { id: $scope.workshopId },
      url: "public/getWorkshopsByid",
    }).then(
      function successCallback(response) {
        console.log(response.data);
        $scope.workshops = response.data;
        $scope.form.name = $scope.workshops.name;
        $scope.form.workshopId = $scope.workshops.id;
        $scope.form.date = $scope.workshops.date;
        $scope.form.startTime = $scope.workshops.startTime;
        $scope.form.endTime = $scope.workshops.endTime;
        $scope.form.availableSeats = $scope.workshops.availableSeats;
        $scope.form.price = $scope.workshops.price;
        $scope.form.holdSeats = 1;
        $scope.form.paymentStatus = "FP";
        $scope.form.holdAmount = $scope.workshops.price;
        $scope.form.dueAmount = 0.0;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }


  $scope.emailSender = function (id) {
    showHideLoad();
    var method = "GET";
    var url = "workShopConfirmMail";
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
        showHideLoad(true);
        console.log(response);
        alert("Email Sended Successfully");
      },
      function errorCallback(response) {
        showHideLoad(true);
        console.log(response);
        alert("EROOR to Send Mail");
      }
    );
  };


  $scope.getNetAmount = function (seat) {
    $scope.form.holdAmount = $scope.form.price * seat;
  };


  $scope.proceedToPaymentWorkshop = function () {
    if ($scope.form.paymentStatus === "FP") {
      if (
        $scope.form.holdAmount !==
        $scope.form.price * $scope.form.holdSeats
      ) {
        Swal.fire({
          text: "Price Mismatch",
          icon: "error",
          buttonsStyling: !1,
          confirmButtonText: "Ok, got it!",
          customClass: {
            confirmButton: "btn btn-primary",
          },
        });
        return;
      }
    }

    if ($scope.form.paymentStatus === "PP") {
      if (
        $scope.form.price * $scope.form.holdSeats !==
        $scope.form.holdAmount + $scope.form.dueAmount
      ) {
        Swal.fire({
          text: "Price Mismatch",
          icon: "error",
          buttonsStyling: !1,
          confirmButtonText: "Ok, got it!",
          customClass: {
            confirmButton: "btn btn-primary",
          },
        });
        return;
      }
    }

    $scope.paymentGateway($scope.form.holdAmount, $scope.form.workshopId);

    console.log($scope.form.holdAmount, $scope.form.workshopId);
};

// Payment Gateway initialization function
$scope.paymentGateway = function (amount, workshopId) {
    showHideLoad();

    $http({
        method: "POST",
        url: "createOrder",
        data: { amount: amount.toString(), id: workshopId, type: 'W' },
        headers: { "Content-Type": "application/json" }
    }).then(
        function (response) {
            handlePaymentResponse(response.data);
            console.log(response.data);
        },
        function (error) {
            showHideLoad(true);
            console.error("Error creating payment order:", error);
            showAlert("Error creating payment order", "error");
        }
    );
};

function handlePaymentResponse(data) {
    showHideLoad(true);

    if (data.status === "created") {
        const options = {
            key: 'rzp_live_03ZqIGhx4BVNIM',
            // key: 'rzp_test_QpuyoJh4EuusxU',
            amount: data.amount,
            currency: "INR",
            name: "Art Rickshaw",
            description: "Payable Amount",
            image: "assets/images/brad-logo-circle.png",
            order_id: data.id,
            handler: function (response) {
                $scope.addEditWorkshopBookings(response.razorpay_payment_id, response.razorpay_order_id, "paid");
                // $scope.createOrUpdateBooking();
               // $scope.activityEmailSender($scope.id);
                swal("Good Job !!", "Payment Successfull !!", "Success");
            },
            prefill: { name: "", email: "", contact: "" },
            notes: { address: "Art Rickshaw" },
            theme: { color: "#0C86F6" },
        };

        const rzp = new Razorpay(options);

        rzp.on("payment.failed", function () {
            showHideLoad(true);
            swal("Failed !!", "Payment failed.", "error");
        });

        rzp.on("modal.close", function () {
            showHideLoad(true);
            swal("Payment Failed", "You closed the payment popup. Please try again.", "error");
        });

        rzp.open();
    } else {
        showAlert("Failed to create payment order", "error");
    }
}

// $scope.paymentGateway = function (amount, id) {
//   showHideLoad();
//   //alert(id);
//   console.log("payment started");
//   //     var amount = 100;
//   console.log(id);
//   console.log(amount);
//   var redirectUrl = "booking-confirm?workshopBookingId=" + id;
//   if (!amount) {
//     alert("Amount is required");
//     return;
//   }

//   if (id !== "Updated") {
//     // Send request to server to create order using $http service
//     $http({
//       method: "POST",
//       url: "createOrder",
//       // data: { amount: amount, info: 'order_request' },
//       params: {
//         amount: amount,
//         id: id,
//         type: "W",
//       },
//       // headers: { 'Content-Type': 'application/json' }
//     }).then(
//       function (response) {
//         showHideLoad(true);
//         // Success callback
//         //console.log(response.data);

//         if (response.data.status == "created") {
//           // Open payment form
//           var options = {
//             // key: "rzp_test_QpuyoJh4EuusxU",
//             // Payment id of Art-Rickshaw (8017178277)
//             // key: 'rzp_test_JOC0wRKpLH1cVW',
//             // Payment id of Art-Rickshaw Live (8017178277)
//             key: 'rzp_live_03ZqIGhx4BVNIM',
//             amount: response.data.amount,
//             currency: "INR",
//             name: "Art Rickshaw",
//             description: "Payable Amount",
//             image: (src = "assets/images/brad-logo-circle.png"),
//             order_id: response.data.id,
//             handler: function (response) {
//               // console.log("Id: " + $scope.id);
//               // console.log(response.razorpay_payment_id);
//               // console.log(response.razorpay_order_id);
//               // console.log(response.razorpay_signature);
//               // console.log("Payment successful!!");
//               window.location.href = redirectUrl;
//               $scope.updatePaymentOnServer(
//                 $scope.id,
//                 response.razorpay_payment_id,
//                 response.razorpay_order_id,
//                 "paid"
//               );
//               $scope.emailSender($scope.id);
//               // updatePaymentOnServer();
//               // alert("Congrates !! Payment Successful !!");
//               swal("Good Job !!", "Payment Successfull !!", "Success");
//             },
//             prefill: {
//               name: "",
//               email: "",
//               contact: "",
//             },
//             notes: {
//               address: "Art Rickshaw",
//             },
//             theme: {
//               color: "#0C86F6",
//             },
//           };
//           var rzp = new Razorpay(options);
//           rzp.on("payment.failed", function (response) {
//             showHideLoad(true);
//             console.log(response.error.code);
//             console.log(response.error.description);
//             console.log(response.error.source);
//             console.log(response.error.step);
//             console.log(response.error.reason);
//             console.log(response.error.metadata.order_id);
//             console.log(response.error.metadata.payment_id);
//             // alert("Payment is Failled!!")
//             swal("Failed !!", "Oops payment failed !!", "error");
//           });
//           rzp.open();
//         }
//       },
//       function (error) {
//         showHideLoad(true);
//         // Error callback
//         console.log(error);
//         alert("Something went wrong !!");
//       }
//     );
//   } else {
//     showHideLoad(true);
//     autoBookingsListFetch();
//     Swal.fire({
//       text: "Sucessfully Saved",
//       icon: "success",
//       buttonsStyling: !1,
//       confirmButtonText: "Ok, got it!",
//       customClass: {
//         confirmButton: "btn btn-primary",
//       },
//     });
//   }
// };

// Create or update the booking after payment success


$scope.addEditWorkshopBookings = function (paymentId, orderId, status) {
    const method = $scope.form.id ? "PUT" : "POST";
    const url = $scope.form.id ? "updateWorkshopBooking" : "postWorkshopBooking";

     // Set payment details in form data
        $scope.form.paymentId = paymentId;
        $scope.form.orderId = orderId;
        $scope.form.paymentStatus = status;

    $http({
        method: method,
        url: url,
        data: angular.toJson($scope.form),
        headers: { "Content-Type": "application/json" }
    }).then(
        function (response) {
            $scope.id = response.data;
            $scope.updatePaymentOnServer($scope.id, paymentId, orderId, "paid").then(() => {
                $scope.emailSender($scope.id);
                window.location.href = "booking-confirm?workshopBookingId=" + $scope.id;
               
            }).catch(error => {
                console.error("Error updating payment on server:", error);
            });
        },
        function (error) {
            console.error("Error creating booking:", error);
            showAlert("Error creating booking: " + error.statusText, "error");
        }
    );
};

  // Update payment details on the server
  $scope.updatePaymentOnServer = function (id, razorpay_payment_id, razorpay_order_id, status) {
    showHideLoad();
    return $http({
        method: "PUT",
        url: "updateOrder",
        params: {
            id: id,
            razorpay_payment_id: razorpay_payment_id,
            razorpay_order_id: razorpay_order_id,
            status: status,
            type: 'W'
        }
    }).then(
        function (response) {
            showHideLoad(true);
            Swal.fire({
                text: "Successfully Saved",
                icon: "success",
                confirmButtonText: "Ok, got it!",
                customClass: { confirmButton: "btn btn-primary" }
            });
        },
        function (error) {
            showHideLoad(true);
            showAlert("Failed to update payment details on server", "error");
            console.log("Error updating payment:", error.statusText);
        }
    );
  };

  // Helper function to show alerts
  function showAlert(message, type) {
    Swal.fire({
        title: type === "error" ? 'Error' : 'Success',
        text: message,
        icon: type,
        confirmButtonText: 'OK'
    });
}

  // $scope.addEditWorkshopBookings = function () {
  //   showHideLoad();
  //   if ($scope.form.paymentStatus === "FP") {
  //     if (
  //       $scope.form.holdAmount !==
  //       $scope.form.price * $scope.form.holdSeats
  //     ) {
  //       Swal.fire({
  //         text: "Price Mismatch",
  //         icon: "error",
  //         buttonsStyling: !1,
  //         confirmButtonText: "Ok, got it!",
  //         customClass: {
  //           confirmButton: "btn btn-primary",
  //         },
  //       });
  //       return;
  //     }
  //   }

  //   if ($scope.form.paymentStatus === "PP") {
  //     if (
  //       $scope.form.price * $scope.form.holdSeats !==
  //       $scope.form.holdAmount + $scope.form.dueAmount
  //     ) {
  //       Swal.fire({
  //         text: "Price Mismatch",
  //         icon: "error",
  //         buttonsStyling: !1,
  //         confirmButtonText: "Ok, got it!",
  //         customClass: {
  //           confirmButton: "btn btn-primary",
  //         },
  //       });
  //       return;
  //     }
  //   }

  //   if ($scope.form.id == "" || $scope.form.id == undefined) {
  //     var method = "POST";
  //     var url = "postWorkshopBooking";
  //   } else {
  //     var method = "PUT";
  //     var url = "updateWorkshopBooking";
  //   }
  //   $http({
  //     method: method,
  //     url: url,
  //     data: angular.toJson($scope.form),
  //     headers: {
  //       "Content-Type": "application/json",
  //     },
  //     transformResponse: angular.identity,
  //   }).then(
  //     function successCallback(response) {
  //       showHideLoad(true);
  //       //       alert(response.data);
  //       $scope.id = response.data;
  //       console.log(response.data);
  //       //        alert($scope.form.workshopId);
  //       // autoWorkshopFetch();
  //       if ($scope.form.holdAmount > 0) {
  //         $scope.paymentGateway($scope.form.holdAmount, $scope.id);
  //       }
  //     },
  //     function errorCallback(response) {
  //       showHideLoad(true);
  //       console.log(response.statusText);
  //     }
  //   );
  // };

  $scope.editWorkshopBookings = function (id) {
    $scope.form = $scope.listWorkshop.find(function (item) {
      return item.id === id;
    });
    $scope.getActiveWorkshop();
    $("#workshop_modal").modal("show");
  };

  $scope.deleteWorkshopBookings = function (id) {
    Swal.fire({
      title: "Are you sure?",
      text: "you want to delete this Workshop Booking!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.isConfirmed) {
        var method = "DELETE";
        var url = "deleteWorkshopBooking";
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

            Swal.fire("Deleted!", response.data, "success");
            autoWorkshopFetch();
          },
          function errorCallback(response) {
            console.log(response);
          }
        );
      }
    });
  };

  // <!-- =============================================================== -->
  // <!-- %%%%%%%%%%%%%%%% Workshop Booking END %%%%%%%%%%%%%%%% -->
  // <!-- =============================================================== -->

  $scope.Export = function () {
    $("#tblBooking").table2excel({
      filename: "Bookings.xls",
    });
  };

  // $scope.paymentGateway = function (amount, id) {
  //   showHideLoad();
  //   //alert(id);
  //   console.log("payment started");
  //   //     var amount = 100;
  //   console.log(id);
  //   console.log(amount);
  //   var redirectUrl = "booking-confirm?workshopBookingId=" + id;
  //   if (!amount) {
  //     alert("Amount is required");
  //     return;
  //   }

  //   if (id !== "Updated") {
  //     // Send request to server to create order using $http service
  //     $http({
  //       method: "POST",
  //       url: "createOrder",
  //       // data: { amount: amount, info: 'order_request' },
  //       params: {
  //         amount: amount,
  //         id: id,
  //         type: "W",
  //       },
  //       // headers: { 'Content-Type': 'application/json' }
  //     }).then(
  //       function (response) {
  //         showHideLoad(true);
  //         // Success callback
  //         //console.log(response.data);

  //         if (response.data.status == "created") {
  //           // Open payment form
  //           var options = {
  //             // key: "rzp_test_QpuyoJh4EuusxU",
  //             // Payment id of Art-Rickshaw (8017178277)
  //             // key: 'rzp_test_JOC0wRKpLH1cVW',
  //             // Payment id of Art-Rickshaw Live (8017178277)
  //             key: 'rzp_live_03ZqIGhx4BVNIM',
  //             amount: response.data.amount,
  //             currency: "INR",
  //             name: "Art Rickshaw",
  //             description: "Payable Amount",
  //             image: (src = "assets/images/brad-logo-circle.png"),
  //             order_id: response.data.id,
  //             handler: function (response) {
  //               // console.log("Id: " + $scope.id);
  //               // console.log(response.razorpay_payment_id);
  //               // console.log(response.razorpay_order_id);
  //               // console.log(response.razorpay_signature);
  //               // console.log("Payment successful!!");
  //               window.location.href = redirectUrl;
  //               $scope.updatePaymentOnServer(
  //                 $scope.id,
  //                 response.razorpay_payment_id,
  //                 response.razorpay_order_id,
  //                 "paid"
  //               );
  //               $scope.emailSender($scope.id);
  //               // updatePaymentOnServer();
  //               // alert("Congrates !! Payment Successful !!");
  //               swal("Good Job !!", "Payment Successfull !!", "Success");
  //             },
  //             prefill: {
  //               name: "",
  //               email: "",
  //               contact: "",
  //             },
  //             notes: {
  //               address: "Art Rickshaw",
  //             },
  //             theme: {
  //               color: "#0C86F6",
  //             },
  //           };
  //           var rzp = new Razorpay(options);
  //           rzp.on("payment.failed", function (response) {
  //             showHideLoad(true);
  //             console.log(response.error.code);
  //             console.log(response.error.description);
  //             console.log(response.error.source);
  //             console.log(response.error.step);
  //             console.log(response.error.reason);
  //             console.log(response.error.metadata.order_id);
  //             console.log(response.error.metadata.payment_id);
  //             // alert("Payment is Failled!!")
  //             swal("Failed !!", "Oops payment failed !!", "error");
  //           });
  //           rzp.open();
  //         }
  //       },
  //       function (error) {
  //         showHideLoad(true);
  //         // Error callback
  //         console.log(error);
  //         alert("Something went wrong !!");
  //       }
  //     );
  //   } else {
  //     showHideLoad(true);
  //     autoBookingsListFetch();
  //     Swal.fire({
  //       text: "Sucessfully Saved",
  //       icon: "success",
  //       buttonsStyling: !1,
  //       confirmButtonText: "Ok, got it!",
  //       customClass: {
  //         confirmButton: "btn btn-primary",
  //       },
  //     });
  //   }
  // };

  // $scope.updatePaymentOnServer = function (
  //   id,
  //   razorpay_payment_id,
  //   razorpay_order_id,
  //   status
  // ) {
  //   showHideLoad();
  //   // alert("Id: "+  id )
  //   // alert("razorpay_payment_id: " + razorpay_payment_id);
  //   // alert("razorpay_order_id: " + razorpay_order_id);
  //   // alert("status: " + status);
  //   $http({
  //     method: "PUT",
  //     url: "updateOrder",
  //     params: {
  //       id: id,
  //       razorpay_payment_id: razorpay_payment_id,
  //       razorpay_order_id: razorpay_order_id,
  //       status: status,
  //       type: "W",
  //     },
  //   }).then(
  //     function successCallback(response) {
  //       showHideLoad(true);
  //       // alert("Hi..");
  //       console.log(response.data);
  //       Swal.fire({
  //         text: "Successfully Saved",
  //         icon: "success",
  //         buttonsStyling: false,
  //         confirmButtonText: "Ok, got it!",
  //         customClass: {
  //           confirmButton: "btn btn-primary",
  //         },
  //       });
  //     },
  //     function errorCallback(response) {
  //       showHideLoad(true);
  //       console.log(response.statusText);
  //     }
  //   );
  // };

});
