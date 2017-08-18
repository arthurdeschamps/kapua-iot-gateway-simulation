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

import 'dart:html';
import 'package:angular2/angular2.dart';
import 'package:webapp_angular/src/data_services/app/company/Address.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart' as Utils;
import 'package:webapp_angular/src/pipes/FirstLetterUppercase.pipe.dart';
import 'package:webapp_angular/src/pipes/SplitUppercases.dart';
import 'package:webapp_angular/src/sections/map/utils/Field.dart';

@Injectable()
class InformationPanelService {

  final FirstLetterUppercase _firstLetterUppercaseTransform = new FirstLetterUppercase();
  final SplitUppercases _splitUppercasesTransform = new SplitUppercases();

  /// Content of the panel where information on any clicked element of the map is displayed.
  List<Field> informationPanelContent = null;
  /// All the defined fields.
  static const List<String> knownFields = const ["what", "location", "fullAddress"];

  /// Sets the inner html of the information panel.
  ///
  /// [information] is a map containing all the information that must be displayed.
  /// Map's keys are either already defined fields (such as what or location) or
  /// unknown fields. In the latter case, field names will have to use the lowerCamelCase
  /// format in order to display correctly.
  void setInformationPanel(final List<String> fields, final List<dynamic> values) {
    final Map<String, dynamic> information = _buildMap(fields, values);
    // Will contain all the given fields as well formatted strings.
    List<Field> informationPanelContent = new List();

    /// Parses a pair of the map [information].
    ///
    /// [onValue] is used to transform the value when the key is present in the map.
    /// [onAbsent] is a fallback if the key is not present in the map.
    void addField(final String key, final String fieldName, String onValue(dynamic value), String onAbsent) {
      String value;
      if (information.containsKey(key)) {
        try {
          value = onValue(information[key]);
        } catch (e) {
          print(e);
          value = "";
        }
      } else {
        value = onAbsent;
      }
      informationPanelContent.add(new Field(fieldName, value));
    }

    // Handles known fields.
    addField("what", "What", (val) => val, "?");
    addField("location", "Coordinates", (val) => (val as Utils.Coordinates).toString(), "Unknown");
    addField("fullAddress", "Address", (val) => (val as Address).toString(), "Unknown");
    // Handles unknown fields.
    String parseKey(String key) => _splitUppercasesTransform.transform(_firstLetterUppercaseTransform.transform(key));
    information.forEach((key, val) {
      if (!knownFields.contains(key))
        addField(key, parseKey(key), (val) => val, val);
    });

    // Updates instance list. This is done only now because every time this.informationPanelContent
    // is updated, the UI is also updated.
    this.informationPanelContent = informationPanelContent;
  }

  Map<String, dynamic> _buildMap(final List<String> keys, final List<dynamic> values) =>
      new Map.fromIterables(keys, values);

  void show() => (querySelector("#map-information") as HtmlElement).classes.remove("fade-out");
  void hide() => (querySelector("#map-information") as HtmlElement).classes.add("fade-out");
}
