package palhares.ofxfix.core

import static OfxTags.*;

/**
 * Bean com as informações da transação.
 * @author Palhares
<pre>
&lt;STMTTRN&gt;
	&lt;TRNTYPE&gt;CREDIT&lt;/TRNTYPE&gt;
	&lt;DTPOSTED&gt;20100401120000[-3:BRT]&lt;/DTPOSTED&gt;
	&lt;TRNAMT&gt;0.24&lt;/TRNAMT&gt;
	&lt;FITID&gt;0&lt;/FITID&gt;
	&lt;CHECKNUM&gt;0&lt;/CHECKNUM&gt;
	&lt;MEMO&gt;REM BASICA&lt;/MEMO&gt;
	&lt;NAME&gt;MAESTRO LOCAL 438&lt;/NAME&gt;
&lt;/STMTTRN&gt;
</pre>
 *
 */
class Stmttrn {
	/** Representa o elemento &lt;TRNTYPE&gt;. */
	String trntype;
	/** Representa o elemento &lt;DTPOSTED&gt;. */
	String dtposted;
	/** Representa o elemento &lt;TRNAMT&gt;. */
	Double trnamt;
	/** Representa o elemento &lt;FITID&gt;. */
	String fitid;
	/** Representa o elemento &lt;CHECKNUM&gt;. */
	String checknum;
	/** Representa o elemento &lt;NAME&gt;. */
	String name;
	/** Representa o elemento &lt;MEMO&gt;. */
	String memo;
	/** Informa se o elemento deve ser deletado. */
	Boolean deleted = false;
	
	public Stmttrn() {
	}

	public Stmttrn(String elem) {
		elem.eachLine {
			it = it.replaceAll("</.*>", "")
			int openTagStartIdx = it.indexOf("<");
			if (openTagStartIdx >= 0 && !it.contains(TRN_STMTTRN)) {
				int openTagEndIdx = it.indexOf(">", openTagStartIdx);
				String tag = it.substring(openTagStartIdx + 1, openTagEndIdx);
				String value = it.substring(openTagEndIdx + 1);
				this.set(tag, value)
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb << "<$TRN_STMTTRN>"
		if(this.trntype != null) {
			sb << "\r\n\t<${TRN_TRNTYPE}>$trntype</${TRN_TRNTYPE}>"
		}
		if(this.dtposted != null) {
			sb << "\r\n\t<${TRN_DTPOSTED}>$dtposted</${TRN_DTPOSTED}>"
		}
		if(this.trnamt != null) {
			sb << "\r\n\t<${TRN_TRNAMT}>$trnamt</${TRN_TRNAMT}>"
		}
		if(this.fitid != null) {
			sb << "\r\n\t<${TRN_FITID}>$fitid</${TRN_FITID}>"
		}
		if(this.checknum != null) {
			sb << "\r\n\t<${TRN_CHECKNUM}>$checknum</${TRN_CHECKNUM}>"
		}
		if(this.name != null) {
			sb << "\r\n\t<${TRN_NAME}>$name</${TRN_NAME}>"
		}
		if(this.memo != null) {
			sb << "\r\n\t<${TRN_MEMO}>$memo</${TRN_MEMO}>"
		}
		sb << "\r\n</$TRN_STMTTRN>"
		return sb.toString();
	}

	@Override
	public boolean equals(item2) {
		return this.trntype == item2.trntype &&
			this.dtposted == item2.dtposted &&
			this.trnamt == item2.trnamt &&
			this.fitid == item2.fitid &&
			this.checknum == item2.checknum &&
			this.name == item2.name &&
			this.memo == item2.memo &&
			this.deleted == item2.deleted
	}

	private set(String tag, String value) {
		switch (tag) {
			case TRN_TRNTYPE:
				this.trntype = value;
				break;
			case TRN_DTPOSTED:
				this.dtposted = value;
				break;
			case TRN_TRNAMT:
		        try {
		            trnamt = value.toDouble();
		        } catch (NumberFormatException e) {
		            trnamt = value.replaceAll(/\\./, "").replaceAll(/,/, '.').toDouble();
		        }
				break;
			case TRN_FITID:
				this.fitid = value;
				break;
			case TRN_CHECKNUM:
				this.checknum = value;
				break;
			case TRN_NAME:
				this.name = value;
				break;
			case TRN_MEMO:
				this.memo = value;
				break;
		}
	}
}
