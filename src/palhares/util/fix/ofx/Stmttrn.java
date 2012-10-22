package palhares.util.fix.ofx;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import palhares.util.common.StringTools;

/**
 * Bean com as informações da transação.
 * @author Palhares

<STMTTRN>
    <TRNTYPE>CREDIT</TRNTYPE>
    <DTPOSTED>20100401120000[-3:BRT]</DTPOSTED>
    <TRNAMT>0.24</TRNAMT>
    <FITID>0</FITID>
    <CHECKNUM>0</CHECKNUM>
    <MEMO>REM BASICA</MEMO>
    <NAME>MAESTRO LOCAL 438</NAME>
</STMTTRN>
 *
 */
public class Stmttrn {

    private final Map<String, String> values = new LinkedHashMap<String, String>();

    public Stmttrn(String elem) {
        String[] splitted = elem.split("\n");
        for (String item : splitted) {
            int openTagStartIdx = item.indexOf("<");
            if (openTagStartIdx >= 0 && !item.contains(OfxTags.TRN_STMTTRN)) {
                int openTagEndIdx = item.indexOf(">", openTagStartIdx);
                String tag = item.substring(openTagStartIdx + 1, openTagEndIdx);
                int closeTagStartIdx = item.indexOf("</");
                if (closeTagStartIdx < 0) {
                    closeTagStartIdx = item.length();
                }
                String value = item.substring(openTagEndIdx + 1, closeTagStartIdx);
                this.values.put(tag.trim().toUpperCase(), value.trim());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(OfxUtil.getOpenTag(OfxTags.TRN_STMTTRN));
        sb.append("\r\n");
        for (String tag : OfxTags.ORDER) {
            if (this.values.containsKey(tag)) {
                sb.append("\t")
                .append(OfxUtil.getOpenTag(tag))
                .append(this.values.get(tag))
                .append(OfxUtil.getCloseTag(tag))
                .append("\r\n");
            }
        }
        sb.append(OfxUtil.getCloseTag(OfxTags.TRN_STMTTRN));

        return sb.toString();
    }

    public Map<String, String> getValues() {
        return this.values;
    }

    public String get(String tag) {
        return this.values.get(tag);
    }

    public void set(String tag, String value) {
        this.values.put(tag, value);
    }

    /**
     * Repara o registro.
     * @return <code>false</code> se o arquivo não puder ser reparado
     */
    public boolean repair() {
        String name = this.values.get(OfxTags.TRN_NAME);
        String memo = this.values.get(OfxTags.TRN_MEMO);
        if ("SALDO ANTERIOR".equals(name) || "RECURSOS EM C/C".equals(name) || "SALDO FINAL".equals(name) || "bx Aut Poupanca".equals(memo)) {
            return false;
        }

        if ("REM BASICA".equals(memo) || "CRED JUROS".equals(memo)) {
            this.values.put(OfxTags.TRN_NAME, "Juros");
        }

        Double trnamt;
        try {
            trnamt = Double.valueOf(this.values.get(OfxTags.TRN_TRNAMT));
        } catch (NumberFormatException e) {
            trnamt = Double.valueOf(this.values.get(OfxTags.TRN_TRNAMT).replace(".", "").replace(',', '.'));
        }
        if (trnamt.equals(0.0)) {
            return false;
        }

        String fitid = this.values.get(OfxTags.TRN_FITID);
        if (StringTools.isEmpty(fitid) || "0".equals(fitid.trim())) {
            DecimalFormat df = new DecimalFormat("0000");
            df.setMaximumIntegerDigits(4);
            String date = this.values.get(OfxTags.TRN_DTPOSTED).substring(0, 8);
            String value = df.format(this.values.get(OfxTags.TRN_TRNAMT).hashCode());
            this.values.put(OfxTags.TRN_FITID, date + value);
        }

        return true;
    }
}
