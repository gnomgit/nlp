package com.taiger.kp.citimails.nlp.oer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.springframework.util.Assert;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;
import com.taiger.kp.citimails.model.Word;
import com.taiger.kp.citimails.utils.StringTools;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
public class CitiWordsFinderOER implements OER {

	private Map<String, Map<String, String>> examples;

	public CitiWordsFinderOER() {
		examples = new LinkedHashMap<>();
		initialize();
	}

	@SuppressWarnings("deprecation")
	public CitiWordsFinderOER initialize() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology;
		try {
			ontology = manager.loadOntology(IRI.create(CitiWordsFinderOER.class.getClassLoader().getResource(Constants.CITI_ONTOLOGY_TTL_new).toString()));
			OWLDataFactory df = manager.getOWLDataFactory();
			
			Set<OWLClass> classes = ontology.getClassesInSignature();
			for (OWLClass oc : classes) {
				HashMap<String, String> inClass = new LinkedHashMap<>();
				log.info("Class: {}", oc.toStringID());
				IRI iri = IRI.create(oc.toStringID());
				
				OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
				OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

				NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(oc, true);
				Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
 
				for (OWLNamedIndividual ind : individuals) {
				    String s = ind.toStringID();
				    
				    Set<? extends OWLAxiom> axioms = ontology.getAxioms(ind);
				    axioms = ontology.getAnnotationAssertionAxioms(ind.getIRI()); // 
			        for (OWLAxiom ax : axioms) {
			        	inClass.put(extractSynonym(ax.toString()), ind.toStringID());
			        }
			        axioms = ontology.getDataPropertyAssertionAxioms(ind);
			        /*for (OWLAxiom ax : axioms) {
			        	log.info("getDataPropertyAssertionAxioms: {} ", ax);
			        }*/
			        axioms = ontology.getObjectPropertyAssertionAxioms(ind);
			        /*for (OWLAxiom ax : axioms) {
			        	log.info("getObjectPropertyAssertionAxioms: {} ", ax);
			        }*/
				    
				}
				
				Map<String, String> ordered = StringTools.sortDesc(inClass);
				examples.put(oc.toStringID(), ordered);
				
			}
			
			Map<String, Map<String, String>> orderedClasses = new TreeMap(Collections.reverseOrder());
			orderedClasses.putAll(examples);
			
			examples = orderedClasses;
			
			
		} catch (OWLOntologyCreationException e) {
			log.error(e.getMessage());
		}
		
		examples.entrySet().forEach( es -> {
			System.out.println(" - " + es.getKey());
			es.getValue().entrySet().forEach(System.out::println);
		});
		
		return this;
	}

	private String extractSynonym(String string) {
		StringBuilder sb = new StringBuilder();
		boolean begun = false;
		for (int i = 0; i < string.length(); i++) {
			if (!begun) {
				if (string.charAt(i) == '\"') {
					begun = true;
				}
			}	else {
				if (string.charAt(i) == '\"') {
					return sb.toString();
				}	else {
					sb.append(string.charAt(i));
				}
			}
		}
		return "";
	}

	@Override
	public Sentence annotate(Sentence sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getS(), "sentence content shouldn't be null");

		for (Map.Entry<String, Map<String, String>> oClass : examples.entrySet()) {
			for (Map.Entry<String, String> syn : oClass.getValue().entrySet()) {
				List<String> ind = new ArrayList<>();
				for (String word : syn.getKey().split(" ")) {
					ind.add(word);
				}
				
				System.out.println(ind);
				
				if (lcs(sentence, ind, sentence.getS().size(), ind.size(), oClass.getKey(), syn.getKey())) {
					System.out.println(ind);
					break;
				}
			}
		}

		return sentence;
	}
	
	// Returns LCS for X[0..m-1], Y[0..n-1] 
    private boolean lcs (Sentence X, List<String> Y, int m, int n, String classUrl, String key) { 
        int[][] L = new int[m+1][n+1]; 
   
        // Following steps build L[m+1][n+1] in bottom up fashion. Note 
        // that L[i][j] contains length of LCS of X[0..i-1] and Y[0..j-1]  
        for (int i = 0; i <= m; i++) { 
            for (int j = 0; j <= n; j++) { 
                if (i == 0 || j == 0) 
                    L[i][j] = 0; 
                else if (compare(X.getS().get(i-1), Y.get(j-1))) 
                    L[i][j] = L[i-1][j-1] + 1; 
                else
                    L[i][j] = Math.max(L[i-1][j], L[i][j-1]); 
            } 
        } 
   
        // Following code is used to print LCS 
        if (Y.size() != L[m][n]) return false;
        System.out.println(Arrays.deepToString(L));
        int index = L[m][n];
   
        // Start from the right-most-bottom-most corner and 
        // one by one store characters in lcs[] 
        int i = m, j = n; 
        int end = -1;
        int begin = -1;
        while (i > 0 && j > 0) { 
            // If current character in X[] and Y are same, then 
            // current character is part of LCS 
            if (compare(X.getS().get(i-1), Y.get(j-1))) {
                // Put current character in result 
            	X.getS().get(i-1).setOerTag(classUrl + "|" + examples.get(classUrl).get(key));
                  
                // reduce values of i, j and index 
                i--;  
                j--;  
                index--;      
                if (end == -1) end = begin = i;
                else begin = i;
            } 
   
            // If not same, then find the larger of two and 
            // go in the direction of larger value 
            else if (L[i-1][j] > L[i][j-1]) 
                i--; 
            else
                j--; 
        }
        
        System.out.println("[" + begin + ";" + end + "]");
        
        for (i = begin; i <= end; i++) {
        	X.getS().get(i).setOerTag(classUrl + "|" + examples.get(classUrl).get(key));
        }
        
        return true;
    } 
    
    private boolean compare (Word w, String s) {
    	if (w.getNerTag().isEmpty() || w.getNerTag().equals(Constants.O)) {
    		return w.getW().equals(s);
    	}
    	if (!s.contains(Constants.AT)) {
    		return false;
    	}
    	return w.getNerTag().toLowerCase().contains(s.toLowerCase().replace(Constants.AT, ""));
    }

}
