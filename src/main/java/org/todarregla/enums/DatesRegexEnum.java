package org.todarregla.enums;

public enum DatesRegexEnum {

    //TODO: Generate Regex patterns with locale month names

    SIMPLE_DAY_MONTH_YEAR_HYPHEN("\\d{2}-\\d{2}-\\d{4}"),
    SIMPLE_DAY_MONTH_YEAR_DASH("\\d{2}[/]{1}\\d{2}[/]{1}\\d{4}"),
    SIMPLE_YEAR_DAY_MONTH_HYPHEN("\\d{4}-\\d{2}-\\d{2}"),
    SIMPLE_YEAR_DAY_MONTH_DASH("\\d{4}[/]{1}\\d{2}[/]{1}\\d{2}"),

    FULL_MONTH_NAME_DAY_MONTH_YEAR_HYPHEN_UPPERCASE("\\d{1,2}-(Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre)-\\d{4}"),
    FULL_MONTH_NAME_DAY_MONTH_YEAR_SPACE_UPPERCASE("\\d{1,2}\\s(Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre)\\s\\d{4}"),


    FULL_MONTH_NAME_DAY_MONTH_YEAR_HYPHEN_LOWERCASE("\\d{1,2}-(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|octubre|noviembre|diciembre)-\\d{4}"),
    FULL_MONTH_NAME_DAY_MONTH_YEAR_SPACE_LOWERCASE("\\d{1,2}\\s(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|octubre|noviembre|diciembre)\\s\\d{4}"),

    SHORT_MONTH_NAME_DAY_MONTH_YEAR_HYPHEN("\\d{1,2}-(Jan|Feb|Mar|Apr|May|June|July|Aug|Sept|Oct|Nov|Dec)-\\d{4}"),
    SHORT_MONTH_NAME_DAY_MONTH_YEAR_SPACE("\\d{1,2}\\s(Jan|Feb|Mar|Apr|May|June|July|Aug|Sept|Oct|Nov|Dec)\\s\\d{4}");


    DatesRegexEnum(String pattern){
        this.pattern = pattern;
    }

    private final String pattern;

    public String getPattern() {
        return pattern;
    }
}
