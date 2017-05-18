var jkPie = echarts.init(document.getElementById("keyword-pie"));
jkPie.setOption({
    tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: []
    },
    series : [
        {
            name: '期刊关键字比例',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
});
$.ajax({
    type: "GET",
    url: "/data/journal/keyword/per",
    success: function (response) {
        legendList = [];
        for (var i = 0; i < response.length; i++) {
            legendList.push(response[i].name);
        }
        jkPie.setOption({
            legend: {
                data: legendList
            },
            series: [{
                data: response
            }]
        })
    }
});