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

$("#table").bootstrapTable({
    mothod: "get",
    url: "/data/journal"
});
setTimeout(function() {
    $("#table").bootstrapTable("resetView");
}, 200);
$("table").on('check.bs.table uncheck.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
    $("#remove").prop('disabled', !$("#table").bootstrapTable('getSelections').length);

    // save your data, here just save the current page
    
    // push or splice the selections if you want to save all data selections
});
$("#table").on('all.bs.table', function (e, name, args) {
    console.log(name, args);
});
function getIdSelections() {
    return $.map($("#table").bootstrapTable('getSelections'), function (row) {
        return row.name;
    });
}
$("#remove").click(function() {
    var ids = getIdSelections();
    $("#table").bootstrapTable("remove", {
        field: "name",
        values: ids
    });
    $("#remove").prop('disabled', true);
});
function getHeight() {
    return $(window).height();
}
$(window).resize(function () {
    $("#table").bootstrapTable('resetView', {
        height: getHeight()
    });
});
function urlFormatter(value, row, index) {
        return ['<a href="' + value + '" target="_blank">' + value + '</a>'].join('');
}