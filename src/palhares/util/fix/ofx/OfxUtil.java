package palhares.util.fix.ofx;

public final class OfxUtil {

    public static final String getOpenTag(String tag) {
        return String.format("<%s>", tag);
    }

    public static final String getCloseTag(String tag) {
        return String.format("</%s>", tag);
    }


}
