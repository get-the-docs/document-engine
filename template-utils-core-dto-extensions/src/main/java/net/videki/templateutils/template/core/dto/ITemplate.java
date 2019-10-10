package net.videki.templateutils.template.core.dto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ITemplate {
  String PLACEHOLDER_EMPTY = "................";
  DateTimeFormatter DATE_FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");

  Logger           LOG             = LoggerFactory.getLogger(ITemplate.class);

  default String fmtN(final Integer value) {
    try {
      final DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("hu", "HU"));
      dfs.setDecimalSeparator(',');
      dfs.setGroupingSeparator(' ');

      final DecimalFormat fmt = new DecimalFormat("###,###,##0", dfs);
      return fmt.format(value);
    } catch (final IllegalArgumentException e) {
      LOG.warn("fmtN format error: {}", value, e);

      return PLACEHOLDER_EMPTY;
    }

  }

  default String fmtNxM(final Double value, final int m) {
    try {
      final DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("hu", "HU"));
      dfs.setDecimalSeparator(',');
      dfs.setGroupingSeparator(' ');

      final DecimalFormat fmt = new DecimalFormat("###,###,##0." + new String(new char[m]).replace('\0', '0'), dfs);

      return fmt.format(value);
    } catch (final IllegalArgumentException e) {
      LOG.warn("fmtNxM format error: {}", value, e);

      return "";
    }
  }

  default String fmtNxMOpc(final Double value, final int m) {

    String result;

    if (value != null) {
      result = fmtNxM(value, m);
    } else {
      result = "";
    }

    return result;
  }

  default String fmtN(final String value) {
    return value;
  }

  default String fmtNxM(final String value) {
    return value;
  }

  default String fmtNxMOpc(final String value) {

    String result;

    if (value != null) {
      result = fmtNxM(value);
    } else {
      result = "";
    }

    return result;
  }

  default String fmtDate(final LocalDate value) {
    String result;
    if (value != null) {
      result = value.format(DATE_FORMAT_DATE);
    } else {
      result = PLACEHOLDER_EMPTY;
    }
    return result;
  }

  default String optional(final String value) {
    return value != null ? value : "";
  }

  default <T> List<List<T>> collateToColumns(final List<T> items, final int colCount) {
    final List<List<T>> results = new LinkedList<>();

    if (colCount > 1 && items != null && !items.isEmpty()) {

      List<T> actRow = new ArrayList<>(colCount);

      for (int i = 0; i < items.size(); i++) {

        if ((i % colCount) == 0) {
          actRow = new ArrayList<>(colCount);
          results.add(actRow);
        }

        actRow.add(items.get(i));

      }

    }

    return results;
  }

}
