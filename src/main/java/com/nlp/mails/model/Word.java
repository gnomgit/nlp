package com.nlp.mails.model;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Word {
	
	private String w;
	private String stem;
	@Setter(AccessLevel.NONE)
	private String posTag;
	private String posUTag;
	@Setter(AccessLevel.NONE)
	private String nerTag;
	private String oerTag;
	private String depTag;
	private Double tokenProb;
	private Double nerProb;
	private int position;
	private int offset;
	private int father;
	private Set<String> prevTags;
	private Set<String> datapoints;
	private Location location;
	
	public Word (String w, String nerTag, Double tokenProb, int position, int offset) {
		this.w = w;
		this.stem = "";
		this.nerTag = nerTag; 
		this.oerTag = "";
		this.tokenProb = tokenProb;
		this.position = position;
		this.offset = offset;
		this.depTag = "";
		this.posTag = "";
		this.nerProb = 0.0;
		this.father = -1;
		this.prevTags = new LinkedHashSet<>();
		this.datapoints = new LinkedHashSet<>();
		this.location = new Location();
	}
	
	public Word () {
		this.w = "";
		this.stem = "";
		this.nerTag = "";
		this.oerTag = "";
		this.tokenProb = 0.0;
		this.position = -1;
		this.depTag = "";
		this.posTag = "";
		this.nerProb = 0.0;
		this.offset = 0;
		this.father = -1;
		this.prevTags = new LinkedHashSet<>();
		this.datapoints = new LinkedHashSet<>();
		this.location = new Location();
	}
	
	public void setPosTag (String posTag) {
		if (posTag == null) return;
		this.posTag = posTag;
		translatePOS (posTag);
	}
	
	public void setNerTag (String nerTag) {
		if (nerTag == null) return;
		if (!nerTag.isEmpty() && !nerTag.equals(Constants.O)) {
			String prev = nerTag.replace(Constants.B, "").replace(Constants.I, "");
			this.prevTags.add(prev);
		}
		this.nerTag = nerTag;
	}

	private void translatePOS (String posTag) {
		if (posTag == null || posTag.isEmpty()) return;
		if (posTag.equals(Constants.hash)
				|| posTag.equals(Constants.dollar)
				|| posTag.equals(Constants.sym)) {
			this.posUTag = Constants.usym;
			return;
		}
		if (posTag.equals(Constants.apostrophe)
				|| posTag.equals(Constants.backquote)
				|| posTag.equals(Constants.comma)
				|| posTag.equals(Constants.dot)
				|| posTag.equals(Constants.colon)
				|| posTag.equals(Constants.lrb)
				|| posTag.equals(Constants.hyph)
				|| posTag.equals(Constants.rrb)) {
			this.posUTag = Constants.upunct;
			return;
		}
		if (posTag.equals(Constants.afx)
				|| posTag.equals(Constants.jj)
				|| posTag.equals(Constants.jjr)
				|| posTag.equals(Constants.jjs)) {
			this.posUTag = Constants.uadj;
			return;
		}
		if (posTag.equals(Constants.cc)) {
			this.posUTag = Constants.ucconj;
			return;
		}
		if (posTag.equals(Constants.cd)) {
			this.posUTag = Constants.unum;
			return;
		}
		if (posTag.equals(Constants.dt)
				|| posTag.equals(Constants.pdt)
				|| posTag.equals(Constants.wdt)
				|| posTag.equals(Constants.wpdollar)) {
			this.posUTag = Constants.udet;
			return;
		}
		if (posTag.equals(Constants.ex)
				|| posTag.equals(Constants.prp)
				|| posTag.equals(Constants.wp)) {
			this.posUTag = Constants.upron;
			return;
		}
		if (posTag.equals(Constants.in)
				|| posTag.equals(Constants.rp)) {
			this.posUTag = Constants.uadp;
			return;
		}
		if (posTag.equals(Constants.md)
				|| posTag.equals(Constants.vb)
				|| posTag.equals(Constants.vbd)
				|| posTag.equals(Constants.vbg)
				|| posTag.equals(Constants.vbn)
				|| posTag.equals(Constants.vbp)
				|| posTag.equals(Constants.vbz)) {
			this.posUTag = Constants.uverb;
			return;
		}
		if (posTag.equals(Constants.nn)
				|| posTag.equals(Constants.nns)) {
			this.posUTag = Constants.unoun;
			return;
		}
		if (posTag.equals(Constants.nnp)
				|| posTag.equals(Constants.nnps)) {
			this.posUTag = Constants.upropn;
			return;
		}
		if (posTag.equals(Constants.pos)
				|| posTag.equals(Constants.to)) {
			this.posUTag = Constants.upart;
			return;
		}
		if (posTag.equals(Constants.rb)
				|| posTag.equals(Constants.rbr)
				|| posTag.equals(Constants.rbs)
				|| posTag.equals(Constants.wrb)) {
			this.posUTag = Constants.uadv;
			return;
		}
		if (posTag.equals(Constants.uh)) {
			this.posUTag = Constants.uintj;
			return;
		}
		if (posTag.equals(Constants.fw)
				|| posTag.equals(Constants.ls)
				|| posTag.equals(Constants.nil)) {
			this.posUTag = Constants.ux;
		}	else {
			this.posUTag = "";
		}
	}
	
	public void updateLocation (int pos, int line, int npage, int pageW, int pageH) {
		this.location.setX(pos - 3);
		this.location.setY(line - 3);
		this.location.setWidth(this.w.length() * Constants.NHINC + 6);
		this.location.setHeight(Constants.NVINC);
		this.location.setPage(npage);
		this.location.setPageH(pageH);
		this.location.setPageW(pageW);
	}
}


