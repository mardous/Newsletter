package ve.msucre.noticias.util;

/**
 * @author Christians Martínez Alvarado
 */
public enum Hour {
    ONE(3600000),
    TWO(7200000),
    FIVE(18000000),
    EIGHT(28800000),
    TWELVE(43200000),
    TWENTY_FOUR(86400000);

    public long hourInMillis;

    Hour(long hourInMillis) {
        this.hourInMillis = hourInMillis;
    }
}
