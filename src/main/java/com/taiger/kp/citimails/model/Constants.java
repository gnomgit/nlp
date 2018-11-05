package com.taiger.kp.citimails.model;

import lombok.Getter;
import opennlp.tools.util.Span;

public class Constants {

	// BIO tags prefixes
	public static final String B = "B-";
	public static final String I = "I-";
	public static final String O = "O";
	public static final String AT = "@";
	
	// ner tags
	public static final String LOCATION = "LOCATION";
	public static final String PERSON = "PERSON";
	public static final String ORGANIZATION = "ORGANIZATION";
	public static final String DATE = "DATE";
	public static final String TIME = "TIME";
	public static final String MONEY = "MONEY";
	public static final String PERCENTAGE = "PERCENTAGE";
	public static final String AMOUNT = "AMOUNT";
	public static final String CURRENCY = "CURRENCY";
	public static final String ISIN = "ISIN";
	public static final String CUSIP = "CUSIP";
	
	// opennlp models
	public static final String NER_ES_DATE = "es-ner-misc.bin";
	public static final String NER_EN_DATE = "en-ner-date.bin";
	public static final String NER_ES_LOCATION = "es-ner-location.bin";
	public static final String NER_EN_LOCATION = "en-ner-location.bin";
	public static final String NER_EN_PERSON = "en-ner-person.bin";
	public static final String NER_EN_ORGANIZATION = "en-ner-organization.bin";
	public static final String NER_EN_TIME = "en-ner-time.bin";
	public static final String NER_EN_MONEY = "en-ner-money.bin";
	public static final String NER_EN_PERCENTAGE = "en-ner-percentage.bin";
	public static final String TOKEN_EN = "en-token.bin";
	public static final String TAGGER_EN = "en-pos-maxent.bin";
	public static final String TAGGER_PER_EN = "en-pos-perceptron.bin";
	public static final String SENTENCE_EN = "en-sent.bin";
	public static final String CHUNKER_EN = "en-chunker.bin";
	public static final String PARSER_EN = "en-parser-chunking.bin";
	public static final String MALTPARSER_ARGS = "-c espmalt-1.0 -m parse -w ./models -lfi parser.log";
	
	// ontologies
	public static final String CITI_ONTOLOGY_TTL = "CitibankOnto.ttl";   
	public static final String CITI_ONTOLOGY_TTL_new = "CitiBankOnto_1031.ttl";
	public static final String LEXIS_ONTOLOGY_TTL = "lexis.ttl";
	public static final String CITI_ONTOLOGY_OBDA = "CitibankOnto.obda";
	public static final String CITI_ONTOLOGY_JSON = "CitibankOnto1.obda";
	public static final String CITI_ONTOLOGY_OWL = "citibankNew.owl";
	public static final String CITI_ONTOLOGY_OWL_old = "CitibankOnto3.owl";
	public static final String BIRRA_ONTOLOGY_OWL = "birra.owl";
	
	// dbpedia
	public static final String DBPEDIA_EN_URL = "https://api.dbpedia-spotlight.org/en/annotate?";
	public static final String DBPEDIA_SNORQL = "http://dbpedia.org/sparql";
	public static final String DBPEDIA_ES_URL = "https://api.dbpedia-spotlight.org/es/annotate?";
	public static final String TEXT_PARAM = "text=";
	public static final String TYPES_PARAM = "types=";
	
	public static final int MAXINT = 999999;
	
	@Getter
	public static enum RESOURCE {
		LAT ("http://www.w3.org/2003/01/geo/wgs84_pos#lat"),
		LATITUDE ("http://dbpedia.org/property/latitude"),
		LONG ("http://www.w3.org/2003/01/geo/wgs84_pos#long"),
		LONGITUDE ("http://dbpedia.org/property/longitude"),
		GEOPOINT ("http://www.georss.org/georss/point");
		
		private final String link;
		
		private RESOURCE (String link) {
			this.link = link;
		}
	}
	
	public static String formChunk (String [] tokens, Span sp) {
		StringBuilder chunk = new StringBuilder();
		
		for (int i = sp.getStart(); i < sp.getEnd(); i++) {
			chunk.append(tokens[i] + " ");
		}
		
		return chunk.toString().trim();
	}
	
	// date regexp
	public static final String yyyy_b = "(2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))";
	public static final String yyyy = "((19|2[0-9])[0-9]{2})";
	public static final String yy = "([0-9][0-9])";
	public static final String MM_b = "(2|02|(F|f)eb)";
	public static final String MM_31 = "((0[13578]|10|12)|([13578]|10|12)|(J|j)(an|ul)|(M|m)(ar|ay)|(A|a)ug|(O|o)ct|(D|d)ec)";
	public static final String MM_30 = "((0[469]|11)|([469]|11)|(A|a)pr|(J|j)un|(S|s)ep(t)?|(N|n)ov)";
	public static final String dd_b = "29";
	public static final String dd_nb = "(0[1-9]|1[0-9]|2[0-8]|[1-9])";
	public static final String dd_31 = "(0[1-9]|[12][0-9]|3[01]|[1-9])";
	public static final String dd_30 = "(0[1-9]|[12][0-9]|30|[1-9])";
	public static final String date_sep = "(-|/)";
	public static final String days = "^((mon|tues|wed(nes)?|thur(s)?|fri|sat(ur)?|sun)(day)?)$";
	
	// money regexp
	//public static final String amount = "^(\\$|\\€|\\£|\\¥)?(-)?(([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+)(\\.[0-9][0-9]))?$";
	public static final String amount = "^(\\$|\\€|\\£|\\¥)?(-)?(([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+)(\\.[0-9][0-9])?)?$";
	public static final String amount2 = "^(\\$|\\€|\\£|\\¥)?(-)?(([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+)(\\.[0-9][0-9]|(\\.-))?)?$";
	public static final String amount3 = "^(\\$|\\€|\\£|\\¥)?(-)?(([0-9]{1,3}\\.([0-9]{3}\\.)*[0-9]{3}|[0-9]+)(\\.[0-9][0-9]|(\\.-))?)?$";
	public static final String amount4 = "^(\\$|\\€|\\£|\\¥)?(-)?(([0-9]{1,3}( |,|\\.)([0-9]{3}( |,|\\.))*[0-9]{3}|[0-9]+)((\\.|,)[0-9][0-9]|((\\.|,)-))?)?$";
	public static final String amount5 = "^$";
	public static final String currency = "^(euro(s)?|eur|dollar(s)?|pound(s)?|\\$|\\€|\\£|\\¥|yen(s)?|yuan(s)?|usd|cad|gbp|jpy|cny|hkd)$";
	
	// subject regexp
	public static final String subject = "^((R|r)(E|e):?( )*(.*)( )+)([0-9]+)(( )*-( )*)(.*)(( )*(V|v)(S|s).( )*)(.*)$";
	//public static final String subject = "^((R|r)(E|e):?( )*(.*)( )+)([0-9]+)(( )*-( )*)(.*)(( )*(V|v)(S|s).( )*)(.*)((( )*:( )*)(.*))?$";
	public static final String subject_extra = "^((R|r)(E|e):( )*)?(.*)(( )+(F|f)(R|r)(O|o)(M|m)( )+)(.*)(( )*(T|t)(O|o)( )*)(.*)$";
	public static final String subject_from = "^((R|r)(E|e):( )*)?(.*)(( )*(F|f)(R|r)(O|o)(M|m)( )*)(.*)$";
	
	// ISIN regexp
	public static final String isin_regexp = "^[A-Z]{2}[A-Z0-9]{9}\\d$";
	
	// penntreebanck POS tags
	public static final String hash = "#";
	public static final String dollar = "$";
	public static final String apostrophe = "'";
	public static final String comma = ",";
	public static final String lrb = "-LRB-";
	public static final String rrb = "-RRB-";
	public static final String dot = ".";
	public static final String colon = ":";
	public static final String afx = "AFX";
	public static final String cc = "CC";
	public static final String cd = "CD";
	public static final String dt = "DT";
	public static final String ex = "EX";
	public static final String fw = "FW";
	public static final String hyph = "HYPH";
	public static final String in = "IN";
	public static final String jj = "JJ";
	public static final String jjr = "JJR";
	public static final String jjs = "JJS";
	public static final String ls = "LS";
	public static final String md = "MD";
	public static final String nil = "NIL";
	public static final String nn = "NN";
	public static final String nnp = "NNP";
	public static final String nnps = "NNPS";
	public static final String nns = "NNS";
	public static final String pdt = "PDT";
	public static final String pos = "POS";
	public static final String prp = "PRP";
	public static final String prpdollar = "PRP$";
	public static final String rb = "RB";
	public static final String rbr = "RBR";
	public static final String rbs = "RBS";
	public static final String rp = "RP";
	public static final String sym = "SYM";
	public static final String to = "TO";
	public static final String uh = "UH";
	public static final String vb = "VB";
	public static final String vbd = "VBD";
	public static final String vbg = "VBG";
	public static final String vbn = "VBN";
	public static final String vbp = "VBP";
	public static final String vbz = "VBZ";
	public static final String wdt = "WDT";
	public static final String wp = "WP";
	public static final String wpdollar = "WP$";
	public static final String wrb = "WRB";
	public static final String backquote = "``";
	
	// universal (conll) POS tags
	public static final String usym = "SYM";
	public static final String upunct = "PUNCT";
	public static final String uadj = "ADJ";
	public static final String ucconj = "CCONJ";
	public static final String unum = "NUM";
	public static final String udet = "DET";
	public static final String upron = "PRON";
	public static final String upropn = "PROPN";
	public static final String ux = "X";
	public static final String uadp = "ADP";
	public static final String uverb = "VERB";
	public static final String unoun = "NOUN";
	public static final String upart = "PART";
	public static final String uadv = "ADV";
	public static final String uintj = "INTJ";
	
	// Datapoints
	public static final String AGREEMENT_ID = "AGREEMENT_ID";
	public static final String COUNTER_PARTY_NAME = "COUNTER_PARTY_NAME";
	public static final String LEGAL_ENTITY = "LEGAL_ENTITY";
	public static final String SUBJECT_FROM = "SUBJECT_FROM";
	public static final String SUBJECT_TO = "SUBJECT_TO";
	public static final String SUBJECT_REASON = "SUBJECT_REASON";
	public static final String OTHER = "OTHER";
	public static final String FROM = "FROM";
	public static final String TO = "TO";
	
	
	
}