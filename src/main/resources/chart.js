var options = {
  chart: {
    zoomType: 'x'
  },
  title: {
    text: '$title'
  },
  xAxis: {
    type: 'datetime'
  },
  yAxis: {
  	title: {
    	text: null
    }
  },
  legend: {
    enabled: false
  },
  credits: {
    enabled: false
  },
  exporting: {
    enabled: false
  },
  plotOptions: {
    series: {
      animation: false
    }
  },
  series: [{
    type: 'line',
    name: '$title',
    data: $data
  }]
};