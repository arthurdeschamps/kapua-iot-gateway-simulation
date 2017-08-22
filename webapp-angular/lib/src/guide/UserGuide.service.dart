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

import 'package:angular2/angular2.dart';
import 'package:js/js.dart';
import 'package:webapp_angular/src/guide/shepherd.interop.dart';
import 'dart:html';

@Injectable()
class UserGuideService {

  Tour _guide;

  /**
   * Builds the tour.
   */
  void _build() {

    _guide = new Tour(new TourOptions(
        defaults: new StepOptions(
          classes: 'shepherd-theme-dark',
          scrollTo: true,
          showCancelLink: true
        )
    ));

    Shepherd.on('start', allowInterop((_) {
      querySelectorAll("app *").style.opacity = '0.9';
    }));

    Function normalOpacity = allowInterop((_) {
      querySelectorAll("app *").style.opacity = '1';
    });

    Shepherd.on('cancel', normalOpacity);
    Shepherd.on('complete', normalOpacity);

    _buildSteps(_guide);

  }

  /**
   * Adds every step to the tour.
   */
  void _buildSteps(Tour guide) {
    final List<Button> buttons = <Button>[
      new Button(
          text: 'Previous',
          classes: 'shepherd-button-secondary',
          action: allowInterop((_) {
            return guide.back();
          })
      ),
      new Button(
          text: 'Next',
          classes: 'shepherd-button-primary',
          action: allowInterop((_) {
            return guide.next();
          })
      )
    ];

    guide.addStep('map-section',
        new StepOptions(
            title: 'Sections / Map',
            text: 'Here is the first menu of the navigation bar: Map. In this '+
                'section, you can see real time telemetry data about your company on '
                +'a map. Click on any marker to discover more information about it.',
            attachTo: '#map-section-tab right',
            buttons: buttons
        )
    );

    guide.addStep('map-element',
        new StepOptions(
            title: 'Map / map',
            text: 'This map is an entertaining way of seing you deliveries moving around '+
                'the world. As they get transported to their destination point, you can clik on them '+
                'to see more information about the delivery and its transporter.',
            attachTo: '#map center',
            advanceOn: '.docs-link click',
            when: new WhenOptions(
                show: allowInterop(() {
                  // Clicks on a random marker of the map
                  ElementList<HtmlElement> markers = querySelectorAll('#map .fa');
                  if (markers.length > 0)
                    markers.last.parent.click();
                }),
                hide: allowInterop(() =>
                    (querySelector("#company-data-section-tab li") as HtmlElement).click()
                )
            ),
            buttons: buttons
        )
    );

    guide.addStep('company-data-section',
        new StepOptions(
            title: 'Sections / Company data',
            text: 'The company data section allows you to see the evolution '+
                'of your company\'s metrics in real time on a chart.',
            attachTo: '#company-data-section-tab right',
            advanceOn: '.docs-link click',
            buttons: buttons
        )
    );
  }

  /**
   * Starts the step by step user tour.
   */
  void start() {
    _build();
    _guide.start();
  }
}
