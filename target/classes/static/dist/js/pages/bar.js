var kBar = echarts.init(document.getElementById("keyword-bar"));
kBar.setOption({
    tooltip: {},
    legend: {
        data: ["关键字个数"]
    },
    xAxis: {
        type: 'value',
        data: []
    },
    yAxis: {
        type: 'category',
        data: []
    },
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
    url: "/data/keyword/number/50",
    success: function (response) {
        kBar.setOption({
            yAxis: {
                data: response.x.reverse()
            },
            series: [{
                name: "关键字个数",
                type: 'bar',
                data: response.data.reverse()
            }]
        })
    }
});