// Copyright (c) 2017, arthurdeschamps. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.

import 'dart:async';
import 'dart:html';
import 'package:angular2/angular2.dart';
import 'package:chartjs/chartjs.dart';
import 'package:webapp_angular/src/data_services/company/ChartData.service.dart';

// AngularDart info: https://webdev.dartlang.org/angular

@Component(
  selector: 'data-chart',
  styleUrls: const ["styles/dataChart.component.css"],
  templateUrl: 'templates/dataChart.component.html',
  directives: const [CORE_DIRECTIVES]
)
class DataChartComponent implements AfterViewInit {

  final ChartDataService _chartDataService;
  Chart _companyDataChart;

  DataChartComponent(this._chartDataService);

  @override
  ngAfterViewInit() {
    _chartDataService.initChartData().then((_) {
      _initChart();
      new Timer.periodic(new Duration(seconds: 1), (_) => _updateChart());
    });
  }

  void _initChart() {
    // Initiate the chart
    var data = new LinearChartData(
        labels: _chartDataService.time,
        datasets: _getDataSets()
    );
    var config = new ChartConfiguration(
        type: "line",
        data: data,
        options: new ChartOptions(
          animation: new ChartAnimationOptions(duration: 0),
          responsive: true,
          scales: new LogarithmicScale(
            yAxes: [new ChartYAxe(
              type: "logarithmic",
              scaleLabel: new ScaleTitleOptions(
                display: true,
                labelString: "Quantity in store"
              )
            )],
            xAxes: [new ChartXAxe(
              scaleLabel: new ScaleTitleOptions(
                display: true,
                labelString: "Time"
              )
            )]
          )
        )
    );
    _companyDataChart = new Chart(querySelector('#chart') as CanvasElement, config);
  }

  List<ChartDataSets> _getDataSets() {
    // Map of background colors for lines
    Map<String, String> colorOf = {
      "customers" : "#001f3f",
      "products" : "#FF4136",
      "productTypes" : "#B10DC9",
      "deliveries" : "#FF851B",
      "orders" : "#AAAAAA",
      "transportation" : "#DDDDDD"
    };

    List<ChartDataSets> data = new List<ChartDataSets>();
    _chartDataService.storesQuantities.forEach((name, quantities) =>
      data.add(new ChartDataSets(
          data: quantities,
          label: name,
          borderColor: colorOf[name]
      ))
    );
    return data;
  }

  void _updateChart() {
    _companyDataChart.data = new LinearChartData(
        labels: _chartDataService.time,
        datasets: _getDataSets()
    );
    _companyDataChart.update();
  }


}
