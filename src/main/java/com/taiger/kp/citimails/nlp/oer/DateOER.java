package com.taiger.kp.citimails.nlp.oer;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import com.taiger.kp.citimails.model.Constants;
import com.taiger.kp.citimails.model.Sentence;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DateOER implements OER {
	
	public DateOER() {
		initialize();
	}

	@SuppressWarnings("deprecation")
	public DateOER initialize() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = null;
		OWLReasoner reasoner = null;
		
		OWLDataFactory df = manager.getOWLDataFactory();
		IRI pizzaontology = IRI.create(DateOER.class.getClassLoader().getResource(Constants.GREGORIAN_ONTOLOGY_OWL).toString());
		try {
			ontology = manager.loadOntology(pizzaontology);
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			reasoner = reasonerFactory.createReasoner(ontology);
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
			System.out.println("----------------");
			//reasoner.getSubClasses(df.getOWLClass(IRI.create("http://www.w3.org/2006/timezone#")), false).forEach(System.out::println);
			
			Set<OWLClass> classes = ontology.getClassesInSignature();
			for (OWLClass oc : classes) {
				HashMap<String, String> inClass = new LinkedHashMap<>();
				log.info("Class: {}", oc.toStringID());

				NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(oc, true);
				Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
 
				for (OWLNamedIndividual ind : individuals) {
				    Set<? extends OWLAxiom> axioms = ontology.getAxioms(ind);
				    axioms = ontology.getAnnotationAssertionAxioms(ind.getIRI()); // 
			        for (OWLAxiom ax : axioms) {
			        	log.info("{} | {}", ax.toString(), ind.toStringID());
			        }
			        axioms = ontology.getDataPropertyAssertionAxioms(ind);
			        for (OWLAxiom ax : axioms) {
			        	log.info("getDataPropertyAssertionAxioms: {} ", ax);
			        }
			        axioms = ontology.getObjectPropertyAssertionAxioms(ind);
			        for (OWLAxiom ax : axioms) {
			        	log.info("getObjectPropertyAssertionAxioms: {} ", ax);
			        }
				}
				
			}
			
			System.out.println("----------------");
		} 	catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        // Create the DLQueryPrinter helper class. This will manage the
        // parsing of input and printing of results
        DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner, shortFormProvider), shortFormProvider);
		
		dlQueryPrinter.askQuery("Thing that {Sunday,Monday}");
		
		return this;
	}
	
	@Override
	public Sentence annotate (Sentence sentence) {
		
		
		
		return sentence;
	}
	
	
	
	class DLQueryEngine {
	    private final OWLReasoner reasoner;
	    private final DLQueryParser parser;

	    public DLQueryEngine(OWLReasoner reasoner, ShortFormProvider shortFormProvider) {
	        this.reasoner = reasoner;
	        parser = new DLQueryParser(reasoner.getRootOntology(), shortFormProvider);
	    }

	    public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        NodeSet<OWLClass> superClasses = reasoner
	                .getSuperClasses(classExpression, direct);
	        return superClasses.getFlattened();
	    }

	    public Set<OWLClass> getEquivalentClasses(String classExpressionString) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
	        Set<OWLClass> result = null;
	        if (classExpression.isAnonymous()) {
	            result = equivalentClasses.getEntities();
	        } else {
	            result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
	        }
	        return result;
	        }

	    public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
	        return subClasses.getFlattened();
	        }

	    public Set<OWLNamedIndividual> getInstances(String classExpressionString,
	            boolean direct) {
	        if (classExpressionString.trim().length() == 0) {
	            return Collections.emptySet();
	        }
	        OWLClassExpression classExpression = parser
	                .parseClassExpression(classExpressionString);
	        NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression,
	                direct);
	        return individuals.getFlattened();
	        }
	    }

	class DLQueryParser {
	    private final OWLOntology rootOntology;
	    private final BidirectionalShortFormProvider bidiShortFormProvider;

	    public DLQueryParser(OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
	        this.rootOntology = rootOntology;
	        OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
	        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
	        // Create a bidirectional short form provider to do the actual mapping.
	        // It will generate names using the input
	        // short form provider.
	        bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager,
	                importsClosure, shortFormProvider);
	    }

	    public OWLClassExpression parseClassExpression(String classExpressionString) {
	        OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager()
	                .getOWLDataFactory();
	        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(dataFactory, classExpressionString);
	        parser.setDefaultOntology(rootOntology);
	        OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
	        parser.setOWLEntityChecker(entityChecker);
	        return parser.parseClassExpression();
	        }
	    }

	class DLQueryPrinter {
	    private final DLQueryEngine dlQueryEngine;
	    private final ShortFormProvider shortFormProvider;

	    public DLQueryPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
	        this.shortFormProvider = shortFormProvider;
	        dlQueryEngine = engine;
	        }

	    public void askQuery(String classExpression) {
	        if (classExpression.length() == 0) {
	            System.out.println("No class expression specified");
	        } else {
	            try {
	                StringBuilder sb = new StringBuilder();
	                sb.append("\nQUERY:   ").append(classExpression).append("\n\n");
	                Set<OWLClass> superClasses = dlQueryEngine.getSuperClasses(
	                        classExpression, false);
	                printEntities("SuperClasses", superClasses, sb);
	                Set<OWLClass> equivalentClasses = dlQueryEngine
	                        .getEquivalentClasses(classExpression);
	                printEntities("EquivalentClasses", equivalentClasses, sb);
	                Set<OWLClass> subClasses = dlQueryEngine.getSubClasses(classExpression,
	                        true);
	                printEntities("SubClasses", subClasses, sb);
	                Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(
	                        classExpression, true);
	                printEntities("Instances", individuals, sb);
	                System.out.println(sb.toString());
	            } catch (ParserException e) {
	                System.out.println(e.getMessage());
	            }
	            }
	        }

	    private void printEntities(String name, Set<? extends OWLEntity> entities, StringBuilder sb) {
	        sb.append(name);
	        int length = 50 - name.length();
	        for (int i = 0; i < length; i++) {
	            sb.append(".");
	        }
	        sb.append("\n\n");
	        if (!entities.isEmpty()) {
	            for (OWLEntity entity : entities) {
	                sb.append("\t").append(shortFormProvider.getShortForm(entity))
	                        .append("\n");
	            }
	        } else {
	            sb.append("\t[NONE]\n");
	            }
	        sb.append("\n");
	        }
	    }
	
}
