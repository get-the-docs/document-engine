package net.videki.templateutils.documentstructure.builder.core.service.impl;

import net.videki.templateutils.documentstructure.builder.core.documentstructure.DocumentStructureOptions;
import net.videki.templateutils.documentstructure.builder.core.service.ConfigurableDocumentStructureBuilder;
import net.videki.templateutils.template.core.documentstructure.DocumentStructure;
import net.videki.templateutils.template.core.documentstructure.ValueSet;
import net.videki.templateutils.template.core.service.exception.TemplateServiceConfigurationException;
import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

public class YmlDroolsOptionsDocStructureBuilder implements ConfigurableDocumentStructureBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(YmlDocStructureBuilder.class);

    private final YmlDocStructureBuilder dsBuilder = new YmlDocStructureBuilder();

    @Override
    public DocumentStructure build(final String config,
                                   final String options,
                                   final String configuration,
                                   final ValueSet values)
            throws TemplateServiceConfigurationException {

        final DocumentStructure baseDs = this.dsBuilder.build(config);
        final DocumentStructureOptions optionsDs = this.dsBuilder.buildOptions(options);


        KieServices kieServices = KieServices.Factory.get();
        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();

        KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel( "KBase1 ")
                .setDefault( true )
                .setEqualsBehavior( EqualityBehaviorOption.EQUALITY )
                .setEventProcessingMode( EventProcessingOption.STREAM )
                .getRuleTemplates().add();

        KieSessionModel ksessionModel1 = kieBaseModel1.newKieSessionModel( "KSession1" )
                .setDefault( true )
                .setType( KieSessionModel.KieSessionType.STATEFUL )
                .setClockType( ClockTypeOption.get("realtime") );

        try {
            KieFileSystem kfs = kieServices.newKieFileSystem();
            kfs.write(configuration,
                    kieServices.getResources().newClassPathResource(configuration)
                            .setResourceType(ResourceType.DTABLE));
            kfs.write("/full-example-rulebased/rules/contract-options-template.drt",
                    kieServices.getResources().newInputStreamResource(
                            this.getClass().getResourceAsStream("/full-example-rulebased/rules/contract-options-template.drt"))
                            .setResourceType(ResourceType.DRT));
            kfs.writeKModuleXML(kieModuleModel.toXML());

            kieServices.newKieBuilder( kfs ).buildAll();
            KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());


            final KieSession kieSession = kieContainer.newKieSession("KSession1");
            kieSession.insert(getBaseTemplateElements(baseDs));
            kieSession.insert(getTemplateElementOptions(optionsDs));
            kieSession.insert(getValuesFromContexts(values));

            kieSession.addEventListener( new DefaultAgendaEventListener() {
                public void afterMatchFired(AfterMatchFiredEvent event) {
                    super.afterMatchFired( event );
                    System.out.println( event );
                }
            });

            kieSession.fireAllRules();
        } catch (Exception e) {
            System.out.println(e);
        }











/*
        KieRepository kieRepository = kieServices.getRepository();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        final Resource xlsRes = ResourceFactory.newClassPathResource(configuration);
        kieFileSystem.write(xlsRes);
        final Resource drtRes = ResourceFactory.newClassPathResource("full-example-rulebased/rules/contract-options-template.drt");
        kieFileSystem.write(drtRes);

        kieServices.getResources().newClassPathResource(configuration).setResourceType(ResourceType.DTABLE);
        kieServices.getResources().newClassPathResource("full-example-rulebased/rules/contract-options-template.drt").setResourceType(ResourceType.DRT);
        kieFileSystem.writeKModuleXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kmodule xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"TemplatesKB\" packages=\"/full-example-rulebased.rules\">\n" +
                "    <ruleTemplate dtable=\"/full-example-rulebased/rules/contract-options_v01.xlsx\"\n" +
                "                  template=\"/full-example-rulebased/rules/contract-options-template.drt\"\n" +
                "                  row=\"3\" col=\"3\"/>\n" +
                "      <ksession name=\"TemplatesKS\"/>\n" +
                "      </kbase>\n" +
                "</kmodule>");
//        kbuilder.add( xlsRes, ResourceType.DTABLE, dtableconfiguration );

//        addResource(kieFileSystem, configuration);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
//        KieBuilder kieBuilder = kieServices.newKieBuilder(new File("full-example-rulebased/rules)"));



        kieBuilder.buildAll();
        for (Message message : kieBuilder.getResults().getMessages()) {
            System.out.println(message);
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
*/
/*        final DecisionTableConfiguration dtableconfiguration =
                KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dtableconfiguration.setInputType( DecisionTableInputType.XLS );

        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem().write( dtableconfiguration );
        KieBuilder kb = ks.newKieBuilder( kfs ).buildAll();

        KieServices ks = populateKieFileSystem( dt );

        // get the session
        KieSession ksession = ks.newKieContainer(ks.getRepository().getDefaultReleaseId()).newKieSession();

        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        final Resource xlsRes = ResourceFactory.newClassPathResource(configuration, this.getClass());
        kbuilder.add( xlsRes, ResourceType.DTABLE, dtableconfiguration );

        final KieServices kieServices = KieServices.Factory.get();
        final KieContainer kieContainer = kieServices.getKieClasspathContainer();
*/
/*
        final KieSession kieSession = kieContainer.newKieSession("TemplatesKS");
        kieSession.insert(getBaseTemplateElements(baseDs));
        kieSession.insert(getTemplateElementOptions(optionsDs));
        kieSession.insert(getValuesFromContexts(values));

        kieSession.addEventListener( new DefaultAgendaEventListener() {
            public void afterMatchFired(AfterMatchFiredEvent event) {
                super.afterMatchFired( event );
                System.out.println( event );
            }
        });

        kieSession.fireAllRules();
*/
        return baseDs;
    }

    private void addResource(KieFileSystem kieFileSystem, String name) {
        kieFileSystem.write(name, ResourceFactory .newClassPathResource(name));

    }


    private KieServices populateKieFileSystem(Resource dt) {
        KieServices ks = KieServices.Factory.get();

        KieFileSystem kfs = ks.newKieFileSystem().write( dt );
        KieBuilder kb = ks.newKieBuilder( kfs ).buildAll();
//        assertTrue( kb.getResults().getMessages().isEmpty() );

        return ks;
    }

    private List<Object> getBaseTemplateElements(final DocumentStructure ds) {
        final List<Object> results = new LinkedList<>();

        if (ds != null) {
            results.addAll(ds.getElements());
            results.add(ds);
        }

        return results;
    }

    private List<Object> getTemplateElementOptions(final DocumentStructureOptions dso) {
        final List<Object> results = new LinkedList<>();

        if (dso != null) {
            results.addAll(dso.getElements());
        }

        return results;
    }

    private List<Object> getValuesFromContexts(final ValueSet values) {
        final List<Object> results = new LinkedList<>();

        if (values != null) {
                results.addAll(values.getValues().values());
        }

        return results;
    }

}
