var atLine = echarts.init(document.getElementById("article-line"));
atLine.setOption({
    tooltip: {},
    legend: {
        textStyle: {
            color: "#fff"
        },
        data: ["论文数量"]
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
    success: function (response) {
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
    tooltip: {},
    legend: {
        textStyle: {
            color: "#fff"
        },
        data: ["关键字数量"]
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
    success: function (response) {
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
});