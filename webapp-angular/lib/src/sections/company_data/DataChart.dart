/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

import 'dart:async';
import 'dart:html';
import 'package:angular2/angular2.dart';
import 'package:chartjs/chartjs.dart';
import 'package:webapp_angular/src/data_services/app/company/ChartData.service.dart';

/// A Chart component displaying the evolution of the stores sizes.
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

  /// Initializes the chart.
  void _initChart() {
    // Initiate the chart
    var data = new LinearChartData(
        labels: _chartDataService.timeline,
        datasets: _getDataSets()
    );
    var config = new ChartConfiguration(
        type: "line",
        data: data,
        options: new ChartOptions(
          title: new ChartTitleOptions(
            display: true,
            text: "Evolution of the company over time",
            fontColor: "#e6e8ed",
            fontSize: 16,
            fontStyle: "normal"
          ),
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

  /// Returns all the data the shall be displayed in the chart.
  ///
  /// Time against store size will be displayed, for each defined store (orders,
  /// customers, etc).
  List<ChartDataSets> _getDataSets() {
    // Map of border colors for the lines
    Map<String, String> colorOf = {
      "customers" : "#7b93d1",
      "products" : "#FF4136",
      "productTypes" : "#B10DC9",
      "deliveries" : "#FF851B",
      "orders" : "#AAAAAA",
      "transportation" : "#DDDDDD"
    };

    List<ChartDataSets> data = new List<ChartDataSets>();
    _chartDataService.storesQuantities.forEach((name, quantities) {
      String labelName = name.replaceRange(0,1,name.substring(0,1).toUpperCase());
      if (labelName == "ProductTypes")
        labelName = "Product Types";
      data.add(new ChartDataSets(
          data: quantities,
          label: labelName,
          borderColor: colorOf[name]
      ));
    }
    );
    return data;
  }

  /// Updates the chart data.
  void _updateChart() {
    _companyDataChart.data = new LinearChartData(
        labels: _chartDataService.timeline,
        datasets: _getDataSets()
    );
    _companyDataChart.update();
  }


}
