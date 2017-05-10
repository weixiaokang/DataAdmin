/*
 * Author: Weixk
 * Date: 2017-5-1
 * Description:
 *      spider/index.html
 **/

$(function () {

  "use strict";

  //Make the dashboard widgets sortable Using jquery UI
  $(".connectedSortable").sortable({
    placeholder: "sort-highlight",
    connectWith: ".connectedSortable",
    handle: ".box-header, .nav-tabs",
    forcePlaceholderSize: true,
    zIndex: 999999
  });
  $(".connectedSortable .box-header, .connectedSortable .nav-tabs-custom").css("cursor", "move");

});
function showProcess() {
  $.ajax({
    type: "GET",
    url: "/spider/process",
    success: function (response) {
      $(".progress-bar").text(response + "%");
      $(".progress-bar").attr("aria-valuenow", response);
      $(".progress-bar").attr("style", "width: " + response + "%;");
      var process = parseInt(response);
      if (process < 100) {
        setTimeout("showProcess()", 500);
      }
    }
  });
}
$("#query").click(function() {
    var journalName = $("#journalName").val();
    var journalUrl = $("#journalUrl").val().replace("&", "%26");
    $("#queryForm").html('<div class="progress"><div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div></div>');
    $.ajax({
      type: "GET",
      url: "/spider/search?name=" + journalName + "&url=" + journalUrl,
      success: function (response) {
        alert(response);
        showProcess();
      },
      error: function(xhr) {
        var response = $.parseJSON(xhr.responseText);
        alert(response.message);
      }
    });
});
