package palhares.util.fix.ofx;

public enum StmtEnum {
    TRN_STMTTRN("STMTTRN"),
    TRN_TRNTYPE("TRNTYPE"),
    TRN_DTPOSTED("DTPOSTED"),
    TRN_TRNAMT("TRNAMT"),
    TRN_FITID("FITID"),
    TRN_CHECKNUM("CHECKNUM"),
    TRN_MEMO("MEMO"),
    TRN_NAME("NAME");

    private String tag;

    private StmtEnum(String tag) {
        this.tag = tag;
    }

    public String getOpenTag() {
        return String.format("<%s>", this.tag);
    }

    public String getCloseTag() {
        return String.format("</%s>", this.tag);
    }
}
