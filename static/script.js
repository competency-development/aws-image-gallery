$(document).ready(function () {
  // TODO: Paste sign-out URL
  $("#sign-out").attr("href", "<paste_signout_url_here>");
  // TODO: Paste home URL
  $("#home").attr("href", "<paste_home_url_here>");

  let images = [];

  get("http://localhost:8080/images", function (data) {
    data.forEach((image) => images.push(image));
    loadMore(images);
  });

  function loadMore(images) {
    let template = $("#image-card-template").html();
    for (let i = 0; i < 6; i++) {
      let imageData = images.shift();
      if (imageData) {
        $(".row").append(Mustache.render(template, imageData));
      }
    }

    $(".image").unbind("click").on("click", bindExpandedImage);
    $(".info-button").unbind("click").on("click", showDescription);
    $(".play-button").unbind("click").on("click", openPlayer);
    $(".delete-button").unbind("click").on("click", deleteImage);

    hideAppearingElements();
    bindForMobileDevices();
  }

  // Clear description on click outside or "Info" button click
  $(document).on("mouseover", function (event) {
    if (!$(event.target).closest(".image-card").length) {
      hideAppearingElements();
    }
  });

  $("#upload-file").on("click", function (event) {
    $("#upload-file-input").trigger("click");
    event.preventDefault();
  });

  $("#upload-file-input").on("change", function () {
    const file = $(this).prop("files")[0];
    const url = "http://localhost:8080/images/upload";

    const formData = new FormData();
    formData.append("file", file);
    const fetchOptions = {
      method: "POST",
      body: formData,
    };

    fetch(url, fetchOptions)
      .then((response) => response.json())
      .then((data) => {
        var fileName = { fileName: file.name };

        let alertTemplate = $("#upload-alert-template").html();
        $(".header").prepend(Mustache.render(alertTemplate, fileName));

        let template = $("#image-card-template").html();
        $(".row").prepend(Mustache.render(template, data));
      });
  });

  $("#load-more").on("click", function (event) {
    event.preventDefault();
    loadMore(images);
    checkIfLoadMoreRequired();
  });

  checkIfLoadMoreRequired();

  function checkIfLoadMoreRequired() {
    if (images.length > 0) {
      $("#load-more").show();
    } else {
      $("#load-more").hide();
    }
  }

  // TODO: AWS Rekognition call
  function showDescription() {
    const parent = $(this).closest(".image-card");
    parent.find(".hover-buttons").toggle();
    parent.find(".description").addClass("appear-animation").toggle();
  }

  function openPlayer() {
    // TODO: Call POST for description
    /*post('urlToApiGateway', null, function (data) {
        var parent = $(this).closest('.image-card');
        parent.find('.hover-buttons').toggle();
        parent.find('.audio').addClass('appear-animation').append('<audio id="audio" src="'+ data +'" controls></audio>').toggle();
        });*/

    // TO_BE_REMOVED_START
    const parent = $(this).closest(".image-card");
    parent.find(".hover-buttons").toggle();
    parent
      .find(".audio")
      .addClass("appear-animation")
      .append('<audio id="audio" src="" controls></audio>')
      .toggle();
    // TO_BE_REMOVED_END
  }

  function deleteImage() {
    const url = "http://localhost:8080/images/upload";

    var parent = $(this).closest(".image-box");
    var key = parent.attr("key");

    console.log(key);

    parent.addClass("delete-animation");
    setTimeout(function () {
      parent.remove();
    }, 500);

    const fetchOptions = {
      method: "POST",
      body: formData,
    };

    fetch(url, fetchOptions)
      .then((response) => response.json())
      .then((data) => {
        var fileName = { fileName: file.name };

        let alertTemplate = $("#upload-alert-template").html();
        $(".header").prepend(Mustache.render(alertTemplate, fileName));

        let template = $("#image-card-template").html();
        $(".row").prepend(Mustache.render(template, data));
      });
    // TODO: Call POST for delete
    // fetch("urlToApiGateway", function (data) {
    //   var parent = $(this).closest(".image-box");
    //   parent.addClass("delete-animation");
    //   setTimeout(function () {
    //     parent.remove();
    //   }, 500);
    // });
  }

  function bindExpandedImage() {
    const parent = $(this).closest("img");
    $("#expanded-image").attr("src", parent.attr("src"));
  }

  function hideAppearingElements() {
    $(".description").hide();
    $(".audio").empty().hide();
    $(".hover-buttons").show();
  }

  // Show hover buttons for mobile devices
  function bindForMobileDevices() {
    if ($(window).width() < 992) {
      $(".hover-buttons").css("opacity", "1");
      $(".description, .audio").on("click", hideAppearingElements);
    }
  }

  function nextPage(url) {
    location.href = url;
  }

  function get(url, callback) {
    fetch(url, {
      method: "GET",
      headers: {
        Accept: "application/json, text/plain",
      },
    })
      .then((response) => response.json())
      .then((data) => callback(data))
      .catch((error) => {
        console.error("Error GET:", error);
      });
  }
});
