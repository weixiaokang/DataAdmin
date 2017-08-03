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
var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height");

var format = d3.format(",d");

var pack = d3.pack()
    .size([width - 2, height - 2])
    .padding(3);
d3.json("/data/keyword/100", function (error, data) {
    if (error) throw error;
    var root = d3.hierarchy({ children: data })
        .sum(function (d) { return d.number; })
        .sort(function (a, b) { return b.number - a.number; });

    pack(root);
    var node = svg.select("g")
        .selectAll("g")
        .data(root.children)
        .enter().append("g")
        .attr("transform", function (d) { return "translate(" + d.x + "," + d.y + ")"; })
        .attr("class", "node");
    node.append("circle")
        .attr("id", function (d) { return "node-" + d.data.id; })
        .attr("r", function (d) { return d.r; });
    node.append("clipPath")
        .attr("id", function (d) { return "clip-" + d.data.id; })
        .append("use")
        .attr("xlink:href", function (d) { return "#node-" + d.data.id + ""; });
    node.append("text")
        .attr("clip-path", function (d) { return "url(#clip-" + d.data.id + ")"; })
        .selectAll("tspan")
        .data(function (d) { return d.data.keyword.split(/(?=[A-Z][^A-Z])/g); })
        .enter().append("tspan")
        .attr("x", 0)
        .attr("y", function (d, i, nodes) { return 13 + (i - nodes.length / 2 - 0.5) * 10; })
        .text(function (d) { return d; });
    node.append("title")
        .text(function (d) { return "出现次数：" + "\n" + format(d.data.number); });
});

function type(d) {
    return (d.value = +d.value) ? d : null;
}

var kBar = echarts.init(document.getElementById("keyword-bar"));
  kBar.setOption({
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
    url: "/data/keyword/number/10",
    success: function(response) {
      kBar.setOption({
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