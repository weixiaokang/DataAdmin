/*
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This is a demo file used only for the main dashboard (index.html)
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

  $.get("/data/keyword/size", function(data, status) {
    if (status == "success") {
      $("#keyword").text(data);
    } else {
      $("#keyword").text("0");
    }
  });
  $.get("/data/article/size", function(data, status) {
    if (status == "success") {
      $("#article").text(data);
    } else {
      $("#article").text("0");
    }
  });
  $.get("/data/journal/size", function(data, status) {
    if (status == "success") {
      $("#journal").text(data);
    } else {
      $("#journal").text("0");
    }
  });

  var jkBar = echarts.init(document.getElementById("journal-keyword-bar"));
  jkBar.setOption({
    tooltip:{},
    legend: {
      data:["关键字个数"]
    },
    xAxis: {
        data: []
    },
    yAxis: {},
    series: [{
      name: "关键字个数",
      type: "bar",
      data: [],
      itemStyle: {
        normal: {
          color: '#39cccc'
        }
      }
    }]
  });
  $.ajax({
    type: "GET",
    url: "/data/journal/keyword/number",
    success: function(response) {
      jkBar.setOption({
        xAxis: {
          data: response.x
        },
        series: [{
          name: "关键字个数",
          data: response.data
        }]
      })
    }
  });
  var atLine = echarts.init(document.getElementById("article-line"));
  atLine.setOption({
    tooltip:{},
    legend: {
      textStyle: {
            color: "#fff"
      },
      data:["论文数量"]
    },
    xAxis: {
        type: "category",
        boundaryGap: false,
        axisLine: {
            lineStyle: {
                color: "#fff"
            }
        },
        data: []
    },
    yAxis: {
      axisLine: {
            lineStyle: {
                color: "#fff"
            }
        },
        splitLine: {
            lineStyle: {
                color: "#fff"
            }
        }
    },
    series: [{
      name: "论文数量",
      type: "line",
      data: [],
      itemStyle: {
        normal: {
          color: '#fff'
        }
      }
    }]
  });
  $.ajax({
    type: "GET",
    url: "/data/article/time",
    success: function(response) {
      atLine.setOption({
        xAxis: {
          data: response.x
        },
        series: [{
          name: "论文数量",
          data: response.data
        }]
      })
    }
  });
  var ktLine = echarts.init(document.getElementById("keyword-line"));
  ktLine.setOption({
    tooltip:{},
    legend: {
      textStyle: {
            color: "#fff"
      },
      data:["关键字数量"]
    },
    xAxis: {
        type: "category",
        boundaryGap: false,
        axisLine: {
            lineStyle: {
                color: "#fff"
            }
        },
        data: []
    },
    yAxis: {
      axisLine: {
            lineStyle: {
                color: "#fff"
            }
        },
        splitLine: {
            lineStyle: {
                color: "#fff"
            }
        }
    },
    series: [{
      name: "关键字数量",
      type: "line",
      data: [],
      itemStyle: {
        normal: {
          color: '#fff'
        }
      }
    }]
  });
  $.ajax({
    type: "GET",
    url: "/data/keyword/time",
    success: function(response) {
      ktLine.setOption({
        xAxis: {
          data: response.x
        },
        series: [{
          name: "关键字数量",
          data: response.data
        }]
      })
    }
  })
});

