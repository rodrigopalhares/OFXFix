package palhares.util.fix.ofx;

public class OfxTags {
    public static final String TRN_STMTTRN = "STMTTRN";
    public static final String TRN_TRNTYPE = "TRNTYPE";
    public static final String TRN_DTPOSTED = "DTPOSTED";
    public static final String TRN_TRNAMT = "TRNAMT";
    public static final String TRN_FITID = "FITID";
    public static final String TRN_CHECKNUM = "CHECKNUM";
    public static final String TRN_NAME = "NAME";
    public static final String TRN_MEMO = "MEMO";
    public static final String[] ORDER = new String[]{OfxTags.TRN_TRNTYPE, OfxTags.TRN_DTPOSTED, OfxTags.TRN_TRNAMT, OfxTags.TRN_FITID, OfxTags.TRN_CHECKNUM, OfxTags.TRN_NAME, OfxTags.TRN_MEMO};
}
